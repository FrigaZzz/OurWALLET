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
class UserDto {
   
    private Long id;

    private String email;

    private String password;

    private String username;

  
    private Boolean payer;

    private GroupsDto familyGroup;

    private List<AccountDto> accounts;

    private List<GroupsDto> commonFundGroups;
}
