/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.repos;

import com.mfr.taass.spring.transaction.api.entities.Account;
import com.mfr.taass.spring.transaction.api.entities.Category;
import com.mfr.taass.spring.transaction.api.entities.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author matteo
 */
public interface CategoryRepository extends CrudRepository<Category, Long> {

}
