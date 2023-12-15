/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.auth.api.utils;

import com.mfr.taass.spring.auth.api.entities.Account;
import com.mfr.taass.spring.auth.api.entities.Groups;
import com.mfr.taass.spring.auth.api.entities.Transaction;

/**
 *
 * @author matteo
 */
public class InputTransaction {

    private Long id;

    private String description;

    private Long amount;

    private InputGroup groupSenderID;

    private InputAccount accountSenderID;

    private InputAccount accountReceivedID;

    private Long categoryID;

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

    public InputGroup getGroupSenderID() {
        return groupSenderID;
    }

    public void setGroupSenderID(InputGroup groupSenderID) {
        this.groupSenderID = groupSenderID;
    }

    public void setGroupSenderID(Groups groupSenderID) {
        this.groupSenderID.setGroupID(groupSenderID.getId());
        this.groupSenderID.setIsFamily(groupSenderID.isIsFamilyGroup());
        this.groupSenderID.setIsPayer(false);
        this.groupSenderID.setName(groupSenderID.getName());
        this.groupSenderID.setUserID(-1L);
    }
    
    public void setAccountSenderID(InputAccount accountSenderID) {
        this.accountSenderID = accountSenderID;
    }

    public void setAccountSenderID(Account accountSenderID) {
       setAccount(this.accountSenderID,accountSenderID);
    }
    
    private void setAccount(InputAccount out,Account account) {
        out = new InputAccount();
        out.setGoal(account.getGoal().getId());
        out.setId(account.getId());
        out.setIsEasyPay(account.isIsEasyPay());
        out.setName(account.getName());
        out.setUser(account.getUser().getId());
    }

    public InputAccount getAccountSenderID() {
        return accountSenderID;
    }
    
     public InputAccount getAccountReceivedID() {
        return accountReceivedID;
    }
 
    public void setAccountReceivedID(InputAccount accountReceivedID) {
        this.accountReceivedID = accountReceivedID;
    }

    public void setAccountReceivedID(Account accountReceivedID) {
       setAccount(this.accountReceivedID,accountReceivedID);
    }

    public Long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

}
