/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.stats.api.checks;


import com.mfr.taass.spring.stats.api.exceptions.InvalidJWTTokenException;
import com.mfr.taass.spring.stats.api.repos.AccountRepository;
import com.mfr.taass.spring.stats.api.repos.CategoryRepository;
import com.mfr.taass.spring.stats.api.repos.GroupsRepository;

import com.mfr.taass.spring.stats.api.repos.TransactionRepository;
import com.mfr.taass.spring.stats.api.repos.UserRepository;
import com.mfr.taass.spring.stats.api.utils.JwtTokenUtil;
import com.mfr.taass.spring.stats.api.utils.JwtUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author matteo
 */
@Service
public class Correctness {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GroupsRepository groupsRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public Correctness() {
    }

    public JwtUser checkJWT(String jwt) throws InvalidJWTTokenException {
        JwtUser user = jwtTokenUtil.getUserDetails(jwt);
        if (user == null) {
            throw new InvalidJWTTokenException();
        } else {
            return user;
        }
    }

}