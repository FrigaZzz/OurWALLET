/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.utils;

import com.mfr.taass.spring.transaction.api.entities.Account;
import com.mfr.taass.spring.transaction.api.entities.Groups;

/**
 *
 * @author matteo
 */
public class InputTransaction2 {

    private Long id;

    private String description;

    private Long amount;

    private OutputGroup groupSenderID;

    private MiniAccount accountSenderID;

    private MiniAccount accountReceivedID;

    private Long categoryID;
    
    private Long date;

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public OutputGroup getGroupSenderID() {
        return groupSenderID;
    }

    public void setGroupSenderID(OutputGroup groupSenderID) {
        this.groupSenderID = groupSenderID;
    }

 
    
    public void setAccountSenderID(MiniAccount accountSenderID) {
        this.accountSenderID = accountSenderID;
    }

    public void setAccountSenderID(Account accountSenderID) {
       this.accountSenderID=setAccount(accountSenderID);
    }
    
    private MiniAccount setAccount(Account account) {
        MiniAccount out = new MiniAccount();
        if(account.getGoal()!=null)
            out.setGoal(account.getGoal().getId());
        out.setId(account.getId());
        out.setIsEasyPay(account.isIsEasyPay());
        out.setName(account.getName());
        if(account.getUser()!=null)
            out.setUser(account.getUser().getId());
        return out;
    }

    public MiniAccount getAccountSenderID() {
        return accountSenderID;
    }
    
     public MiniAccount getAccountReceivedID() {
        return accountReceivedID;
    }
 
    public void setAccountReceivedID(MiniAccount accountReceivedID) {
        this.accountReceivedID = accountReceivedID;
    }

    public void setAccountReceivedID(Account accountReceivedID) {
        this.accountReceivedID=setAccount(accountReceivedID);

    }

    public Long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

}
