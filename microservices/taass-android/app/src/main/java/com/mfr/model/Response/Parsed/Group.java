/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.model.Response.Parsed;


import java.io.Serializable;
import java.util.List;

/**
 *
 * @author matteo
 */
public class Group implements Serializable {

    private Long id;
    private String name;
    private List<Account> accounts;
    private Boolean isFamilyGroup;

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

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Boolean getFamilyGroup() {
        return isFamilyGroup;
    }

    public void setFamilyGroup(Boolean familyGroup) {
        isFamilyGroup = familyGroup;
    }
}
