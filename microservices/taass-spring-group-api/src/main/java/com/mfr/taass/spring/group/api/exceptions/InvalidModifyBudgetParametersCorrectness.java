/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.group.api.exceptions;

import com.mfr.taass.spring.group.api.utils.InputBudget;
/**
 *
 * @author Luca
 */
public class InvalidModifyBudgetParametersCorrectness extends Exception {
    private final InputBudget providedGroup;

    public InvalidModifyBudgetParametersCorrectness(InputBudget providedGroup) {
        this.providedGroup = providedGroup;
    }
    
    public InputBudget getProvidedGroup() {
        return providedGroup;
    }
    
    

}
