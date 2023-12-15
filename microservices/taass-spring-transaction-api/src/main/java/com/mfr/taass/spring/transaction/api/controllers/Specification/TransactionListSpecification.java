/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.controllers.Specification;

import com.mfr.taass.spring.transaction.api.controllers.dto.TransactionListRequest;
import com.mfr.taass.spring.transaction.api.entities.Account;
import com.mfr.taass.spring.transaction.api.entities.Category;
import com.mfr.taass.spring.transaction.api.entities.Groups;
import com.mfr.taass.spring.transaction.api.entities.Transaction;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import javax.persistence.criteria.CriteriaQuery;
import static org.springframework.data.jpa.domain.Specification.where;

/**
 *
 * @author luca
 */
@Component
public class TransactionListSpecification extends BaseSpecification<Transaction, TransactionListRequest> {
    
    @Override
    public Specification<Transaction> getFilter(TransactionListRequest request) {
        return (root, query, cb) -> {
            query.distinct(true); //Important because of the join in the addressAttribute specifications
            return where(
                    where(desciptionContains(request.search))
                            .or(amountContains(request.search))
                            
            )
                  .and(groupContains(request.search))
                  .and(accountContains(request.search))
                  .and(targetContains(request.search))
                  .and(categoryContains(request.search))
                  .toPredicate(root, query, cb);
        };
    }

    private Specification<Transaction> desciptionContains(String desc) {
        return transactionAttributeContains("description", desc);
    }

    private Specification<Transaction> amountContains(String lastName) {
        return transactionAttributeContains("amount", lastName);
    }

    private Specification<Transaction> transactionAttributeContains(String attribute, String value) {
        return (root, query, cb) -> {
            if (value == null) {
                return null;
            }

            return cb.like(
                    cb.lower(root.get(attribute)),
                    containsLowerCase(value)
            );
        };
    }

    private Specification<Transaction> groupContains(String search) {
        return groupAttributeContains("name", search);
    }

    private Specification<Transaction> accountContains(String search) {
        return accountAttributeContains("name", search,"account");
    }

    private Specification<Transaction> targetContains(String search) {
        return accountAttributeContains("name", search,"transferTargetAccount");
    }

    private Specification<Transaction> categoryContains(String search) {
        return categoryAttributeContains("name", search);
    }

    private Specification<Transaction> groupAttributeContains(String attribute, String value) {
        return (root, query, cb) -> {
            if (value == null) {
                return null;
            }

            Join<Transaction, Groups> group = root.join("groups", JoinType.INNER);

            return cb.like(
                    cb.lower(group.get(attribute)),
                    containsLowerCase(value)
            );
        };
    }

    private Specification<Transaction> accountAttributeContains(String attribute, String value,String accountType) {
        return (root, query, cb) -> {
            if (value == null) {
                return null;
            }
            Join<Transaction, Account> account = root.join(accountType, JoinType.INNER);

            return cb.like(
                    cb.lower(account.get(attribute)),
                    containsLowerCase(value)
            );
        };
    }

    private Specification<Transaction> categoryAttributeContains(String attribute, String value) {
        return (root, query, cb) -> {
            if (value == null) {
                return null;
            }

            Join<Transaction, Category> category = root.join("category", JoinType.INNER);

            return cb.like(
                    cb.lower(category.get(attribute)),
                    containsLowerCase(value)
            );
        };
    }

}
