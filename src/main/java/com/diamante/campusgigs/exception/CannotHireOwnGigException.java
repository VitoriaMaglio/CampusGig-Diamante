package com.diamante.campusgigs.exception;

public class CannotHireOwnGigException extends RuntimeException {
    public CannotHireOwnGigException() {
        super("Você não pode contratar o próprio serviço");
    }
}