/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.group.api.exceptions;

import com.mfr.taass.spring.group.api.entities.Groups;
import com.mfr.taass.spring.group.api.utils.InputGroup;

/**
 *
 * @author matteo
 */
public class InvalidModifyMemberGroupCorrectnessException extends Exception {

   private final Groups providedGroup;

    public InvalidModifyMemberGroupCorrectnessException(Groups providedGroup) {
        this.providedGroup = providedGroup;
    }
    
    public Groups getProvidedGroup() {
        return providedGroup;
    }
}
