package com.example.ProjetoFinal.DTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InvestimentoRequest {

    @NotBlank(message = "O ticker é obrigatório")
    public String ticker;

    @NotNull(message = "O valor é obrigatório")
    @Min(value = 0, message = "O valor investido deve ser positivo")
    public Double valor;

    @NotNull(message = "O número de dias é obrigatório")
    @Min(value = 1, message = "O número de dias deve ser no mínimo 1")
    public Integer dias;
}
