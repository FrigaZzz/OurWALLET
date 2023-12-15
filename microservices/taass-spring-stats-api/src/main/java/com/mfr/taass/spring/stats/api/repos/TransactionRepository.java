package com.mfr.taass.spring.stats.api.repos;

import com.mfr.taass.spring.stats.api.entities.Account;
import com.mfr.taass.spring.stats.api.entities.Category;
import com.mfr.taass.spring.stats.api.entities.Groups;
import com.mfr.taass.spring.stats.api.entities.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 *
 * @author matteo
 */

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
   
    public List<Transaction> findByAccountOrTransferTargetAccount(Account account,Account transfer_target_account);
    public List<Transaction> findByGroupsAndDateBetween(Groups groups, Long start, Long end);
    public List<Transaction> findByGroups(Groups groups);
    public List<Transaction> findByGroupsAndCategoryAndTransferTargetAccountAndAmountLessThanAndDateBetween(Groups groups, Category cat, Account transfer_target_account, Long maxAmount, Long start, Long end);

    //public Page<Transaction> findAll(Specification spec, Pageable pageable);
    
    
    
    /*public List<Transaction> findTransactions(String accountID, String account_name, String account_transfer_name, 
                                              String       
                                                );
    */
    //public List<Transaction> findByAccountOrTransferTargetAccount(Account account, Account transfer_target_account);
  
    //public List<Transaction> findByAccountOrTransferTargetAccount(Account account, Account transfer_target_account, Pageable pageable);
    
}
