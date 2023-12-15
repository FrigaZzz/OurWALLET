
package com.mfr.taass.spring.auth.api.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum LoginResponse implements Serializable{
    ERROR_REGISTRATION_NUMBER_PARAMETERS(0, "Error: invalid number of parameters."), 
    ERROR_REGISTRATION_EMAIL_ALREADY_USED(1,"Error: email already used."), 
    ERROR_REGISTRATION_USERNAME_ALREADY_USED(2,"Error: username already taken."),
    ERROR_LOGIN_USERNAME_REQUIRED(3,"Error: username required"),
    ERROR_LOGIN_USERNAME_INCORRECT(4,"Error: username incorrect"),
    ERROR_LOGIN_PSW_INCORRECT(5,"Error: password incorrect"),
    OK_REGISTRATION(100,"TO CHANGE TO JWT"),
    OK_LOGIN(101,"TO CHANGE TO JWT")
    
    ;

    private int id;
    private String message;

    private LoginResponse(int id, String message) {
        this.id = id;
        this.message = message;
    }
   
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    

}