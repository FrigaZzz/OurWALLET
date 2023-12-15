/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.exceptions;
import com.mfr.taass.spring.transaction.api.entities.Transaction;
import com.mfr.taass.spring.transaction.api.utils.InputTransaction;

/**
 *
 * @author matteo
 */
public class InvalidDeleteTransactionCorrectnessParametersException extends Exception {

    private final Transaction providedTransaction;

    public InvalidDeleteTransactionCorrectnessParametersException(Transaction providedTransaction) {
        this.providedTransaction = providedTransaction;
    }

    public Transaction getProvidedTransaction() {
        return providedTransaction;
    }
    
}
