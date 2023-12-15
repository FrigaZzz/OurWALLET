/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.stats.api.beans;

public class BudgetGraph {
    private long amount;
    private long spent;
    private OutputBudget budget;

    public BudgetGraph(long amount, long spent, OutputBudget budget) {
        this.amount = amount;
        this.spent = spent;
        this.budget = budget;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public OutputBudget getBudget() {
        return budget;
    }

    public void setBudget(OutputBudget budget) {
        this.budget = budget;
    }

    public long getSpent() {
        return spent;
    }

    public void setSpent(long spent) {
        this.spent = spent;
    }
}
