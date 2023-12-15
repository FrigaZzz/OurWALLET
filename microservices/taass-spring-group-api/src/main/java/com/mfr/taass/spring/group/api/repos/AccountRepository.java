/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.group.api.repos;

import com.mfr.taass.spring.group.api.entities.Account;
import com.mfr.taass.spring.group.api.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author matteo
 */
public interface AccountRepository extends CrudRepository<Account, Long> {
    @Query("select a from Account a "
            + "join User u on a.user = u.id "+
            "join Groups g on u.familyGroup = g.id "
            + "where g.id = ?1")
    List<Account> findByGroupID(Long groupID);
}
