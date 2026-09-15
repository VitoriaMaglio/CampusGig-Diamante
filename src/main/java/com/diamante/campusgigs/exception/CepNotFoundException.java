package com.diamante.campusgigs.exception;

public class CepNotFoundException extends RuntimeException {
    public CepNotFoundException(String cep) {
        super("CEP não encontrado: " + cep);
    }
}