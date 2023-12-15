package com.mfr.taass.spring.group.api.repos;

import com.mfr.taass.spring.group.api.entities.Transaction;
import org.springframework.data.repository.CrudRepository;



/**
 *
 * @author matteo
 */

public interface TransactionRepository extends CrudRepository<Transaction, Long>{
    
    
    
}
