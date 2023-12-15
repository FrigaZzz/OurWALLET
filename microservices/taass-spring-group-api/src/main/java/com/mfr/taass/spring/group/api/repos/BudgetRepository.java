package com.mfr.taass.spring.group.api.repos;



import com.mfr.taass.spring.group.api.entities.Account;
import com.mfr.taass.spring.group.api.entities.Budget;
import com.mfr.taass.spring.group.api.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BudgetRepository extends CrudRepository<Budget, Long>{
   @Query("select b from Budget b "
            + "left join Groups g on g.id = b.groups "+
            "left join Category c on b.category = c.id "
            + "where c.id = ?1 AND g.id = ?2")
    Optional<Budget> findByGroupCategory(Long categoryID, Long groupID);
}
