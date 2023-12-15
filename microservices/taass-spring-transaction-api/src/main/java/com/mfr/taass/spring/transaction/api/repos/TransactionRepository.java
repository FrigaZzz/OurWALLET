package com.mfr.taass.spring.transaction.api.repos;

import com.mfr.taass.spring.transaction.api.entities.Account;
import com.mfr.taass.spring.transaction.api.entities.Category;
import com.mfr.taass.spring.transaction.api.entities.Transaction;
import com.mfr.taass.spring.transaction.api.utils.TransactionSpecification;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author matteo
 */

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
   
    public List<Transaction> findByAccountOrTransferTargetAccount(Account account,Account transfer_target_account);
    
    public List<Transaction> findByCategory(Category category);

    //public Page<Transaction> findAll(Specification spec, Pageable pageable);
    
    
    
    /*public List<Transaction> findTransactions(String accountID, String account_name, String account_transfer_name, 
                                              String       
                                                );
    */
    //public List<Transaction> findByAccountOrTransferTargetAccount(Account account, Account transfer_target_account);
  
    //public List<Transaction> findByAccountOrTransferTargetAccount(Account account, Account transfer_target_account, Pageable pageable);
    
}
