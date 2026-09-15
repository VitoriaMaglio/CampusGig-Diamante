package com.diamante.campusgigs.exception;
public class CepServiceUnavailableException extends RuntimeException {
    public CepServiceUnavailableException() {
        super("Serviço de CEP indisponível no momento, tente novamente mais tarde");
    }
}
