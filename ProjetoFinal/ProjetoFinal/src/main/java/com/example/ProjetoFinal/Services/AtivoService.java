package com.example.ProjetoFinal.Services;

import com.example.ProjetoFinal.Interfaces.BrapiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class AtivoService {

    @Autowired
    private BrapiClient brapiClient;

    private static final String TOKEN = "oByBAARQ36b2ye6jdYCMow";

    public Map<String, Object> buscarAtivo(String codigo) {
        return brapiClient.getAtivo(codigo, TOKEN);
    }
}
