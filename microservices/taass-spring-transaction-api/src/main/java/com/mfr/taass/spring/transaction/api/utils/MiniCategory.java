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
public class MiniCategory {

    private String name;
    private long superCategory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSuperCategory() {
        return superCategory;
    }

    public void setSuperCategory(long superCategory) {
        this.superCategory = superCategory;
    }

   

}
