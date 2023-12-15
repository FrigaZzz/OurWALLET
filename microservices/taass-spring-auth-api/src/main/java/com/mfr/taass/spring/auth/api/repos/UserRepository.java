package com.mfr.taass.spring.auth.api.repos;

import org.springframework.data.repository.CrudRepository;
import com.mfr.taass.spring.auth.api.entities.User;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long>{
    User findByUsername(String username);
    User findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    
}
