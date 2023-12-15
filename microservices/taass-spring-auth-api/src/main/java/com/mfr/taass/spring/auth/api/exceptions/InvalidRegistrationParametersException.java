/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.auth.api.exceptions;

import com.mfr.taass.spring.auth.api.beans.UserInput;
import com.mfr.taass.spring.auth.api.entities.User;

/**
 *
 * @author Luca
 */
public class InvalidRegistrationParametersException extends Exception {
    private final UserInput providedUser;

    public InvalidRegistrationParametersException(UserInput providedUser) {
        this.providedUser = providedUser;
    }

    public UserInput getProvidedUser() {
        return providedUser;
    }
}
