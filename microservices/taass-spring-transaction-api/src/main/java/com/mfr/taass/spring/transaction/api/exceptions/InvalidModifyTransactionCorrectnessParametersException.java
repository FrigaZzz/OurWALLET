/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.exceptions;
import com.mfr.taass.spring.transaction.api.utils.InputTransaction;
/**
 *
 * @author matteo
 */
public class InvalidModifyTransactionCorrectnessParametersException extends Exception {

    private final InputTransaction providedTransaction;

    public InvalidModifyTransactionCorrectnessParametersException(InputTransaction providedTransaction) {
        this.providedTransaction = providedTransaction;
    }

    public InputTransaction getProvidedTransaction() {
        return providedTransaction;
    }
    
}
