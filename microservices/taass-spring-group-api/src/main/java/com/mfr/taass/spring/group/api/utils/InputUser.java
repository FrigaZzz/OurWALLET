/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.group.api.utils;

/**
 *
 * @author matteo
 */
public class InputUser {
    public String username;
    public String email;
    public Boolean isPayer;
    public Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsPayer() {
        return isPayer;
    }

    public void setIsPayer(Boolean isPayer) {
        this.isPayer = isPayer;
    }
    
    
    
    
}
