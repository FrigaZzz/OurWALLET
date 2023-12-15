package com.mfr.taass.spring.group.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mfr.taass.spring.group.api.beans.BaseResponse;
import com.mfr.taass.spring.group.api.beans.StatusMeta;
import com.mfr.taass.spring.group.api.checks.Correctness;
import com.mfr.taass.spring.group.api.exceptions.InvalidAddMemberGroupParametersException;
import com.mfr.taass.spring.group.api.exceptions.InvalidNameGroupParametersException;
import com.mfr.taass.spring.group.api.checks.Presence;
import com.mfr.taass.spring.group.api.entities.Account;
import com.mfr.taass.spring.group.api.entities.Budget;
import com.mfr.taass.spring.group.api.entities.Goal;
import com.mfr.taass.spring.group.api.entities.Groups;
import com.mfr.taass.spring.group.api.entities.User;
import com.mfr.taass.spring.group.api.exceptions.InvalidAddBudgetParametersCorrectness;
import com.mfr.taass.spring.group.api.exceptions.InvalidAddBudgetParametersException;
import com.mfr.taass.spring.group.api.exceptions.InvalidAddGoalParametersCorrectness;
import com.mfr.taass.spring.group.api.exceptions.InvalidAddGoalParametersException;
import com.mfr.taass.spring.group.api.exceptions.InvalidAddGroupParametersException;
import com.mfr.taass.spring.group.api.exceptions.InvalidAddMemberGroupCorrectnessException;
import com.mfr.taass.spring.group.api.exceptions.InvalidAuthorizationHeaderException;
import com.mfr.taass.spring.group.api.exceptions.InvalidDeleteBudgetParametersCorrectness;
import com.mfr.taass.spring.group.api.exceptions.InvalidDeleteGoalParametersCorrectness;
import com.mfr.taass.spring.group.api.exceptions.InvalidGroupAccountCorrectnessException;
import com.mfr.taass.spring.group.api.exceptions.InvalidGroupGoalCorrectnessException;
import com.mfr.taass.spring.group.api.exceptions.InvalidJWTTokenException;
import com.mfr.taass.spring.group.api.exceptions.InvalidModifyBudgetParametersCorrectness;
import com.mfr.taass.spring.group.api.exceptions.InvalidModifyGoalParametersCorrectness;
import com.mfr.taass.spring.group.api.exceptions.InvalidModifyGoalParametersException;
import com.mfr.taass.spring.group.api.exceptions.InvalidModifyMemberGroupCorrectnessException;
import com.mfr.taass.spring.group.api.exceptions.InvalidModifyMemberGroupParametersException;
import com.mfr.taass.spring.group.api.exceptions.InvalidNameGroupCorrectnessException;
import com.mfr.taass.spring.group.api.exceptions.InvalidRemoveMemberGroupCorrectnessException;
import com.mfr.taass.spring.group.api.exceptions.InvalidRemoveMemberGroupParametersException;
import com.mfr.taass.spring.group.api.repos.AccountRepository;
import com.mfr.taass.spring.group.api.repos.BudgetRepository;
import com.mfr.taass.spring.group.api.repos.GoalRepository;
import com.mfr.taass.spring.group.api.repos.GroupsRepository;
import com.mfr.taass.spring.group.api.repos.UserRepository;
import com.mfr.taass.spring.group.api.utils.InputBudget;
import com.mfr.taass.spring.group.api.utils.InputGoal;
import com.mfr.taass.spring.group.api.utils.InputGroup;
import com.mfr.taass.spring.group.api.utils.InputUser;
import com.mfr.taass.spring.group.api.utils.JwtUser;
import com.mfr.taass.spring.group.api.utils.OutputBudget;
import com.mfr.taass.spring.group.api.utils.OutputGoal;
import com.mfr.taass.spring.group.api.utils.OutputGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

@Api(tags={"group-controller"})
@RestController
public class GroupController {

    @Autowired
    private Correctness correctness;
    @Autowired
    private GroupsRepository groupsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private BudgetRepository budgetRepository;
    
