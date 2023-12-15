/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.auth.api.exceptions;

import com.mfr.taass.spring.auth.api.beans.Credentials;
import com.mfr.taass.spring.auth.api.entities.User;

/**
 *
 * @author Luca
 */
public class InvalidLoginParametersException extends Exception {
    private final Credentials providedCredentials;

    public InvalidLoginParametersException(Credentials providedCredentials) {
        this.providedCredentials = providedCredentials;
    }

    public Credentials getProvidedCredentials() {
        return providedCredentials;
    }
}
