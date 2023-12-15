/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.auth.api.beans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luca
 */
public class ErrorsMeta extends StatusMeta {
    
    public static final String  ERROR_MISSING_USERNAME = "missingUsername";
    public static final String  ERROR_MISSING_EMAIL = "missingEmail";
    public static final String  ERROR_MISSING_PASSWORD = "missingPassword";
    public static final String  ERROR_INVALID_USERNAME = "invalidUsername";
    public static final String  ERROR_INVALID_EMAIL = "invalidEmail";
    public static final String  ERROR_USERNAME_EXISTS = "usernameExists";
    public static final String  ERROR_EMAIL_EXISTS = "emailExists";
    public static String ERROR_INVALID_JSON="invalidJson";
    
    private final List<String> errors;
    
    public ErrorsMeta(int status) {
        super(status);
        this.errors = new ArrayList<>();
    }

    public List<String> getErrors() {
        return errors;
    }
}
