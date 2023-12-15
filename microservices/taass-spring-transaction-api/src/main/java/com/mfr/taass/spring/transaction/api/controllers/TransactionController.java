package com.mfr.taass.spring.transaction.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Joiner;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mfr.taass.spring.transaction.api.beans.BaseResponse;
import com.mfr.taass.spring.transaction.api.beans.StatusMeta;
import com.mfr.taass.spring.transaction.api.checks.Correctness;
import com.mfr.taass.spring.transaction.api.checks.Presence;
import com.mfr.taass.spring.transaction.api.entities.Account;
import com.mfr.taass.spring.transaction.api.entities.Category;
import com.mfr.taass.spring.transaction.api.entities.Groups;
import com.mfr.taass.spring.transaction.api.entities.Transaction;
import com.mfr.taass.spring.transaction.api.entities.User;
import com.mfr.taass.spring.transaction.api.exceptions.ForbiddenAccessToResourceException;
import com.mfr.taass.spring.transaction.api.exceptions.InvalidAddTransactionCorrectnessParametersException;
import com.mfr.taass.spring.transaction.api.exceptions.InvalidAddTransactionParametersException;
import com.mfr.taass.spring.transaction.api.exceptions.InvalidAuthorizationHeaderException;
import com.mfr.taass.spring.transaction.api.exceptions.InvalidDeleteTransactionCorrectnessParametersException;
import com.mfr.taass.spring.transaction.api.exceptions.InvalidJWTTokenException;
import com.mfr.taass.spring.transaction.api.exceptions.InvalidModifyTransactionCorrectnessParametersException;
import com.mfr.taass.spring.transaction.api.exceptions.InvalidModifyTransactionParametersException;
import com.mfr.taass.spring.transaction.api.exceptions.ResourceNotFoundException;
import com.mfr.taass.spring.transaction.api.repos.AccountRepository;
import com.mfr.taass.spring.transaction.api.repos.CategoryRepository;
import com.mfr.taass.spring.transaction.api.repos.GroupsRepository;
import com.mfr.taass.spring.transaction.api.repos.TransactionRepository;
import com.mfr.taass.spring.transaction.api.repos.UserRepository;
import com.mfr.taass.spring.transaction.api.services.CategoryService;
import com.mfr.taass.spring.transaction.api.utils.CriteriaParser;
import com.mfr.taass.spring.transaction.api.utils.MiniAccount;
import com.mfr.taass.spring.transaction.api.utils.InputGroup;
import com.mfr.taass.spring.transaction.api.utils.JwtUser;
import com.mfr.taass.spring.transaction.api.utils.InputTransaction;
import com.mfr.taass.spring.transaction.api.utils.OutputTransaction;
import com.mfr.taass.spring.transaction.api.utils.SearchCriteria;
import com.mfr.taass.spring.transaction.api.utils.SearchFilter;
import com.mfr.taass.spring.transaction.api.utils.SearchOperation;
import com.mfr.taass.spring.transaction.api.utils.SpecSearchCriteria;
import com.mfr.taass.spring.transaction.api.utils.TransactionSpecification;
import com.mfr.taass.spring.transaction.api.utils.TransactionSpecificationsBuilder;
import javax.persistence.criteria.Join;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.criteria.JoinType;
import javax.validation.constraints.NotNull;
import jdk.nashorn.internal.objects.NativeArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.mfr.taass.spring.transaction.api.services.TransactionService;
import com.mfr.taass.spring.transaction.api.services.TransactionServiceImpl;
import com.mfr.taass.spring.transaction.api.utils.GenericSpecificationsBuilder;
import com.mfr.taass.spring.transaction.api.utils.MiniCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.Random;
import org.springframework.data.domain.PageImpl;

@Api(tags = {"transaction-controller"})
@RestController
public class TransactionController {

