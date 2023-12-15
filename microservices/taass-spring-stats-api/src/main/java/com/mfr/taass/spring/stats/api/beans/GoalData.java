/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.stats.api.beans;

public class GoalData {
    private long amount;
    private long spent;
    private OutputGoal goal;

    public GoalData(long amount, long spent, OutputGoal goal) {
        this.amount = amount;
        this.spent = spent;
        this.goal = goal;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public OutputGoal getGoal() {
        return goal;
    }

    public void setGoal(OutputGoal goal) {
        this.goal = goal;
    }

    public long getSpent() {
        return spent;
    }

    public void setSpent(long spent) {
        this.spent = spent;
    }
}
