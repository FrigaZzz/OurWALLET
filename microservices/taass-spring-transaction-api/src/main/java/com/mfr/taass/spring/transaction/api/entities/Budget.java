package com.mfr.taass.spring.transaction.api.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author matteo
 */
@Entity
@IdClass(BudgetPK.class)
public class Budget {

    @Basic
    @Column(nullable = false)
    @NotNull
    private Long budgetAmount;

    @Id
    @ManyToOne
    private Groups groups;

    @Id
    @ManyToOne
    private Category category;

    public Long getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(Long budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public Groups getGroups() {
        return groups;
    }

    public void setGroups(Groups groups) {
        this.groups = groups;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}