    @Autowired
    private Correctness correctness;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupsRepository groupRespository;
    @Autowired
    private CategoryRepository categoryRespository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "Create a new transaction/transfer.", notes = "The transaction will be added if you have the permissions and the transaction won't overfill the Budget for the specific category. Set accountSenderID if you want to create a transfer to another Ourwallet Account. ", tags = "transaction-management")
    @PostMapping("/transactions")
    public BaseResponse<StatusMeta, Object> addTransaction(@RequestHeader(value = "Authorization") String token, @RequestBody InputTransaction newTransaction) throws JsonProcessingException, InvalidAddTransactionParametersException, InvalidJWTTokenException, InvalidAddTransactionCorrectnessParametersException, InvalidAuthorizationHeaderException, ForbiddenAccessToResourceException, ResourceNotFoundException {

        JwtUser user = checkToken(token);

        Transaction t = correctness.addTransaction(newTransaction, user);

        transactionRepository.save(t);
        OutputTransaction output = this.transactionService.buildFromTransaction(t);
        return new BaseResponse<>(new StatusMeta(200), output);

    }

    @ApiOperation(value = "Modify a transaction/transfer.", notes = "The transaction will be modified if you have the permissions and the transaction not overfill the Budget for the specific category. You can change amount, targetAccount, category, description. ", tags = "transaction-management")
    @PatchMapping("/transactions/{id}")
    public BaseResponse<StatusMeta, Object> modifyTransaction(@RequestHeader(value = "Authorization") String token, @RequestBody InputTransaction modifiedTransaction, @PathVariable("id") Long id) throws JsonProcessingException, InvalidModifyTransactionParametersException, InvalidJWTTokenException, InvalidModifyTransactionCorrectnessParametersException, InvalidAuthorizationHeaderException, ForbiddenAccessToResourceException, ResourceNotFoundException {
        JwtUser user = checkToken(token);

        modifiedTransaction.setId(id);
        Presence.modifyTransaction(modifiedTransaction);
        Transaction mod = correctness.modifyTransaction(modifiedTransaction, user);

        transactionRepository.save(mod);
        return new BaseResponse<>(new StatusMeta(200));
    }

    @ApiOperation(value = "Delete a transaction/transfer.", notes = "The transaction will be deleted if you have the permissions.", tags = "transaction-management")
    @DeleteMapping("/transactions/{id}")
    public BaseResponse<StatusMeta, Object> deleteTransaction(@RequestHeader(value = "Authorization") String token, @PathVariable("id") Long id) throws JsonProcessingException, InvalidJWTTokenException, InvalidDeleteTransactionCorrectnessParametersException, InvalidAuthorizationHeaderException {
        JwtUser user = checkToken(token);
        Transaction toDelete = correctness.deleteTransaction(id, user);
        transactionRepository.delete(toDelete);
        return new BaseResponse<>(new StatusMeta(200));
    }

    @ApiOperation(value = "Delete an account.", notes = "The account will be deleted if you have the permissions.", tags = "account-management")
    @DeleteMapping("/accounts/{id}")
    public BaseResponse<StatusMeta, Object> deleteAccount(@RequestHeader(value = "Authorization") String token, @PathVariable("id") Long id) throws JsonProcessingException, InvalidJWTTokenException, InvalidDeleteTransactionCorrectnessParametersException, InvalidAuthorizationHeaderException, ForbiddenAccessToResourceException, ResourceNotFoundException {
        JwtUser user = checkToken(token);
        Account toDelete = correctness.deleteAccount(id, user);
        List<Transaction> toDelTransactions = transactionRepository.findByAccountOrTransferTargetAccount(toDelete, toDelete);
        toDelTransactions.forEach(t -> transactionRepository.delete(t));
        accountRepository.delete(toDelete);
        return new BaseResponse<>(new StatusMeta(200));
    }

    @ApiOperation(value = "Create a category.", notes = "The category will be created.", tags = "category-management")
    @PostMapping("/categories")
    public BaseResponse<StatusMeta, Object> addCategory(@RequestHeader(value = "Authorization") String token, @RequestBody MiniCategory newCategory) throws JsonProcessingException, InvalidAddTransactionParametersException, InvalidJWTTokenException, InvalidAddTransactionCorrectnessParametersException, InvalidAuthorizationHeaderException {

        this.categoryService.AddCategory(newCategory);

        return new BaseResponse<>(new StatusMeta(200), "ok");
    }

    @ApiOperation(value = "Get the details of a specified account.", notes = "If you have the permissions, you will get all the details of an account.", tags = "account-management")
    @GetMapping("/accounts/{id}")
    public BaseResponse<StatusMeta, Object> getAccount(@RequestHeader(value = "Authorization") String token, @PathVariable("id") Long id) throws InvalidJWTTokenException, InvalidAuthorizationHeaderException, ForbiddenAccessToResourceException, ResourceNotFoundException {
        JwtUser user = checkToken(token);
        Account account = correctness.checkGetAccountTransaction(id, user);
        MiniAccount out = new MiniAccount();
        if (account.getGoal() != null) {
            out.setGoal(account.getGoal().getId());
        } else {
            out.setGoal(-1);
        }

        out.setIsEasyPay(account.isIsEasyPay());
        out.setName(account.getName());
        if (account.getUser() != null) {
            out.setUser(user.getId());
        } else {
            out.setUser((-1));
        }
        out.setId(account.getId());

        return new BaseResponse<>(new StatusMeta(200), out);
    }

    @ApiOperation(value = "Get all categories.", notes = "Get all avaibles categories.", tags = "category-management")
    @GetMapping("/categories")
    public BaseResponse<StatusMeta, Object> getCategories(@RequestHeader(value = "Authorization") String token) throws InvalidJWTTokenException, InvalidAuthorizationHeaderException, ForbiddenAccessToResourceException, ResourceNotFoundException {
        JwtUser user = checkToken(token);
        List<Category> categories = this.categoryService.GetCategories();
        Map m = new HashMap<String, Object>();
        m.put("categories", categories);

        return new BaseResponse<>(new StatusMeta(200), m);
    }

    @DeleteMapping("/categories/{id}")
    public BaseResponse<StatusMeta, Object> deleteCategory(@RequestHeader(value = "Authorization") String token, @PathVariable("id") Long id) throws JsonProcessingException, InvalidJWTTokenException, InvalidDeleteTransactionCorrectnessParametersException, InvalidAuthorizationHeaderException, ForbiddenAccessToResourceException, ResourceNotFoundException {
        JwtUser user = checkToken(token);
        boolean toDelete = this.categoryService.deleteCategory(id);

        return new BaseResponse<>(new StatusMeta(200));
    }

    @ApiOperation(value = "Create an account.", notes = "The account will be created.", tags = "account-management")
    @PostMapping("/accounts")
    public BaseResponse<StatusMeta, Object> createAccount(@RequestHeader(value = "Authorization") String token, @RequestBody MiniAccount account) throws InvalidJWTTokenException, InvalidAuthorizationHeaderException, ForbiddenAccessToResourceException, ResourceNotFoundException, InvalidAddTransactionCorrectnessParametersException, InvalidAddTransactionParametersException {
        JwtUser user = checkToken(token);
        User userDB = userRepository.findByUsername(user.getUsername());
        Account a = new Account();
        a.setIsEasyPay(account.getIsEasyPay());
        a.setName(account.getName());
        Account ret = accountRepository.save(a);
        userDB.addAccount(ret);
        userRepository.save(userDB);
        account.setUser(userDB.getId());
        account.setId(ret.getId());

        return new BaseResponse<>(new StatusMeta(200), account);

    }

    /*  select *
        from transaction join group join account join account2 where ids are equal
        where filter is applied
        offset x
        limit  y
            
     */
    //aggiungere magari anche controlli che per alcuni campi non ci devono essere certe operazioni disponibili
    protected Specification<Transaction> resolveSpecificationFromFilterObject(SearchFilter searchParameters) {
        List<SpecSearchCriteria> criterias = new ArrayList<>();
        if (searchParameters == null || searchParameters.getClaims().isEmpty()) {

        } else {
            searchParameters.getClaims().forEach((key, search) -> {
                criterias.add(new SpecSearchCriteria(search.isOrPredicate(), search.getKey(), search.getOperation(), search.getValue()));
            });
        }
        TransactionSpecificationsBuilder builder = new TransactionSpecificationsBuilder(criterias);

        return builder.build();
    }

    protected Specification<Transaction> resolveSpecificationFromFilterObject(List<SearchCriteria> searchParameters) {
        List<SpecSearchCriteria> criterias = new ArrayList<>();

        if (searchParameters == null || searchParameters.isEmpty()) {
            searchParameters.forEach((search) -> {
                criterias.add(new SpecSearchCriteria(search.isOrPredicate(), search.getKey(), search.getOperation(), search.getValue()));
            });
        }
        TransactionSpecificationsBuilder builder = new TransactionSpecificationsBuilder(criterias);

        return builder.build();
    }

    /*
        cosa fare?
        Specification di questo tipo:
        ( ACCOUNT OR TARGET) (AND ... AND ...)
   
    
    
     */
    @ApiOperation(value = "Get the transactions.", notes = "Get a list of transaction, given some filters and the account id.", tags = "transaction-management")
    @GetMapping("/accounts/{id}/android_transactions")
    public BaseResponse<StatusMeta, Object> getAccountTransactions(@RequestHeader(value = "Authorization") String token, @PathVariable("id") Long id, String filters) throws InvalidJWTTokenException, InvalidAuthorizationHeaderException, ForbiddenAccessToResourceException, ResourceNotFoundException {
        JwtUser user = checkToken(token);
        Account out = correctness.checkGetAccountTransaction(id, user);

        if (filters != null && !filters.equals("")) {
            CriteriaParser parser = new CriteriaParser();
            // restituisce una lista che contiene in notazione POSTFISSA della query
            // id è diventato obbligatorio nell URL, se è presente anche in FILTERS errore! Evitiamo che sia inserito un altro valore
            // a cui l'utente non potrebbe accedere
            Deque<?> des = parser.parse(filters);
            Object[] keys = des.toArray();
            for (Object elem : keys) {
                if (elem instanceof SpecSearchCriteria) {
                    SpecSearchCriteria valid = (SpecSearchCriteria) elem;
                    if (valid.getKey().equals("accountID") || valid.getKey().equals("transferTargetAccountID")) {
                        throw new ForbiddenAccessToResourceException();
                    }
                }
            }
            // mettendo questo AND non dovremmo avere problemi con accesso a dati non "permessi"
            filters = "( accountID:" + id + " OR transferTargetAccountID:" + id + " ) AND  ( " + filters + " )";
        } else {
            filters = "( accountID:" + id + " OR transferTargetAccountID:" + id + " ) ";
        }

        Specification<Transaction> spec = resolveSpecificationFromInfixExpr(filters);

        List<Transaction> allTransactions = this.transactionRepository.findAll(spec);

        //List<Transaction> allTransactions=this.transactionRepository.findByAccountOrTransferTargetAccount(out,out);
        List<OutputTransaction> trc = new ArrayList<>();

        allTransactions.forEach(a -> {
            trc.add(this.transactionService.buildFromTransaction(a));
        });

        Map m = new HashMap<String, Object>();
        m.put("transactions", trc);
        m.put("total", trc.size());
        return new BaseResponse<>(new StatusMeta(200), m);

    }

    @ApiOperation(value = "Get the transactions, simplified and optimized for mobile applications.", notes = "Get a list of transaction, given some filters and the account id.", tags = "transaction-management")
    @GetMapping("/accounts/{id}/transactions")
    public BaseResponse<StatusMeta, Object> getAccountTransactions(@RequestHeader(value = "Authorization") String token, String orderBy, String direction, Integer pageIndex, Integer pageSize, String filters, @PathVariable("id") Long id) throws InvalidJWTTokenException, InvalidAuthorizationHeaderException, ForbiddenAccessToResourceException, ResourceNotFoundException {
        JwtUser user = checkToken(token);
        Account check = correctness.checkGetAccountTransaction(id, user);

        if (filters != null && !filters.equals("")) {
            CriteriaParser parser = new CriteriaParser();
            // restituisce una lista che contiene in notazione POSTFISSA della query
            // id è diventato obbligatorio nell URL, se è presente anche in FILTERS errore! Evitiamo che sia inserito un altro valore
            // a cui l'utente non potrebbe accedere
            Deque<?> des = parser.parse(filters);
            Object[] keys = des.toArray();
            for (Object elem : keys) {
                if (elem instanceof SpecSearchCriteria) {
                    SpecSearchCriteria valid = (SpecSearchCriteria) elem;
                    if (valid.getKey().equals("accountID") || valid.getKey().equals("transferTargetAccountID")) {
                        throw new ForbiddenAccessToResourceException();
                    }
                }
            }
            // mettendo questo AND non dovremmo avere problemi con accesso a dati non "permessi"
            filters = "( accountID:" + id + " OR transferTargetAccountID:" + id + " ) AND  ( " + filters + " )";
        } else {
            filters = "( accountID:" + id + " OR transferTargetAccountID:" + id + " ) ";
        }

        Specification<Transaction> spec = resolveSpecificationFromInfixExpr(filters);

        Direction dir = Direction.ASC;
        if (direction.toLowerCase().equals("asc")) {
            dir = Direction.DESC;
        }
        Sort sort = Sort.by(dir, orderBy);
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        List<Transaction> transactions = this.transactionRepository.findAll(spec);
        //versione 1
        Page<Transaction> out = this.transactionRepository.findAll(spec, pageable);

        //Page<Transaction> out = new PageImpl<Transaction>(transactions,pageable, transactions.size()); 
        int totalEarnings = 0;
        int totalExpanses = 0;
        int earnings = 0;
        int expanses = 0;

        /**
         * *
         * T ( FROM , TO , AMOUNT, TYPE )
         *
         * IN =>
         *
         *
         *
         * OUT =>
         *
         *
         */
        for (Transaction t : transactions) {
            if (t.getAccount().getId() == id) {

                if (t.getAmount() < 0) {
                    totalExpanses += Math.abs(t.getAmount());
                } else {
                    totalEarnings += Math.abs(t.getAmount());
                }
            } else {
                if (t.getAmount() > 0) {
                    totalExpanses += Math.abs(t.getAmount());
                } else {
                    totalEarnings += Math.abs(t.getAmount());
                }
            }

        }

        List<OutputTransaction> trc = new ArrayList<>();
        out.getContent().forEach(a -> {
            trc.add(this.transactionService.buildFromTransaction(a));
        });
        long totalTransactions = out.getTotalElements();

        Map m = new HashMap<String, Object>();
        m.put("transactions", trc);
        m.put("total", totalTransactions);
        m.put("totalEarnings", totalEarnings);
        m.put("totalExpanses", totalExpanses);

        //m.put("fil", (spec));
        //m.put("postfix query", des);
        return new BaseResponse<>(new StatusMeta(200), m);
    }

    /*
        39 39 25
        39 39 35
        40 39 25
    
     */
    protected Specification<Transaction> resolveSpecification(String searchParameters) {
        TransactionSpecificationsBuilder builder = new TransactionSpecificationsBuilder();
        String operationSetExper = Joiner.on("|")
                .join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile(
                "(\\p{Punct}?)(\\w+?)("
                + operationSetExper
                + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(searchParameters + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                    matcher.group(5), matcher.group(4), matcher.group(6));
        }

        return builder.build();
    }

    protected Specification<Transaction> resolveSpecificationFromInfixExpr(String searchParameters) {
        CriteriaParser parser = new CriteriaParser();
        GenericSpecificationsBuilder<Transaction> specBuilder = new GenericSpecificationsBuilder<>();
        return specBuilder.build(parser.parse(searchParameters), TransactionSpecification::new);
    }

    private JwtUser checkToken(String token) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException {
        if (!token.startsWith("Bearer ")) {
            throw new InvalidAuthorizationHeaderException();
        }
        token = token.substring(7, token.length());
        JwtUser user = correctness.checkJWT(token);
        return user;
    }

}
