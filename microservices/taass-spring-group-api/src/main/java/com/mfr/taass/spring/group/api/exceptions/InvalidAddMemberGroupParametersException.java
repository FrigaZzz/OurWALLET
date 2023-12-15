/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.group.api.exceptions;

import com.mfr.taass.spring.group.api.utils.InputUser;

/**
 *
 * @author matteo
 */
public class InvalidAddMemberGroupParametersException extends Exception {

    private final InputUser providedUser;
    

    public InvalidAddMemberGroupParametersException(InputUser user) {
        this.providedUser = user;
    }

    public InputUser getProvidedUser() {
        return providedUser;
    }
    
    
}
