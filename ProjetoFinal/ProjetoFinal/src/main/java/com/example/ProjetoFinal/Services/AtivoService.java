package com.example.ProjetoFinal.Services;

import com.example.ProjetoFinal.Exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class AtivoService {

    private final RestTemplate rest = new RestTemplate();

    @Value("${brapi.base:https://brapi.dev}")
    private String brapiBase;

    @Value("${brapi.token:}")
    private String brapiToken;

    /**
     * Busca dados básicos do ativo via /api/quote/{ticker} (retorna o primeiro resultado como Map).
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> buscarAtivo(String ticker) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(brapiBase)
                    .pathSegment("api", "quote", ticker);
            if (brapiToken != null && !brapiToken.isBlank()) {
                builder.queryParam("token", brapiToken);
            }
            String url = builder.build().toUriString();
            Map resp = rest.getForObject(url, Map.class);
            if (resp == null) {
                throw new ResourceNotFoundException("Ativo", "ticker", ticker);
            }
            Object results = resp.get("results");
            if (results instanceof List && !((List) results).isEmpty()) {
                Object first = ((List) results).get(0);
                if (first instanceof Map) {
                    return (Map<String, Object>) first;
                }
            }
            // Se a API retornou algo, mas sem resultados válidos
            throw new ResourceNotFoundException("Ativo", "ticker", ticker);
        } catch (HttpClientErrorException.NotFound ex) {
            // Se a API retornar 404
            throw new ResourceNotFoundException("Ativo", "ticker", ticker);
        } catch (Exception e) {
            // Outros erros de comunicação
            throw new RuntimeException("Erro ao buscar dados do ativo " + ticker + ": " + e.getMessage(), e);
        }
    }

    /**
     * Tenta extrair uma taxa anual (decimal) a partir dos dados do ativo.
     *
     * Estratégia:
     * 1) Se existir `regularMarketChangePercent` assume que é a variação do dia (porcentagem).
     *    Annualiza com (1 + daily)^252 - 1
     * 2) Se existir `dividendYield` ou `trailingAnnualDividendYield` usa como proxy (já anual)
     * 3) Se existir `yearlyReturn`/`annualReturn` usa direto
     * 4) Se não encontrar, retorna null (caller decide fallback)
     *
     * Retorna taxa em decimal (ex: 0.23 para 23% a.a.)
     */
    public Double obterTaxaAnual(String ticker, Map<String, Object> dados) {
        try {
            // 1) tentar regularMarketChangePercent (porcentagem, ex: 1.23)
            Object changeObj = dados.get("regularMarketChangePercent");
            if (changeObj != null) {
                try {
                    double dailyPercent = Double.parseDouble(changeObj.toString());
                    double daily = dailyPercent / 100.0;
                    double annual = Math.pow(1.0 + daily, 252.0) - 1.0;
                    return annual;
                } catch (Exception ignored) {}
            }

            // 2) tentar dividend yield (já anual)
            Object divYield = dados.get("dividendYield");
            if (divYield == null) divYield = dados.get("trailingAnnualDividendYield");
            if (divYield != null) {
                try {
                    double v = Double.parseDouble(divYield.toString());
                    return v; // já em decimal esperado
                } catch (Exception ignored) {}
            }

            // 3) tentar campos comuns de retorno anual (nomes podem variar)
            Object annualObj = dados.get("annualReturn");
            if (annualObj == null) annualObj = dados.get("ytdReturn");
            if (annualObj != null) {
                try {
                    double v = Double.parseDouble(annualObj.toString());
                    // se está em porcentagem (por ex 5.2), converte
                    if (v > 1.0) v = v / 100.0;
                    return v;
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        return null;
    }
}
