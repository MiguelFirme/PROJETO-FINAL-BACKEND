package com.example.ProjetoFinal.Interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@FeignClient(name = "brapi", url = "https://brapi.dev/api")
public interface BrapiClient {

    @GetMapping("/quote")
    Map<String, Object> getAtivo(
            @RequestParam("symbol") String codigo,
            @RequestParam("token") String token
    );
}
