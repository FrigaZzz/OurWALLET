package com.mfr.taass.spring.transaction.api.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * @author matteo
 */
@Entity
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    @Basic
    @Column(nullable = false)
    private String name;

    @OneToOne
    private Category SuperCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getSuperCategory() {
        return SuperCategory;
    }

    public void setSuperCategory(Category SuperCategory) {
        this.SuperCategory = SuperCategory;
    }

}