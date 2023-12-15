/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.group.api.utils;

import com.mfr.taass.spring.group.api.entities.Goal;
import com.mfr.taass.spring.group.api.entities.Transaction;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.result.Output;

/**
 *
 * @author matteo
 */
public class OutputGoal {
    
    private String description;
    private String name;
    private Long amountReached;
    private Long amountRequested;
    private Long startDate;
    private Long deadLine;
    private Long group;
    private Long account;
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Long deadLine) {
        this.deadLine = deadLine;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAmountReached() {
        return amountReached;
    }

    public void setAmountReached(Long amountReached) {
        this.amountReached = amountReached;
    }

    public Long getAmountRequested() {
        return amountRequested;
    }

    public void setAmountRequested(Long amountRequested) {
        this.amountRequested = amountRequested;
    }

 

    public Long getGroup() {
        return group;
    }

    public void setGroup(Long group) {
        this.group = group;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public static List<OutputGoal> fromGoals(List<Goal> goals, Long groupId) {
        List<OutputGoal> list = new ArrayList<>();
        for (Goal goal : goals) {
            OutputGoal out = new OutputGoal();
            out.account = goal.getAccount().getId();
            out.amountRequested = goal.getAmount();
            out.amountReached = calculateAmountFromGoal(goal);
            out.setStartDate(goal.getStartDate());
            out.deadLine = goal.getDeadLine();
            out.description = goal.getDescription();
            out.name = goal.getName();
            out.id = goal.getId();
            out.group = groupId;
            list.add(out);
        }
        return list;

    }
    
  private static long calculateAmountFromGoal(Goal goal){
        long counter=0;
        long totalEarnings = 0;
        long totalExpanses = 0;
        
        for ( Transaction t :goal.getAccount().getTransactions()){
            
             if(t.getAccount().getId()==goal.getAccount().getId())
            {
                if(t.getAmount()<0)
                    totalExpanses+=Math.abs(t.getAmount());
                else 
                    totalEarnings+=Math.abs(t.getAmount());
            }
            else{
               if(t.getAmount()>0)
                    totalExpanses+=Math.abs(t.getAmount());
                else 
                    totalEarnings+=Math.abs(t.getAmount());
        }
        };
        
        return totalEarnings-totalExpanses;
    }
    
    

}
