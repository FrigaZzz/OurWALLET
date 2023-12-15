/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.group.api.exceptions;

import com.mfr.taass.spring.group.api.utils.InputGoal;

/**
 *
 * @author matteo
 */
public class InvalidAddGoalParametersCorrectness extends Exception {
    
    private final InputGoal providedGoal;

    public InvalidAddGoalParametersCorrectness(InputGoal goal) {
        this.providedGoal = goal;
    }

    public InputGoal getProvidedGoal() {
        return providedGoal;
    }
    
    
    
}
