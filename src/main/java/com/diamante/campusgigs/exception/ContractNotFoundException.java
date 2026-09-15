package com.diamante.campusgigs.exception;


public class ContractNotFoundException extends RuntimeException {
    public ContractNotFoundException(Long id) {
        super("Contratação não encontrada: " + id);
    }
}