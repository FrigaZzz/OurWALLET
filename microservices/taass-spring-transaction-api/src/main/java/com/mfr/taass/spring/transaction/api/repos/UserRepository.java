package com.mfr.taass.spring.transaction.api.repos;

import org.springframework.data.repository.CrudRepository;
import com.mfr.taass.spring.transaction.api.entities.User;
import org.springframework.stereotype.Component;

/**
 *
 * @author matteo
 */

public interface UserRepository extends CrudRepository<User, Long>{
    User findByUsername(String username);
    User findByEmail(String email);
    
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    
}
