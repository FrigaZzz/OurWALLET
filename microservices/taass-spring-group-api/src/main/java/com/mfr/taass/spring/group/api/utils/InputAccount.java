/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.group.api.utils;

import com.mfr.taass.spring.group.api.entities.User;

/**
 *
 * @author matteo
 */
public class InputAccount {

    private long id;

    private Boolean isEasyPay;

    private String name;

    private long user;
    
    private long goal;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getGoal() {
        return goal;
    }

    public void setGoal(long goal) {
        this.goal = goal;
    }


    public Boolean getIsEasyPay() {
        return isEasyPay;
    }

    public void setIsEasyPay(Boolean isEasyPay) {
        this.isEasyPay = isEasyPay;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
