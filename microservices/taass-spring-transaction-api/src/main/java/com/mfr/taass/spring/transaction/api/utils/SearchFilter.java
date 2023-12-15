/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luca
 */
public class SearchFilter {
    Map<String,SearchCriteria> claims;

    public Map<String, SearchCriteria> getClaims() {
        return claims;
    }

    public void setClaims(Map<String, SearchCriteria> claims) {
        this.claims = claims;
    }

    public SearchFilter() {
        this.claims=new HashMap<>();
    }
    public SearchFilter(Map<String,SearchCriteria> claims) {
        this.claims=claims;
    }
    public SearchCriteria getField(String field){
        if(this.claims.containsKey(field)){
            return this.claims.get(field);
        }
        else return null;
    }
    
}
