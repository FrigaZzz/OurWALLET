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
class GroupsDto {
    private Long id;

    private String name;

    private Boolean isFamilyGroup;

    private AccountDto account;

    private List<TransactionDto> transactions;

    private List<GoalDto> goals;

    private List<BudgetDto> budgets;

    private List<UserDto> familyMembers;

    private List<UserDto> users;
}
