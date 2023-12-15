package com.mfr.taass.spring.stats.api.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author matteo
 */
@Entity
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    @Basic
    private String description;

     
    @NotNull
    private Long date;

   
    @Basic
    @Column(nullable = false)
    @NotNull
    private Long amount;

    
    
    @ManyToOne
    @NotNull
    private Groups groups;

    @ManyToOne
    private Account transferTargetAccount;

    @ManyToOne
    @NotNull
    private Account account;

   
    @ManyToOne
    private Category category;

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

    public Groups getGroups() {
        return groups;
    }

    public void setGroups(Groups groups) {
        this.groups = groups;
    }

    public Account getTransferTargetAccount() {
        return transferTargetAccount;
    }

    public void setTransferTargetAccount(Account transferTargetAccount) {
        this.transferTargetAccount = transferTargetAccount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
    
    
     

}