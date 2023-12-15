/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.auth.api.repos;

import com.mfr.taass.spring.auth.api.entities.Groups;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author matteo
 */
public interface GroupsRepository extends CrudRepository<Groups, Long> {

}