    /*Group-Management*/
    @ApiOperation(value = "Create a new group, both a FamilyGroup or CommonFoundGroup.", notes = "If the token is correct, you will create a new Group. To specify if it is Family, set the variable isFamily accordingly.", tags="group-management")
    @PostMapping("/groups")
    public BaseResponse<StatusMeta, Object> addGroup(@RequestHeader(value = "Authorization") String token, @RequestBody InputGroup newGroup) throws JsonProcessingException, InvalidJWTTokenException, InvalidAuthorizationHeaderException, InvalidAddGroupParametersException {
        JwtUser userJWT = checkToken(token);
        newGroup.setUserID(userJWT.getId());

        Account newAccountCommon = new Account();
        newAccountCommon.setIsEasyPay(Boolean.FALSE);
        
        
        Groups g = new Groups();
        if (newGroup.getName() != null) {
            g.setName(newGroup.getName());
            newAccountCommon.setName(newGroup.getName());
        }else{
            g.setName("Cassa comune di "+userJWT.getUsername());
            newAccountCommon.setName("Account Cassa comune di "+userJWT.getUsername());
        }
        
        accountRepository.save(newAccountCommon);
        
        g.setIsFamilyGroup(Boolean.FALSE);
        g.setAccount(newAccountCommon);
        groupsRepository.save(g);
        User user = userRepository.findByEmail(userJWT.getEmail());
        user.addCommonFundGroup(g);
        userRepository.save(user);

        return new BaseResponse<>(new StatusMeta(200));

    }

    @ApiOperation(value = "Change the name of a group.", notes = "If the token is correct, and you have the permissions, you will change the name of the group.", tags="group-management")
    @PatchMapping("/group/{id}")
    public BaseResponse<StatusMeta, Object> modifyGroup(@RequestHeader(value = "Authorization") String token, @RequestBody InputGroup group, @PathVariable("id") Long id) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException, InvalidNameGroupParametersException, InvalidNameGroupCorrectnessException {
        JwtUser userJWT = checkToken(token);
        group.setGroupID(id);

        Presence.checkGroupName(group);

        Groups groupMod = correctness.checkNameGroup(group, userJWT);
        groupMod.setName(group.getName());
        groupsRepository.save(groupMod);

        return new BaseResponse<>(new StatusMeta(200));
    }

    @ApiOperation(value = "Get all users.", notes = "If the token is correct, you will get all the usres", tags="group-management")
    @GetMapping("/members")
    public BaseResponse<StatusMeta, Object> getMembers(@RequestHeader(value = "Authorization") String token) throws InvalidJWTTokenException, InvalidAuthorizationHeaderException {
        JwtUser user = checkToken(token);
        Iterable<User>allUsers=this.userRepository.findAll();
        List<JwtUser> users=new ArrayList<>();
        allUsers.forEach(u->users.add(new JwtUser(u.getId(),u.getUsername(),u.getEmail(),null)));
        return new BaseResponse<>(new StatusMeta(200), users);
    }
    
    @ApiOperation(value = "Get your groups with all the associated accounts.", notes = "If the token is correct, you will get all your groups and the associated accounts.", tags="group-management")
    /*Group-Getter*/
    @GetMapping("/groups")
    public BaseResponse<StatusMeta, Object> getPersonalGroupsWithAccounts(@RequestHeader(value = "Authorization") String token) throws JsonProcessingException, InvalidJWTTokenException, InvalidAuthorizationHeaderException, InvalidAddGroupParametersException {
        JwtUser userJWT = checkToken(token);
        User user = userRepository.findByEmail(userJWT.getEmail());
        List<OutputGroup> groups = new ArrayList<>();

        /*Family*/
        List<Account> accountsFamily = accountRepository.findByGroupID(user.getFamilyGroup().getId());
        groups.add(new OutputGroup(user.getFamilyGroup().getId(), user.getFamilyGroup().getName(), accountsFamily, Boolean.TRUE));

        /*Cassa Comune*/
        if (user.getCommonFundGroups() != null) {
            List<OutputGroup> a = OutputGroup.getFromCommonFundGroups(user.getCommonFundGroups());
            groups.addAll(a);
        }

        return new BaseResponse<>(new StatusMeta(200), groups);
    }

