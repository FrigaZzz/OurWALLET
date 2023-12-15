/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.checks;

import com.mfr.taass.spring.transaction.api.exceptions.InvalidDeleteTransactionCorrectnessParametersException;
import com.mfr.taass.spring.transaction.api.exceptions.InvalidModifyTransactionCorrectnessParametersException;
import com.mfr.taass.spring.transaction.api.entities.Account;
import com.mfr.taass.spring.transaction.api.entities.Budget;
import com.mfr.taass.spring.transaction.api.entities.Category;
import com.mfr.taass.spring.transaction.api.entities.Groups;
import com.mfr.taass.spring.transaction.api.entities.Transaction;
import com.mfr.taass.spring.transaction.api.utils.InputTransaction;
import com.mfr.taass.spring.transaction.api.entities.User;
import com.mfr.taass.spring.transaction.api.exceptions.ForbiddenAccessToResourceException;
import com.mfr.taass.spring.transaction.api.exceptions.InvalidAddTransactionCorrectnessParametersException;
import com.mfr.taass.spring.transaction.api.exceptions.InvalidGenericTransactionException;
import com.mfr.taass.spring.transaction.api.exceptions.InvalidJWTTokenException;
import com.mfr.taass.spring.transaction.api.exceptions.ResourceNotFoundException;
import com.mfr.taass.spring.transaction.api.repos.AccountRepository;
import com.mfr.taass.spring.transaction.api.repos.CategoryRepository;
import com.mfr.taass.spring.transaction.api.repos.GroupsRepository;

import com.mfr.taass.spring.transaction.api.repos.TransactionRepository;
import com.mfr.taass.spring.transaction.api.repos.UserRepository;
import com.mfr.taass.spring.transaction.api.utils.CriteriaParser;
import com.mfr.taass.spring.transaction.api.utils.GenericSpecificationsBuilder;
import com.mfr.taass.spring.transaction.api.utils.MiniAccount;
import com.mfr.taass.spring.transaction.api.utils.InputGroup;
import com.mfr.taass.spring.transaction.api.utils.JwtTokenUtil;
import com.mfr.taass.spring.transaction.api.utils.JwtUser;
import com.mfr.taass.spring.transaction.api.utils.TransactionSpecification;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 *
 * @author matteo
 */
@Service
public class Correctness {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GroupsRepository groupsRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public Correctness() {
    }

    public JwtUser checkJWT(String jwt) throws InvalidJWTTokenException {
        JwtUser user = jwtTokenUtil.getUserDetails(jwt);
        if (user == null) {
            throw new InvalidJWTTokenException();
        } else {
            return user;
        }
    }

    public Account addAccount(MiniAccount account, JwtUser user) throws InvalidAddTransactionCorrectnessParametersException {
        User userDB = userRepo.findByUsername(user.getUsername());
        Account a = new Account();
        a.setIsEasyPay(account.getIsEasyPay());
        a.setUser(userDB);
        a.setName(account.getName());
        //userDB.addAccount(account);

        return a;

    }

    public Transaction addTransaction(InputTransaction transaction, JwtUser user) throws InvalidAddTransactionCorrectnessParametersException, ForbiddenAccessToResourceException, ResourceNotFoundException {
        User userDB = userRepo.findByUsername(user.getUsername());
        Groups realGroup = getTypeOfGroup(userDB, transaction);
        if (realGroup == null) {
            InputGroup i = null;
            transaction.setGroupSenderID(-1L);
            throw new InvalidAddTransactionCorrectnessParametersException(transaction);
        }

        Transaction t = null;

        Account accountSender = this.checkGetAccountTransaction(transaction.getAccountSenderID(), user);
        Account accountReceived = null;
        if (transaction.getAccountReceivedID() != null && transaction.getAccountReceivedID() != -1) {
            accountReceived = this.checkGetAccountTransaction(transaction.getAccountReceivedID(), user);
        }

        try {
            t = fillerTransaction(new Transaction(), accountSender, accountReceived, transaction, realGroup);

            if (checkBudget(t, realGroup, -1)) {
                return t;
            } else {
                throw new InvalidAddTransactionCorrectnessParametersException(null);

            }
        } catch (InvalidGenericTransactionException ex) {
            throw new InvalidAddTransactionCorrectnessParametersException(ex.getProvidedTransaction());
        }

    }

