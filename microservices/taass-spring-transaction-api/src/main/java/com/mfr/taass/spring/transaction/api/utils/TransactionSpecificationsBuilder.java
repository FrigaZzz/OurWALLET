/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.utils;

import com.google.gson.Gson;
import com.mfr.taass.spring.transaction.api.entities.Transaction;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author luca
 */

public final class TransactionSpecificationsBuilder {

    private final List<SpecSearchCriteria> params;

    public TransactionSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public TransactionSpecificationsBuilder(List<SpecSearchCriteria> params) {
            this.params = params;
        }
    public final TransactionSpecificationsBuilder with(final String key, final String operation, final Object value, final String prefix, final String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public final TransactionSpecificationsBuilder with(final String orPredicate, final String key, final String operation, final Object value, final String prefix, final String suffix) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) { // the operation may be complex operation
                final boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH;
                }
            }
            params.add(new SpecSearchCriteria(orPredicate, key, op, value));
        }
        return this;
    }

    public Specification<Transaction> build() {
        if (params.size() == 0)
            return null;

        Specification<Transaction> result = new TransactionSpecification(params.get(0));
     
        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
              ? Specification.where(result).or(new TransactionSpecification(params.get(i))) 
              : Specification.where(result).and(new TransactionSpecification(params.get(i)));
        }
       
        return result;
    }

    public final TransactionSpecificationsBuilder with(TransactionSpecification spec) {
        params.add(spec.getCriteria());
        return this;
    }

    public final TransactionSpecificationsBuilder with(SpecSearchCriteria criteria) {
        params.add(criteria);
        return this;
    }
}