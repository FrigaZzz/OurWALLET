package com.mfr.taass.spring.group.api.repos;



import com.mfr.taass.spring.group.api.entities.Groups;
import com.mfr.taass.spring.group.api.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>{
    User findByUsername(String username);
    User findByEmail(String email);
    
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    List<User> findByfamilyGroup(Groups familyGroups);
}
