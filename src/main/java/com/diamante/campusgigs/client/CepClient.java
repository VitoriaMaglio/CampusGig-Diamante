package com.diamante.campusgigs.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface CepClient {

    @GetExchange("/{cep}/json")
    CepResponse getByCep(@PathVariable String cep);
}

