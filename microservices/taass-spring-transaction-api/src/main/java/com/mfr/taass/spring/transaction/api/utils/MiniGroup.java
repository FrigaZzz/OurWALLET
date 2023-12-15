/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.utils;

/**
 *
 * @author matteo
 */
public class MiniGroup {

    private Long id;
    private String name;
    private Boolean isFamilyGroup;

    public MiniGroup() {
    }

    public MiniGroup(Long id, String name, Boolean isFamilyGroup) {
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

    public Boolean getIsFamilyGroup() {
        return isFamilyGroup;
    }

    public void setIsFamilyGroup(Boolean isFamilyGroup) {
        this.isFamilyGroup = isFamilyGroup;
    }

    

}
