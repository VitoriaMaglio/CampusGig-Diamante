package com.diamante.campusgigs.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateZipCodeRequest {

    @NotBlank(message = "CEP é obrigatório")
    private String zipCode;
}
