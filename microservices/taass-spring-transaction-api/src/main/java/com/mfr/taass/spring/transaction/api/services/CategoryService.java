/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.services;



import com.mfr.taass.spring.transaction.api.entities.Category;
import com.mfr.taass.spring.transaction.api.utils.MiniCategory;
import java.util.List;


/**
 *
 * @author luca
 */
public interface CategoryService {
    
    public void AddCategory(MiniCategory  c);
    public List<Category> GetCategories();
    public boolean deleteCategory(long id);
    
}
