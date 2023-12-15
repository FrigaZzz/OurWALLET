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
@Service
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    private TransactionRepository transactionRepository;
    
//    @Autowired
//    private TransactionMapper transactionMapper;
//    
//    @Autowired
//    private  TransactionListSpecification transactionListSpecification;
//    
    public TransactionServiceImpl(){}
    
//    public Page<Transaction> findAll(TransactionListRequest request, Pageable pageable) {
//        
//        Page<Transaction> transactionPage = transactionRepository.findAll(transactionListSpecification.getFilter(request), pageable);
//        return transactionPage;//transactionPage.map(transactionMapper::map);
//    }
    @Override
    public MiniAccount buildFromAccount(Account a){
        if(a==null)
          return null;
        MiniAccount out=new MiniAccount();
        out.setId(a.getId());
        out.setIsEasyPay(a.isIsEasyPay());
        out.setName(a.getName());
        if(a.getGoal()!=null)
            out.setGoal(a.getGoal().getId());
        if(a.getUser()!=null)
            out.setUser(a.getUser().getId());
        else out.setUser(-1);
        return out;
     
    }
    @Override
    public MiniGroup buildFromGroup(Groups g){
        if(g==null)
            return null;
        MiniGroup out=new MiniGroup();
        out.setId(g.getId());
        out.setIsFamilyGroup(g.isIsFamilyGroup());
        out.setName(g.getName());
        return out;

    }
    
    @Override   
    public OutputTransaction buildFromTransaction(Transaction t){
        OutputTransaction out=new OutputTransaction();
        out.setAccountReceived(buildFromAccount(t.getTransferTargetAccount()));
        out.setAccountSender(buildFromAccount(t.getAccount()));
        out.setAmount(t.getAmount());
        out.setId(t.getId());
        out.setGroupSender(this.buildFromGroup(t.getGroups()));
        out.setDate(t.getDate());
        out.setCategory(t.getCategory());
        out.setDescription(t.getDescription());
        return out;
    }
    
    
}
