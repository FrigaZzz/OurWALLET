/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.services;


import com.mfr.taass.spring.transaction.api.entities.Account;
import com.mfr.taass.spring.transaction.api.entities.Category;
import com.mfr.taass.spring.transaction.api.entities.Groups;
import com.mfr.taass.spring.transaction.api.entities.Transaction;
import com.mfr.taass.spring.transaction.api.repos.CategoryRepository;
import com.mfr.taass.spring.transaction.api.repos.TransactionRepository;
import com.mfr.taass.spring.transaction.api.utils.MiniAccount;
import com.mfr.taass.spring.transaction.api.utils.MiniCategory;
import com.mfr.taass.spring.transaction.api.utils.MiniGroup;
import com.mfr.taass.spring.transaction.api.utils.OutputTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author luca
 */
@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;
     @Autowired
    private TransactionRepository transactionRepository;
    
    public CategoryServiceImpl(){
//        Category food=new Category();
//        Category tech=new Category();
//        Category deposit=new Category();
//        Category transfer=new Category();
//        Category transportation=new Category();
//
//        food.setName("Food");
//        tech.setName("Tech");
//        deposit.setName("Deposit");
//        transfer.setName("Transfer");
//        transportation.setName("Transportation");
//
//        this.categoryRepository.save(food);
//        this.categoryRepository.save(tech);
//        this.categoryRepository.save(deposit);
//        this.categoryRepository.save(transfer);
//        this.categoryRepository.save(transportation);

    }

    @Override
    public void AddCategory(MiniCategory c) {
     Category out=new Category();
     out.setName(c.getName());
     if(c.getSuperCategory()!=-1)
        out.setSuperCategory(categoryRepository.findById(c.getSuperCategory()).get());
     categoryRepository.save(out);
     
    }

    @Override
    public List<Category> GetCategories() {
        return (ArrayList<Category>) this.categoryRepository.findAll();
    }

    @Override
    public boolean deleteCategory(long id) {
        
       Optional<Category> c=this.categoryRepository.findById(id);
       if(!c.isPresent())
           return false;
       Category category=c.get();
       List<Transaction> toUpdateTransactions = transactionRepository.findByCategory(category);

       if(category.getSuperCategory()==null){

            toUpdateTransactions.forEach(t -> {
               transactionRepository.delete(t);
            });
            Iterable<Category> toDelete = categoryRepository.findAll();
            toDelete.forEach(t -> {
                         if(t.getSuperCategory()!=null&&t.getSuperCategory().equals(category))
                             categoryRepository.delete(t);
                      });
       }
       else{
           
           toUpdateTransactions.forEach(t -> {
               t.setCategory(category.getSuperCategory());
               transactionRepository.save(t);
        });
       }
      
       this.categoryRepository.delete(category);
       return true;



    }
    
   
    
    
}
