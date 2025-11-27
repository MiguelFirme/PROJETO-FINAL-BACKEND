package com.example.ProjetoFinal.Services;

import com.example.ProjetoFinal.DTOs.InvestimentoRequest;
import com.example.ProjetoFinal.DTOs.UploadResponse;
import com.example.ProjetoFinal.Entidades.Carteira;
import com.example.ProjetoFinal.Entidades.Investimento;
import com.example.ProjetoFinal.Exceptions.ResourceNotFoundException;
import com.example.ProjetoFinal.Exceptions.ValidationException;
import com.example.ProjetoFinal.Repositorys.CarteiraRepository;
import com.example.ProjetoFinal.Repositorys.InvestimentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class InvestimentoService {

    private final InvestimentoRepository investimentoRepository;
    private final CarteiraRepository carteiraRepository;
    private final AtivoService ativoService;

    public InvestimentoService(InvestimentoRepository investimentoRepository,
                               CarteiraRepository carteiraRepository,
                               AtivoService ativoService) {
        this.investimentoRepository = investimentoRepository;
        this.carteiraRepository = carteiraRepository;
        this.ativoService = ativoService;
    }

    public List<Investimento> listarPorUsuario(UUID usuarioId) {
        return investimentoRepository.findByCarteiraUsuarioId(usuarioId);
    }

    @Transactional
    public Investimento criarInvestimento(UUID usuarioId, InvestimentoRequest req) {
        Carteira carteira = carteiraRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Carteira", "usuário id", usuarioId));

        return processarNovoInvestimento(carteira, req);
    }

    @Transactional
    public UploadResponse processarUploadInvestimentos(UUID usuarioId, MultipartFile file) {
        Carteira carteira = carteiraRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Carteira", "usuário id", usuarioId));

        List<String> erros = new ArrayList<>();
        int sucesso = 0;
        int totalRegistros = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                totalRegistros++;
                String[] parts = line.split(";");

                if (parts.length != 3) {
                    erros.add(String.format("Linha %d: Formato inválido. Esperado: TICKER;VALOR;DIAS. Linha: %s", totalRegistros, line));
                    continue;
                }

                try {
                    String ticker = parts[0].trim();
                    Double valor = Double.parseDouble(parts[1].trim());
                    Integer dias = Integer.parseInt(parts[2].trim());

                    InvestimentoRequest req = new InvestimentoRequest();
                    req.ticker = ticker;
                    req.valor = valor;
                    req.dias = dias;

                    // Validação de dados com DTO
                    if (ticker.isBlank() || valor <= 0 || dias <= 0) {
                        erros.add(String.format("Linha %d (%s): Dados inválidos. Ticker, valor e dias devem ser válidos.", totalRegistros, line));
                        continue;
                    }

                    processarNovoInvestimento(carteira, req);
                    sucesso++;

                } catch (NumberFormatException e) {
                    erros.add(String.format("Linha %d (%s): Valor ou dias não são números válidos.", totalRegistros, line));
                } catch (ValidationException e) {
                    erros.add(String.format("Linha %d (%s): Erro de validação: %s", totalRegistros, line, e.getMessage()));
                } catch (ResourceNotFoundException e) {
                    erros.add(String.format("Linha %d (%s): Erro de recurso: %s", totalRegistros, line, e.getMessage()));
                } catch (Exception e) {
                    erros.add(String.format("Linha %d (%s): Erro inesperado: %s", totalRegistros, line, e.getMessage()));
                }
            }
        } catch (Exception e) {
            erros.add("Erro ao ler o arquivo: " + e.getMessage());
        }

        return new UploadResponse(totalRegistros, sucesso, totalRegistros - sucesso, erros);
    }

    private Investimento processarNovoInvestimento(Carteira carteira, InvestimentoRequest req) {
        String ticker = req.ticker.toUpperCase();
        double valor = req.valor;
        int dias = req.dias;

        Map<String, Object> dados = ativoService.buscarAtivo(ticker);

        Double precoAtual = null;
        Object p = dados.get("regularMarketPrice");
        if (p == null) p = dados.get("close");
        if (p == null) p = dados.get("last");
        if (p != null) {
            try { precoAtual = Double.parseDouble(p.toString()); } catch (Exception ignored) { precoAtual = null; }
        }
        if (precoAtual == null) {
            throw new ValidationException("Não foi possível obter preço atual para o ativo: " + ticker);
        }

        // ❗ REGRA DE NEGÓCIO
        if (valor < precoAtual) {
            throw new ValidationException(
                    String.format("O valor investido (%.2f) não pode ser menor que o preço atual do ativo (%.2f).", valor, precoAtual)
            );
        }

        Double taxaAnual = ativoService.obterTaxaAnual(ticker, dados);
        if (taxaAnual == null) taxaAnual = 0.10;

        double taxaDiaria = Math.pow(1.0 + taxaAnual, 1.0 / 252.0) - 1.0;
        double valorFinal = valor * Math.pow(1.0 + taxaDiaria, dias);
        double rendimento = valorFinal - valor;

        Investimento inv = new Investimento();
        inv.setTicker(ticker);
        inv.setValorInvestido(valor);
        inv.setDias(dias);
        inv.setPrecoNoMomento(precoAtual);
        inv.setTaxaEstimativa(taxaDiaria);
        inv.setValorEstimadoFinal(valorFinal);
        inv.setRendimentoEstimado(rendimento);
        inv.setCarteira(carteira);

        return investimentoRepository.save(inv);
    }

    @Transactional
    public void deletar(Long id) {
        if (!investimentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Investimento", "id", id);
        }
        investimentoRepository.deleteById(id);
    }

    public List<Investimento> listarPorCarteira(UUID carteiraId) {
        return investimentoRepository.findByCarteiraId(carteiraId);
    }

    public Investimento buscarPorId(Long id) {
        return investimentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investimento", "id", id));
    }

    @Transactional
    public Investimento atualizar(Long id, InvestimentoRequest req) {

        Investimento inv = investimentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investimento", "id", id));

        inv.setTicker(req.ticker.toUpperCase());
        inv.setValorInvestido(req.valor);
        inv.setDias(req.dias);

        Map<String, Object> dados = ativoService.buscarAtivo(inv.getTicker());

        Double precoAtual = null;
        Object p = dados.get("regularMarketPrice");
        if (p == null) p = dados.get("close");
        if (p == null) p = dados.get("last");
        if (p != null) {
            try { precoAtual = Double.parseDouble(p.toString()); } catch (Exception ignored) { precoAtual = null; }
        }
        if (precoAtual == null) {
            throw new ValidationException("Não foi possível obter preço atual para o ativo: " + inv.getTicker());
        }

        // ❗ REGRA DE NEGÓCIO
        if (req.valor < precoAtual) {
            throw new ValidationException(
                    String.format("O valor investido (%.2f) não pode ser menor que o preço atual do ativo (%.2f).", req.valor, precoAtual)
            );
        }

        Double taxaAnual = ativoService.obterTaxaAnual(inv.getTicker(), dados);
        if (taxaAnual == null) taxaAnual = 0.10;

        double taxaDiaria = Math.pow(1 + taxaAnual, 1.0 / 252.0) - 1.0;
        double valorFinal = req.valor * Math.pow(1 + taxaDiaria, req.dias);
        double rendimento = valorFinal - req.valor;

        inv.setPrecoNoMomento(precoAtual);
        inv.setTaxaEstimativa(taxaDiaria);
        inv.setValorEstimadoFinal(valorFinal);
        inv.setRendimentoEstimado(rendimento);

        return investimentoRepository.save(inv);
    }
}
