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
public class InputTransaction {

    private Long id;

    private String description;

    private Long amount;

    private Long groupSenderID;

    private Long accountSenderID;

    private Long accountReceivedID;

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

    public Long getGroupSenderID() {
        return groupSenderID;
    }

    public void setGroupSenderID(Long groupSenderID) {
        this.groupSenderID = groupSenderID;
    }

    public Long getAccountSenderID() {
        return accountSenderID;
    }

    public void setAccountSenderID(Long accountSenderID) {
        this.accountSenderID = accountSenderID;
    }

    public Long getAccountReceivedID() {
        return accountReceivedID;
    }

    public void setAccountReceivedID(Long accountReceivedID) {
        this.accountReceivedID = accountReceivedID;
    }

    public Long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

   
}
