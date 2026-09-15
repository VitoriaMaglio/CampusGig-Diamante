package com.diamante.campusgigs.exception;


public class EmailAlreadyInUseException extends RuntimeException {

    public EmailAlreadyInUseException(String email) {
        super("Já existe um usuário cadastrado com o e-mail: " + email);
    }
}