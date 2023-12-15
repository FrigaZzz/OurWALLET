/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.stats.api.beans;

import com.mfr.taass.spring.stats.api.entities.Budget;

/**
 *
 * @author Luca
 */
public class OutputBudget {
    private String name;

    private long id;

    private long amount;

    public OutputBudget(String catName, long catId, long amount) {
        this.name = catName;
        this.id = catId;
        this.amount = amount;
    }
    
    public OutputBudget(Budget budget) {
        this.name = budget.getCategory().getName();
        this.id = budget.getCategory().getId();
        this.amount = budget.getBudgetAmount();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
