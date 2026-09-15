package com.diamante.campusgigs.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CepResponse {

    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
    private Boolean erro;
}
