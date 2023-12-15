package com.mfr.taass.spring.auth.api.exceptions;

public class CredentialsAlreadyExistsException extends Exception {
    private final boolean usernameExists;
    private final boolean emailExists;

    public CredentialsAlreadyExistsException(boolean usernameExists, boolean emailExists) {
        this.usernameExists = usernameExists;
        this.emailExists = emailExists;
    }

    public boolean usernameExists() {
        return usernameExists;
    }

    public boolean emailExists() {
        return emailExists;
    }
}