    private Transaction fillerTransaction(Transaction toAdd, Account sender, Account receiver, InputTransaction transaction, Groups realGroup) throws InvalidGenericTransactionException {
        toAdd.setAccount(sender);
        toAdd.setAmount(transaction.getAmount());
        toAdd.setGroups(realGroup);
        toAdd.setDate(transaction.getDate());
        toAdd.setTransferTargetAccount(receiver);

        toAdd.setDescription(transaction.getDescription());
        if (transaction.getCategoryID() != null) {
            Optional<Category> category = categoryRepository.findById(transaction.getCategoryID());
            if (category.isPresent()) {
                toAdd.setCategory(category.get());
            } else {
                transaction.setCategoryID(-1L);
                throw new InvalidGenericTransactionException(transaction);
            }
        }
        return toAdd;
    }

    private Groups getTypeOfGroup(User userDB, InputTransaction transaction) {
        Optional<Groups> group = groupsRepository.findById(transaction.getGroupSenderID());
        if (group.isPresent()) {

            if (userDB.getCommonFundGroups().contains(group.get())) {
                int indexDB = userDB.getCommonFundGroups().indexOf(group.get());
                return userDB.getCommonFundGroups().get(indexDB);
            }

            if (userDB.getFamilyGroup().equals(group.get())) {
                return userDB.getFamilyGroup();
            }
        }

        return null;

    }

    public Transaction modifyTransaction(InputTransaction transaction, JwtUser user) throws InvalidModifyTransactionCorrectnessParametersException, ForbiddenAccessToResourceException, ResourceNotFoundException {
        User userDB = userRepo.findByUsername(user.getUsername());
        Groups realGroup = getTypeOfGroup(userDB, transaction);

        if (realGroup != null) {

            Account account = this.checkGetAccountTransaction(transaction.getAccountSenderID(), user);
            if (account != null) {
                Optional<Transaction> realTransaction = transactionRepository.findById(transaction.getId());
                Account receiver = null;
                if (transaction.getAccountReceivedID() != -1) {
                    receiver = this.checkGetAccountTransaction(transaction.getAccountReceivedID(), user);
                }

                if (realTransaction.isPresent()) {
                    Transaction t = null;

                    try {
                        t = fillerTransaction(realTransaction.get(), account, receiver, transaction, realGroup);

                        if (checkBudget(t, realGroup, t.getId())) {
                            return t;
                        } else {
                            throw new InvalidModifyTransactionCorrectnessParametersException(null);

                        }

                    } catch (InvalidGenericTransactionException ex) {
                        throw new InvalidModifyTransactionCorrectnessParametersException(ex.getProvidedTransaction());
                    }

                } else {
                    transaction.setId(null);
                    throw new InvalidModifyTransactionCorrectnessParametersException(transaction);
                }

            } else {
                MiniAccount i = null;
                transaction.setAccountSenderID(-1L);

                throw new InvalidModifyTransactionCorrectnessParametersException(transaction);

            }

        } else {
            InputGroup i = null;
            transaction.setGroupSenderID(-1L);
            throw new InvalidModifyTransactionCorrectnessParametersException(transaction);

        }

    }

    public Transaction deleteTransaction(Long id, JwtUser user) throws InvalidDeleteTransactionCorrectnessParametersException {
        User userDB = userRepo.findByUsername(user.getUsername());
        Optional<Transaction> realTransaction = transactionRepository.findById(id);

        if (realTransaction.isPresent()) {
            Groups group = realTransaction.get().getGroups();
            if (!userDB.getCommonFundGroups().contains(group) && !userDB.getFamilyGroup().equals(group)) {
                realTransaction.get().setGroups(null);
                throw new InvalidDeleteTransactionCorrectnessParametersException(realTransaction.get());
            } else {
                return realTransaction.get();
            }
        } else {
            throw new InvalidDeleteTransactionCorrectnessParametersException(null);
        }

    }