    @ApiOperation(value = "Get all accounts of a group.", notes = "If the token is correct, and you have the permissions, you will get all accounts associated with a group.", tags="group-management")
    @GetMapping("/groups/{id}/accounts")
    public BaseResponse<StatusMeta, Object> getAccountsOfGroup(@RequestHeader(value = "Authorization") String token, @PathVariable("id") Long id) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException, InvalidGroupAccountCorrectnessException {
        JwtUser userJWT = checkToken(token);
        User user = userRepository.findByEmail(userJWT.getEmail());
        Optional<Groups> groupO = groupsRepository.findById(id);
        List<OutputGroup> groups = new ArrayList<>();

        if (groupO.isPresent()) {
            Groups group = groupO.get();
            if (user.getFamilyGroup().equals(group)) {
                List<Account> accountsFamily = accountRepository.findByGroupID(user.getFamilyGroup().getId());
                groups.add(new OutputGroup(user.getFamilyGroup().getId(), user.getFamilyGroup().getName(), accountsFamily, Boolean.TRUE));
            } else {
                if (user.getCommonFundGroups().contains(group)) {
                    groups.addAll(OutputGroup.getFromCommonFundGroups(user.getCommonFundGroups()));
                } else {
                    group.setId(null);
                    throw new InvalidGroupAccountCorrectnessException(group);
                }
            }
        } else {
            throw new InvalidGroupAccountCorrectnessException(null);
        }

        return new BaseResponse<>(new StatusMeta(200), groups);
    }

    /*Member-Management*/
    @ApiOperation(value = "Add a member to a group.", notes = "If the token is correct, and you have the permissions, you will add the user to the group. If the group is Family, you need to specify isPayer.", tags="member-management")
    @PostMapping("/groups/{id}/users")
    public BaseResponse<StatusMeta, Object> addMemberToGroup(@RequestHeader(value = "Authorization") String token, @RequestBody InputUser user, @PathVariable("id") Long id) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException, InvalidAddMemberGroupParametersException, InvalidAddMemberGroupCorrectnessException, InvalidModifyMemberGroupParametersException  {
        JwtUser userJWT = checkToken(token);
        Presence.checkAddMember(user);
        User utoBeAdded = correctness.checkAddMember(id, userJWT, user);
        userRepository.save(utoBeAdded);
        return new BaseResponse<>(new StatusMeta(200));
    }
    
    @ApiOperation(value = "Get all members of a group.", notes = "If the token is correct, and you have permissions, you will get a list of all the users of a group.", tags="member-management")
    @GetMapping("/groups/{id}/users")
    public BaseResponse<StatusMeta, Object> getGroupMembers(@RequestHeader(value = "Authorization") String token, @PathVariable("id") Long id) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException, InvalidAddMemberGroupParametersException, InvalidAddMemberGroupCorrectnessException  {
        JwtUser userJWT = checkToken(token);
        //Presence.checkAddMember(user);
        Optional<Groups> group=groupsRepository.findById(id);
        List<InputUser> users=new ArrayList<>();
        if(group.isPresent()){
            Iterable<User>allUsers=new ArrayList<>();
            if(group.get().isIsFamilyGroup())
                allUsers=group.get().getFamilyMembers();//this.userRepository.findByfamilyGroup(group.get());
            else
                allUsers=group.get().getUsers();
            allUsers.forEach(u->{
                InputUser n=new  InputUser();
                n.setEmail(u.getEmail());
                n.setIsPayer(u.isPayer());
                n.setUsername(u.getUsername());
                n.setId(u.getId());
                users.add(n);
            }
            
            );
        }
      
    
        return new BaseResponse<>(new StatusMeta(200),users);
    }

    @ApiOperation(value = "Modify isPayer of a member in a group.", notes = "If the token is correct, and you have permissions, you will change isPayer of a user.", tags="member-management")
    @PatchMapping("/groups/{id}/users")
    public BaseResponse<StatusMeta, Object> modifyMemberPermissionGroup(@RequestHeader(value = "Authorization") String token, @RequestBody InputUser user, @PathVariable("id") Long id) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException, InvalidModifyMemberGroupParametersException, InvalidModifyMemberGroupCorrectnessException  {
        JwtUser userJWT = checkToken(token);
        Presence.checkModifyMember(user);
        User uTobeModified = correctness.checkModifyMember(id, userJWT, user);
        userRepository.save(uTobeModified);
        return new BaseResponse<>(new StatusMeta(200));
    }

