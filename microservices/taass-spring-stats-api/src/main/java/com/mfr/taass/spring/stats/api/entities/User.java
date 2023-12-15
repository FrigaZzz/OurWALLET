package com.mfr.taass.spring.stats.api.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author matteo
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Basic
    @Column(unique = true, nullable = false)
    private String email;

    @Basic
    private String password;

    @Basic
    @Column(unique = true, nullable = false)
    @NotNull
    private String username;

    @Basic
    @Column(nullable = false)
    @NotNull
    private Boolean payer;

    @ManyToOne
    @NotNull
    private Groups familyGroup;

    @OneToMany(mappedBy = "user")
    private List<Account> accounts;

    @ManyToMany
    private List<Groups> commonFundGroups;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean isPayer() {
        return payer;
    }

    public void setPayer(Boolean payer) {
        this.payer = payer;
    }

    public Groups getFamilyGroup() {
        return familyGroup;
    }

    public void setFamilyGroup(Groups familyGroup) {
        this.familyGroup = familyGroup;
    }

    public List<Account> getAccounts() {
        if (accounts == null) {
            accounts = new ArrayList<>();
        }
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        getAccounts().add(account);
        account.setUser(this);
    }

    public void removeAccount(Account account) {
        getAccounts().remove(account);
        account.setUser(null);
    }

    public List<Groups> getCommonFundGroups() {
        if (commonFundGroups == null) {
            commonFundGroups = new ArrayList<>();
        }
        return commonFundGroups;
    }

    public void setCommonFundGroups(List<Groups> commonFundGroups) {
        this.commonFundGroups = commonFundGroups;
    }

    public void addCommonFundGroup(Groups commonFundGroup) {
        getCommonFundGroups().add(commonFundGroup);
        commonFundGroup.getUsers().add(this);
    }

    public void removeCommonFundGroup(Groups commonFundGroup) {
        getCommonFundGroups().remove(commonFundGroup);
        commonFundGroup.getUsers().remove(this);
    }

}