    public Account deleteAccount(Long id, JwtUser user) throws InvalidDeleteTransactionCorrectnessParametersException {
        User userDB = userRepo.findByUsername(user.getUsername());
        Optional<Account> account = accountRepository.findById(id);

        if (account.isPresent()) {
            List<Transaction> tr = transactionRepository.findByAccountOrTransferTargetAccount(account.get(), account.get());
            tr.forEach(t -> transactionRepository.delete(t));

        }
        return account.get();
    }

    /**
     * *
     * Verifica che l'account sia accessibile all'utente cioè se: - è
     * direttamente linkato all'utente - l'account è di un GOAL creato in un
     * gruppo in cui partecipa l'utente
     */
    public Account checkGetAccountTransaction(Long accountId, JwtUser user) throws ForbiddenAccessToResourceException, ResourceNotFoundException {
        Optional<Account> optAccount = accountRepository.findById(accountId);

        if (!optAccount.isPresent()) {
            throw new ResourceNotFoundException();
        }
        Account account = optAccount.get();

        if (account.getGoal() != null && account.getUser() != null) {
            throw new ForbiddenAccessToResourceException();
        }
        //allora è un account GOAL
        if (account.getUser() != null) {
            if (account.getUser().getId().equals(user.getId())) {
                return account;
            }
        } else {

            Groups g = groupsRepository.findByAccount(account);

            if (g != null && g.getUsers() != null && g.getUsers().stream().anyMatch(u -> u.getId().equals(user.getId()))) {
                return account;
            }
            User fullUser = userRepo.findById(user.getId()).get();
            if (fullUser.getFamilyGroup().getFamilyMembers().stream().anyMatch(m -> m.getAccounts().contains(account))) {
                //throw new ForbiddenAccessToResourceException();
            } // verifico non sia nel family group
            else if (fullUser.getFamilyGroup().getGoals().stream().anyMatch(gl -> gl.getAccount().getId() == account.getId())) {

                return account;
            } // verifico non sia nel common funds group
            else if (fullUser.getCommonFundGroups().stream().anyMatch(
                    gr -> {
                        return gr.getGoals().stream().anyMatch(
                                gl
                                -> gl.getAccount().getId() == account.getId());
                    }
            )) {
                return account;

            } else {
                throw new ForbiddenAccessToResourceException();
            }

        }
        return account;

    }

    private boolean checkBudget(Transaction t, Groups group, long idTrans) {
        
        Boolean find = false;
        Budget found = null;
        for (Budget b : group.getBudgets()) {
            if (b.getCategory().equals(t.getCategory())) {
                find = true;
                found = b;
            }
        }

        if (find) {
           
        Date date = new Date();
            int y = date.getYear();
            int m = date.getMonth();


            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int day=cal.get(Calendar.DAY_OF_MONTH);


            int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            int remainingDays = daysInMonth - cal.get(Calendar.DAY_OF_MONTH);

        
            Date firstDay = new Date(y, m , 1);
            Date lastDay = new Date(y, m, daysInMonth);

            long f = firstDay.getTime();
            long l = lastDay.getTime() + 3600 * 1000 * 24 - 1;

            String query = "groupsID:" + group.getId() + " and date>" + f + " and date<" + l + " and categoryID:" + t.getCategory().getId();
            Specification<Transaction> spec = resolveSpecificationFromInfixExpr(query);
            List<Transaction> tr = transactionRepository.findAll(spec);

            long amount = 0;
            int totalEarnings = 0;
            int totalExpanses = 0;
            for (Transaction t1 : tr) {
                if (!t1.getId().equals(idTrans)&&t1.getTransferTargetAccount()==null) {
                    
                    amount = amount + Math.abs(t1.getAmount());
                }
            }

            if (found.getBudgetAmount() > amount + Math.abs(t.getAmount())) {
                return true;
            } else {
                return false;
            }

        } else {
            return true;
        }
    }

    protected Specification<Transaction> resolveSpecificationFromInfixExpr(String searchParameters) {
        CriteriaParser parser = new CriteriaParser();
        GenericSpecificationsBuilder<Transaction> specBuilder = new GenericSpecificationsBuilder<>();
        return specBuilder.build(parser.parse(searchParameters), TransactionSpecification::new);
    }

}
