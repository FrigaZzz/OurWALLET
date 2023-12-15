/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.services;


import com.mfr.taass.spring.transaction.api.controllers.Mapper.TransactionMapper;
import com.mfr.taass.spring.transaction.api.controllers.Specification.TransactionListSpecification;
import com.mfr.taass.spring.transaction.api.controllers.dto.TransactionListRequest;
import com.mfr.taass.spring.transaction.api.entities.Account;
import com.mfr.taass.spring.transaction.api.entities.Groups;
import com.mfr.taass.spring.transaction.api.entities.Transaction;
import com.mfr.taass.spring.transaction.api.repos.TransactionRepository;
import com.mfr.taass.spring.transaction.api.utils.MiniAccount;
import com.mfr.taass.spring.transaction.api.utils.MiniCategory;
import com.mfr.taass.spring.transaction.api.utils.MiniGroup;
import com.mfr.taass.spring.transaction.api.utils.OutputTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author luca
 */
public interface TransactionService {
    
    
//    public Page<Transaction> findAll(TransactionListRequest request, Pageable pageable) {
//        
//        Page<Transaction> transactionPage = transactionRepository.findAll(transactionListSpecification.getFilter(request), pageable);
//        return transactionPage;//transactionPage.map(transactionMapper::map);
//    }
    public MiniAccount buildFromAccount(Account a);
    
    public MiniGroup buildFromGroup(Groups g);
    
   
    public OutputTransaction buildFromTransaction(Transaction t);
    
}
