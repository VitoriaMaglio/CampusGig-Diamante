package com.diamante.campusgigs.entity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateContractRequest {

    @NotNull(message = "gigId é obrigatório")
    private Long gigId;
}