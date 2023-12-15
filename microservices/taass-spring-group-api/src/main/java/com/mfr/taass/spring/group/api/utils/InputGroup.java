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
public class InputGroup {

   
    private Long groupID;
    private Boolean isFamily;
    private Long userID;
    private Boolean isPayer;
    private String name;

    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    
    
    public Boolean getIsFamily() {
        return isFamily;
    }

    public void setIsFamily(Boolean isFamily) {
        this.isFamily = isFamily;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Boolean getIsPayer() {
        return isPayer;
    }

    public void setIsPayer(Boolean isPayer) {
        this.isPayer = isPayer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public static InputGroup getFilled() {
        InputGroup g = new InputGroup();
        g.setGroupID(Long.MIN_VALUE);
        g.isFamily = false;
        g.isPayer = false;
        g.name = "";
        g.userID = 1L;
        return g;
    }
    
    
    
    
}