    @ApiOperation(value = "Remove a user from a group.", notes = "If the token is correct, and you have permissions, you will remove a user from the group.", tags="member-management")
    @DeleteMapping("/groups/{id}/users/{user}")
    public BaseResponse<StatusMeta, Object> deleteMemberToGroup(@RequestHeader(value = "Authorization") String token, @PathVariable("id") Long id,@PathVariable("user") Long userID) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException, InvalidRemoveMemberGroupParametersException, InvalidRemoveMemberGroupCorrectnessException{
        JwtUser userJWT = checkToken(token);
        
        User u = correctness.checkDeleteMember(id, userJWT, userID);
        userRepository.save(u);
        return new BaseResponse<>(new StatusMeta(200));
    }
    
    /*Goal-Management*/
    @ApiOperation(value = "Add a goal to a group.", notes = "If the token is correct, and you have the permissions, you will add a goal to the group.", tags="goal-management")
    @PostMapping("/groups/{id}/goals")
    public BaseResponse<StatusMeta, Object> addGoal(@RequestHeader(value = "Authorization") String token,  @PathVariable("id") Long id, @RequestBody InputGoal goal) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException, InvalidAddGoalParametersException, InvalidAddGoalParametersCorrectness  {
        JwtUser userJWT = checkToken(token);
        
      
        goal.setGroupID(id);
        Presence.checkAddGoal(goal);
      
        
        Goal g = correctness.checkAddGoal(goal, userJWT);
        goalRepository.save(g);
        
        return new BaseResponse<>(new StatusMeta(200));
    }
    @ApiOperation(value = "Modify a goal in a group.", notes = "If the token is correct, and you have the permissions, you will modify a goal in a group.You can change amount and the name.", tags="goal-management")
    @PatchMapping("/groups/{id}/goals/{idGoal}")
    public BaseResponse<StatusMeta, Object> modifyGoal(@RequestHeader(value = "Authorization") String token,  @PathVariable("id") Long idGroup, @RequestBody InputGoal goal,  @PathVariable("idGoal") Long idGoal) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException, InvalidModifyGoalParametersException, InvalidModifyGoalParametersCorrectness {
        JwtUser userJWT = checkToken(token);
        goal.setGroupID(idGroup);
        goal.setGoalID(idGoal);
        
        Goal g = correctness.checkModifyGoal(goal, userJWT);
        goalRepository.save(g);
        return new BaseResponse<>(new StatusMeta(200));
    }
    
    @ApiOperation(value = "Remove a goal in a group.", notes = "If the token is correct, and you have the permissions, you will remove a goal in a group.", tags="goal-management")
    @DeleteMapping("/groups/{id}/goals/{idGoal}")
    public BaseResponse<StatusMeta, Object> deleteGoal(@RequestHeader(value = "Authorization") String token,  @PathVariable("id") Long idGroup,  @PathVariable("idGoal") Long idGoal) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException, InvalidDeleteGoalParametersCorrectness {
        JwtUser userJWT = checkToken(token);
        InputGoal goal=new InputGoal();
        goal.setGroupID(idGroup);
        goal.setGoalID(idGoal);
        Goal g = correctness.checkDeleteGoal(goal, userJWT);
        goalRepository.delete(g);
        return new BaseResponse<>(new StatusMeta(200));
    }
    
    @ApiOperation(value = "Get all goals of a group.", notes = "If the token is correct, and you have the permissions, you will get all goal of a group.", tags="goal-management")
    @GetMapping("/groups/{id}/goals")
    public BaseResponse<StatusMeta, Object> getGoalsOfGroup(@RequestHeader(value = "Authorization") String token, @PathVariable("id") Long id) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException, InvalidGroupGoalCorrectnessException {
        JwtUser userJWT = checkToken(token);
        List<OutputGoal> goals = new ArrayList<>();
        User user = userRepository.findByEmail(userJWT.getEmail());
        Optional<Groups> groupO = groupsRepository.findById(id);

        if (groupO.isPresent()) {
            Groups group = groupO.get();
            if (user.getFamilyGroup().equals(group)) {
                goals = OutputGoal.fromGoals(group.getGoals(), group.getId());
            } else {
                if (user.getCommonFundGroups().contains(group)) {
                    goals = OutputGoal.fromGoals(group.getGoals(), group.getId());
                } else {
                    group.setId(null);
                    throw new InvalidGroupGoalCorrectnessException(group);
                }
            }
        } else {
            throw new InvalidGroupGoalCorrectnessException(null);
        }

       return new BaseResponse<>(new StatusMeta(200), goals);
    }

    
    
