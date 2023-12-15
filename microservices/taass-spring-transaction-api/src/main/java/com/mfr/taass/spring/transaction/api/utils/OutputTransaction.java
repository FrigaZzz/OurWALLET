/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.utils;

import com.mfr.taass.spring.transaction.api.entities.Account;
import com.mfr.taass.spring.transaction.api.entities.Category;
import com.mfr.taass.spring.transaction.api.entities.Groups;

/**
 *
 * @author matteo
 */
public class OutputTransaction {

    public OutputTransaction() {
    }

    public OutputTransaction(Long id, String description, Long amount, MiniGroup groupSender, MiniAccount accountSender, MiniAccount accountReceived, Category category, Long date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.groupSender = groupSender;
        this.accountSender = accountSender;
        this.accountReceived = accountReceived;
        this.category = category;
        this.date = date;
    }

    private Long id;

    private String description;

    private Long amount;

    private MiniGroup groupSender;

    private MiniAccount accountSender;

    private MiniAccount accountReceived;

    private Category category;
    
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

    public MiniGroup getGroupSender() {
        return groupSender;
    }

    public void setGroupSender(MiniGroup groupSender) {
        this.groupSender = groupSender;
    }

    public MiniAccount getAccountSender() {
        return accountSender;
    }

    public void setAccountSender(MiniAccount accountSender) {
        this.accountSender = accountSender;
    }

    public MiniAccount getAccountReceived() {
        return accountReceived;
    }

    public void setAccountReceived(MiniAccount accountReceived) {
        this.accountReceived = accountReceived;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


   
}
