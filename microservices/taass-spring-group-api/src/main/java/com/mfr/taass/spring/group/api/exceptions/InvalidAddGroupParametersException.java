/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.group.api.exceptions;

import com.mfr.taass.spring.group.api.utils.InputGroup;
/**
 *
 * @author Luca
 */
public class InvalidAddGroupParametersException extends Exception {
    private final InputGroup providedGroup;

    public InvalidAddGroupParametersException(InputGroup providedGroup) {
        this.providedGroup = providedGroup;
    }
    
    public InputGroup getProvidedGroup() {
        return providedGroup;
    }
    
    

}
