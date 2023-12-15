/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.controllers.dto;

import java.util.Date;

/**
 *
 * @author luca
 */
class GoalDto {
  
    private Long id;

    private String name;

   
    private Date deadline;

   
    private Long amount;

   
    private AccountDto account;

  
    private GroupsDto groups;
}
