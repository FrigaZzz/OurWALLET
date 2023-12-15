/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.checks;

import com.mfr.taass.spring.transaction.api.utils.InputTransaction;
import com.mfr.taass.spring.transaction.api.exceptions.InvalidAddTransactionParametersException;
import com.mfr.taass.spring.transaction.api.exceptions.InvalidJWTTokenException;
import com.mfr.taass.spring.transaction.api.exceptions.InvalidModifyTransactionParametersException;
import com.mfr.taass.spring.transaction.api.utils.MiniAccount;
import com.mfr.taass.spring.transaction.api.utils.JwtUser;

/**
 *
 * @author matteo
 */
public class Presence {
    
    public static void addTransaction(InputTransaction newTransaction) throws InvalidAddTransactionParametersException {
        boolean isValid = newTransaction.getAmount()!= null
                && newTransaction.getGroupSenderID()!= null
                && newTransaction.getAccountSenderID()!= null
                && newTransaction.getCategoryID()!= null;
               
        if (!isValid){
            throw new InvalidAddTransactionParametersException(newTransaction);
        }
    }
    

    public static void modifyTransaction(InputTransaction modifiedTransaction) throws InvalidModifyTransactionParametersException {
         boolean isValid = modifiedTransaction.getAmount()!= null
                && modifiedTransaction.getGroupSenderID()!= null
                && modifiedTransaction.getAccountSenderID()!= null
                && modifiedTransaction.getId() != null 
                && modifiedTransaction.getCategoryID()!= null;
                
        if (!isValid){
            throw new InvalidModifyTransactionParametersException(modifiedTransaction);
        }

    }
    
    public static void addAccount(MiniAccount newAccount) throws InvalidAddTransactionParametersException {
        boolean isValid = newAccount.getIsEasyPay()!= null
                && newAccount.getName()!= null;
                
        if (!isValid){
            throw new InvalidAddTransactionParametersException(null);
        }
    }

}