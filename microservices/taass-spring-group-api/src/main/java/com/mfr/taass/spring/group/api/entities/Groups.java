package com.mfr.taass.spring.group.api.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author matteo
 */
@Entity
@Table(name = "Groups")
public class Groups {

    @Id
    @GeneratedValue
    private Long id;

    @Basic
    private String name;

    @Basic
    @Column(nullable = false)
    private Boolean isFamilyGroup;

    @OneToOne
    private Account account;

    @OneToMany(mappedBy = "groups")
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "groups")
    private List<Goal> goals;

    @OneToMany(mappedBy = "groups")
    private List<Budget> budgets;

    @OneToMany(mappedBy = "familyGroup")
    private List<User> familyMembers;

    @ManyToMany(mappedBy = "commonFundGroups")
    private List<User> users;

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

    public Boolean isIsFamilyGroup() {
        return isFamilyGroup;
    }

    public void setIsFamilyGroup(Boolean isFamilyGroup) {
        this.isFamilyGroup = isFamilyGroup;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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
        transaction.setGroups(this);
    }

    public void removeTransaction(Transaction transaction) {
        getTransactions().remove(transaction);
        transaction.setGroups(null);
    }

    public List<Goal> getGoals() {
        if (goals == null) {
            goals = new ArrayList<>();
        }
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public void addGoal(Goal goal) {
        getGoals().add(goal);
        goal.setGroups(this);
    }

    public void removeGoal(Goal goal) {
        getGoals().remove(goal);
        goal.setGroups(null);
    }

    public List<Budget> getBudgets() {
        if (budgets == null) {
            budgets = new ArrayList<>();
        }
        return budgets;
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }

    public void addBudget(Budget budget) {
        getBudgets().add(budget);
        budget.setGroups(this);
    }

    public void removeBudget(Budget budget) {
        getBudgets().remove(budget);
        budget.setGroups(null);
    }

    public List<User> getFamilyMembers() {
        if (familyMembers == null) {
            familyMembers = new ArrayList<>();
        }
        return familyMembers;
    }

    public void setFamilyMembers(List<User> familyMembers) {
        this.familyMembers = familyMembers;
    }

    public void addFamilyMember(User familyMember) {
        getFamilyMembers().add(familyMember);
        familyMember.setFamilyGroup(this);
    }

    public void removeFamilyMember(User familyMember) {
        getFamilyMembers().remove(familyMember);
        familyMember.setFamilyGroup(null);
    }

    public List<User> getUsers() {
        if (users == null) {
            users = new ArrayList<>();
        }
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        getUsers().add(user);
    }

    public void removeUser(User user) {
        getUsers().remove(user);
    }

}