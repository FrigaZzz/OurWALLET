/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.group.api.utils;

import com.mfr.taass.spring.group.api.entities.Budget;
import com.mfr.taass.spring.group.api.entities.Category;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author matteo
 */
public class OutputBudget {

    
   
    
    private Category category;
    private Long amount;
    private Long groupID;

    public OutputBudget(Category category, Long amount, Long groupID) {
        this.category = category;
        this.amount = amount;
        this.groupID = groupID;
       
    }

    
    
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    

    

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

   
    public static List<OutputBudget> fromBudgets(List<Budget> budgets) {
        List<OutputBudget> budO = new ArrayList<>();
        for(Budget b : budgets){
            budO.add(new OutputBudget(b.getCategory(),b.getBudgetAmount(),b.getGroups().getId()));
        }
        return budO;
    }

    

}