    /*Budget-Management*/
    @PostMapping("/groups/{id}/budgets")
    @ApiOperation(value = "Add a budget to a group.", notes = "If the token is correct, and you have the permissions, you will add a budget to the group.", tags="budget-management")
    public BaseResponse<StatusMeta, Object> addBudget(@RequestHeader(value = "Authorization") String token,  @PathVariable("id") Long id, @RequestBody InputBudget budget) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException, InvalidAddBudgetParametersException, InvalidAddBudgetParametersCorrectness {
        JwtUser userJWT = checkToken(token);
        
        budget.setGroupID(id);
        Presence.checkAddBudget(budget);
        Budget b = correctness.checkAddBudget(budget, userJWT);
        budgetRepository.save(b);
        return new BaseResponse<>(new StatusMeta(200));
    }
    
    @ApiOperation(value = "Modify a budget in a group.", notes = "If the token is correct, and you have the permissions, you will modify a budget in a group.You can change amount and name.", tags="budget-management")
    @PatchMapping("/groups/{id}/budgets/{idCat}")
    public BaseResponse<StatusMeta, Object> modifyBudget(@RequestHeader(value = "Authorization") String token,  @PathVariable("id") Long idGroup, @RequestBody InputBudget budget,  @PathVariable("idCat") Long idCat) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException, InvalidModifyBudgetParametersCorrectness, InvalidAddBudgetParametersException {
        JwtUser userJWT = checkToken(token);
        budget.setGroupID(idGroup);
        budget.setCategoryID(idCat);
        Presence.checkAddBudget(budget);
        Budget b = correctness.checkModifyBudget(budget, userJWT);
        budgetRepository.save(b);
        return new BaseResponse<>(new StatusMeta(200));
    }
    
    @ApiOperation(value = "Remove a budget in a group.", notes = "If the token is correct, and you have the permissions, you will remove a budget in a group.", tags="budget-management")
    @DeleteMapping("/groups/{id}/budgets/{idCat}")
    public BaseResponse<StatusMeta, Object> deleteBudget(@RequestHeader(value = "Authorization") String token,  @PathVariable("id") Long idGroup,  @PathVariable("idCat") Long idCat) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException, InvalidAddBudgetParametersCorrectness, InvalidDeleteBudgetParametersCorrectness {

        
        JwtUser userJWT = checkToken(token);
        InputBudget budget=new InputBudget();
        budget.setGroupID(idGroup);
        budget.setCategoryID(idCat);
        
        Budget b = correctness.checkDeleteBudget(budget, userJWT);
        budgetRepository.delete(b);
        return new BaseResponse<>(new StatusMeta(200));
    }
    
    
    @ApiOperation(value = "Get all budgets of a group.", notes = "If the token is correct, and you have the permissions, you will get all budgets of a group.", tags="budget-management")
    @GetMapping("/groups/{id}/budgets")
    public BaseResponse<StatusMeta, Object> getBudgetsOfGroup(@RequestHeader(value = "Authorization") String token, @PathVariable("id") Long id) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException, InvalidGroupGoalCorrectnessException {
        JwtUser userJWT = checkToken(token);
        List<OutputBudget> budgets = new ArrayList<>();
        User user = userRepository.findByEmail(userJWT.getEmail());
        Optional<Groups> groupO = groupsRepository.findById(id);

        if (groupO.isPresent()) {
            Groups group = groupO.get();
            if (user.getFamilyGroup().equals(group)) {
                budgets = OutputBudget.fromBudgets(user.getFamilyGroup().getBudgets());
            } else {
                if (user.getCommonFundGroups().contains(group)) {
                    budgets =  OutputBudget.fromBudgets(user.getFamilyGroup().getBudgets());
                } else {
                    group.setId(null);
                    throw new InvalidGroupGoalCorrectnessException(group);
                }
            }
        } else {
            throw new InvalidGroupGoalCorrectnessException(null);
        }

       return new BaseResponse<>(new StatusMeta(200), budgets);
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
