/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.utils;
import com.mfr.taass.spring.transaction.api.entities.Account;
import com.mfr.taass.spring.transaction.api.entities.Category;
import com.mfr.taass.spring.transaction.api.entities.Groups;
import com.mfr.taass.spring.transaction.api.entities.Transaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
/**
 *  Qeesta classe è di per sè generica e solo buildKey potrebbe essere l'estensione
 * @author luca
 */
public class TransactionSpecification implements Specification<Transaction> {
        private SpecSearchCriteria criteria;

	public TransactionSpecification(final SpecSearchCriteria criteria) {
		super();
		this.criteria = criteria;
	}

	public SpecSearchCriteria getCriteria() {
		return criteria;
	}

	@Override
	public Predicate toPredicate(final Root<Transaction> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
          
            String val="";
            Path out=null;
            String currentKey=criteria.getKey();
            
            Path e=this.buildKey(root, builder, currentKey);
           
            switch (criteria.getOperation()) {
		case EQUALITY:
			return builder.equal(e, criteria.getValue());
		case NEGATION:
			return builder.notEqual(e, criteria.getValue());
		case GREATER_THAN:
			return builder.greaterThan(e, criteria.getValue().toString());
		case LESS_THAN:
			return builder.lessThan(e, criteria.getValue().toString());
		case LIKE:
			return builder.like(e, criteria.getValue().toString());
		case STARTS_WITH:
			return builder.like(e, criteria.getValue() + "%");
		case ENDS_WITH:
			return builder.like(e, "%" + criteria.getValue());
		case CONTAINS:
			return builder.like(e, "%" + criteria.getValue() + "%");
		default:
                    throw new IndexOutOfBoundsException("sfsafsafs");
			//return null;
		}
	}
        
        
        // Specifico a transaction
        
        private Path buildKey(final Root<Transaction> root, final CriteriaBuilder builder, String currentKey){
           Join<Transaction,Groups> transactionGroups = root.join("groups",JoinType.LEFT);
            Join<Transaction,Account> transactionAccount = root.join("account",JoinType.LEFT);
            Join<Transaction, Category> transactionCategory = root.join("category", JoinType.LEFT);
            Join<Transaction,Account> transactionTarget = root.join("transferTargetAccount",JoinType.LEFT);
            
            String val="";
            Path out=null;
            // creo i join con le altre tabelle (inner join)
                switch(currentKey){ 
                case "groupsNAME":
                    out= (transactionGroups.get("name"));
                    val="gn";
                    break;
                case "groupsID":
                    out=  (transactionGroups.get("id"));
                    val="gi";
                    break;
                    
                case "accountID":
                        out=  (transactionAccount.get("id"));
                    val="ai";
                    break;
                case "accountNAME":
                        out=  (transactionAccount.get("name"));
                    val="an";
                    break;
                case "transferTargetAccountNAME":
                        out=  (transactionTarget.get("name"));
                    val="ta";
                    break;
                case "transferTargetAccountID":
                        out=  (transactionTarget.get("id"));
                    val="ti";
                    break;
                case "categoryNAME":
                        out=  (transactionCategory.get("name"));
                    val="cn";
                    break; 
                case "categoryID":
                        out=  (transactionCategory.get("id"));
                    val="cn";
                    break;
                default: // allora la key è un campo diretto di Transaction
                    out=  root.get(currentKey);
                    val="ddff";
            }
            return out;
        }

}