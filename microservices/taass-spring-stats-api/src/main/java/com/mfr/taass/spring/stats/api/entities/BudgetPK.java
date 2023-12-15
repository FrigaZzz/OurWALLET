package com.mfr.taass.spring.stats.api.entities;

import java.io.Serializable;
import java.util.Objects;

public class BudgetPK implements Serializable {

    private Long groups;

    private Long category;

    public BudgetPK() {
    }

    public BudgetPK(Long groups, Long category) {
        this.groups = groups;
        this.category = category;
    }

    public Long getGroups() {
        return groups;
    }

    public void setGroups(Long groups) {
        this.groups = groups;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Objects.equals(getClass(), obj.getClass())) {
            return false;
        }
        final BudgetPK other = (BudgetPK) obj;
        if (!java.util.Objects.equals(this.getGroups(), other.getGroups())) {
            return false;
        }
        if (!java.util.Objects.equals(this.getCategory(), other.getCategory())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.getGroups());
        hash = 31 * hash + Objects.hashCode(this.getCategory());
        return hash;
    }

    @Override
    public String toString() {
        return "BudgetPK{" + " groups=" + groups + ", category=" + category + '}';
    }

}