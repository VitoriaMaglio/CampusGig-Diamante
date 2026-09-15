package com.diamante.campusgigs.exception;

public class GigNotActiveException extends RuntimeException {
    public GigNotActiveException(Long id) {
        super("Serviço " + id + " não está ativo e não pode ser contratado");
    }
}
