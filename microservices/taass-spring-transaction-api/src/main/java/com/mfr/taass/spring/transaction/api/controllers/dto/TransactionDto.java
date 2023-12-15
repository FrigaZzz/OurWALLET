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

public class TransactionDto {
    private Long id;
    private String description;
    private Long amount;
    private GroupsDto groups;
    private AccountDto transferTargetAccount;
    private AccountDto account;
    private long date;

   
}