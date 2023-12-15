/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.controllers.dto;

import java.util.List;

/**
 *
 * @author luca
 */
class AccountDto {
   
    private Long id;

   
    private String name;

    private Boolean isEasyPay;

   
    private GoalDto goal;

    private UserDto user;

    private List<TransactionDto> transactions;

    private List<TransactionDto> transfers;
}
