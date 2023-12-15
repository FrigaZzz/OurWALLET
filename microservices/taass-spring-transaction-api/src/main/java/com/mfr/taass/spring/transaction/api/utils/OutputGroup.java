/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.utils;

import com.mfr.taass.spring.transaction.api.entities.Account;
import com.mfr.taass.spring.transaction.api.entities.Groups;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author matteo
 */
public class OutputGroup {

    private Long id;
    private String name;
    private List<MiniAccount> accounts;
    private Boolean isFamilyGroup;

    public OutputGroup(Long id, String name, List<Account> accounts, Boolean isFamilyGroup) {
        this.id = id;
        this.name = name;
        this.isFamilyGroup = isFamilyGroup;
        this.accounts = new ArrayList<>();
        for (Account account : accounts) {
            this.accounts.add(buildFromAccount(account));
        }
    }
    
    public OutputGroup(Long id, String name, Account account, Boolean isFamilyGroup) {
        this.id = id;
        this.name = name;
        this.isFamilyGroup = isFamilyGroup;
        this.accounts = new ArrayList<>();
            this.accounts.add(buildFromAccount(account));

    }

    private OutputGroup(Long id, String name, Boolean isFamilyGroup) {
        this.id = id;
        this.name = name;
        this.isFamilyGroup = isFamilyGroup;
        
    }

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

    public List<MiniAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<MiniAccount> accounts) {
        this.accounts = accounts;
    }

    public Boolean getIsFamilyGroup() {
        return isFamilyGroup;
    }

    public void setIsFamilyGroup(Boolean isFamilyGroup) {
        this.isFamilyGroup = isFamilyGroup;
    }

    public static List<OutputGroup> getFromCommonFundGroups(List<Groups> groups) {
        ArrayList<OutputGroup> returnGrouP = new ArrayList<>();
        for (Groups group : groups) {
            OutputGroup a;
            if (group.getAccount() != null) {
                a = new OutputGroup(group.getId(), group.getName(), group.getAccount(), Boolean.FALSE);
            } else {
                a = new OutputGroup(group.getId(), group.getName(), Boolean.FALSE);
            }
            returnGrouP.add(a);
        }

        return returnGrouP;

    }
    
    private static MiniAccount buildFromAccount(Account account){
        MiniAccount in=new MiniAccount();
            if(account.getGoal()!=null)
                in.setGoal(account.getGoal().getId());
            else 
                in.setGoal(-1);
            in.setId(account.getId());
            in.setIsEasyPay(account.isIsEasyPay());
            in.setName(account.getName());
            if(account.getUser()!=null)
                in.setUser(account.getUser().getId());
            else                 
                in.setUser(-1);
            return in;
    }

}
