package com.diamante.campusgigs.exception;


public class GigNotFoundException extends RuntimeException {
    public GigNotFoundException(Long id) {
        super("Serviço não encontrado: " + id);
    }
}