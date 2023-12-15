/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.group.api.exceptions;

import com.mfr.taass.spring.group.api.utils.InputGroup;

/**
 *
 * @author matteo
 */
public class InvalidNameGroupParametersException extends Exception {

    public InvalidNameGroupParametersException(InputGroup newGroup) {
    }
    
}
