package com.mfr.taass.spring.group.api.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * @author matteo
 */
@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @Basic
    @Column(nullable = false)
    private String name;

    @Basic
    @Column(nullable = false)
    @NotNull
    private Boolean isEasyPay;

    @OneToOne(mappedBy = "account")
    private Goal goal;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "transferTargetAccount")
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transfers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isIsEasyPay() {
        return isEasyPay;
    }

    public void setIsEasyPay(Boolean isEasyPay) {
        this.isEasyPay = isEasyPay;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Transaction> getTransactions() {
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {
        getTransactions().add(transaction);
        transaction.setTransferTargetAccount(this);
    }

    public void removeTransaction(Transaction transaction) {
        getTransactions().remove(transaction);
        transaction.setTransferTargetAccount(null);
    }

    public List<Transaction> getTransfers() {
        if (transfers == null) {
            transfers = new ArrayList<>();
        }
        return transfers;
    }

    public void setTransfers(List<Transaction> transfers) {
        this.transfers = transfers;
    }

    public void addTransfer(Transaction transfer) {
        getTransfers().add(transfer);
        transfer.setAccount(this);
    }

    public void removeTransfer(Transaction transfer) {
        getTransfers().remove(transfer);
        transfer.setAccount(null);
    }

}