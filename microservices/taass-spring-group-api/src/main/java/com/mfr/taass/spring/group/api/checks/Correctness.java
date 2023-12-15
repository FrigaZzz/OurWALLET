/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.group.api.checks;

import com.mfr.taass.spring.group.api.entities.Account;
import com.mfr.taass.spring.group.api.entities.Budget;
import com.mfr.taass.spring.group.api.entities.Category;
import com.mfr.taass.spring.group.api.entities.Goal;
import com.mfr.taass.spring.group.api.exceptions.InvalidAddMemberGroupCorrectnessException;
import com.mfr.taass.spring.group.api.entities.Groups;
import com.mfr.taass.spring.group.api.entities.User;
import com.mfr.taass.spring.group.api.exceptions.InvalidAddBudgetParametersCorrectness;
import com.mfr.taass.spring.group.api.exceptions.InvalidAddGoalParametersCorrectness;
import com.mfr.taass.spring.group.api.exceptions.InvalidAddMemberGroupParametersException;
import com.mfr.taass.spring.group.api.exceptions.InvalidDeleteBudgetParametersCorrectness;
import com.mfr.taass.spring.group.api.exceptions.InvalidDeleteGoalParametersCorrectness;
import com.mfr.taass.spring.group.api.exceptions.InvalidJWTTokenException;
import com.mfr.taass.spring.group.api.exceptions.InvalidModifyBudgetParametersCorrectness;
import com.mfr.taass.spring.group.api.exceptions.InvalidModifyGoalParametersCorrectness;
import com.mfr.taass.spring.group.api.exceptions.InvalidModifyMemberGroupCorrectnessException;
import com.mfr.taass.spring.group.api.exceptions.InvalidModifyMemberGroupParametersException;
import com.mfr.taass.spring.group.api.exceptions.InvalidNameGroupCorrectnessException;
import com.mfr.taass.spring.group.api.exceptions.InvalidRemoveMemberGroupCorrectnessException;
import com.mfr.taass.spring.group.api.exceptions.InvalidRemoveMemberGroupParametersException;
import com.mfr.taass.spring.group.api.repos.AccountRepository;
import com.mfr.taass.spring.group.api.repos.BudgetRepository;
import com.mfr.taass.spring.group.api.repos.CategoryRepository;
import com.mfr.taass.spring.group.api.repos.GoalRepository;
import com.mfr.taass.spring.group.api.repos.GroupsRepository;
import com.mfr.taass.spring.group.api.repos.UserRepository;
import com.mfr.taass.spring.group.api.utils.InputBudget;
import com.mfr.taass.spring.group.api.utils.InputGoal;
import com.mfr.taass.spring.group.api.utils.InputGroup;
import com.mfr.taass.spring.group.api.utils.InputUser;
import com.mfr.taass.spring.group.api.utils.JwtTokenUtil;
import com.mfr.taass.spring.group.api.utils.JwtUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author matteo
 */
@Service
public class Correctness {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private GroupsRepository groupsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private CategoryRepository catRepository;
    @Autowired
    private BudgetRepository budgetRepository;

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

    public Groups checkNameGroup(InputGroup groupI, JwtUser user) throws InvalidNameGroupCorrectnessException {
        Optional<Groups> group = groupsRepository.findById(groupI.getGroupID());
        if (group.isPresent()) {
            User u = userRepository.findByUsername(user.getUsername());
            if (group.get().equals(u.getFamilyGroup())) {
                if (u.isPayer()) {
                    return group.get();
                } else {
                    groupI = InputGroup.getFilled();
                    groupI.setIsPayer(null);
                    throw new InvalidNameGroupCorrectnessException(groupI);
                }
            } else {
                if (u.getCommonFundGroups().contains(group.get())) {
                    return group.get();
                } else {
                    InputGroup.getFilled();
                    groupI.setUserID(null);
                    throw new InvalidNameGroupCorrectnessException(groupI);
                }
            }

        } else {
            InputGroup.getFilled();
            groupI.setGroupID(null);
            throw new InvalidNameGroupCorrectnessException(groupI);
        }

    }

    public User checkAddMember(Long id, JwtUser user, InputUser userToBeAdded) throws InvalidAddMemberGroupCorrectnessException, InvalidAddMemberGroupParametersException, InvalidModifyMemberGroupParametersException {

        User  uTobeAdded = this.userRepository.findByEmail(userToBeAdded.getEmail());
        if (uTobeAdded == null) {
            uTobeAdded = this.userRepository.findByUsername(userToBeAdded.getUsername());
        }
        if (uTobeAdded == null) {
            throw new InvalidAddMemberGroupCorrectnessException(null);
        }

        Optional<Groups> group = groupsRepository.findById(id);
        if (group.isPresent()) {
            User u = userRepository.findByUsername(user.getUsername());
            if (u.getFamilyGroup().equals(group.get())) {
                //if (u.isPayer()) {
                    uTobeAdded.setFamilyGroup(group.get());
                    uTobeAdded.setPayer(false);                       //Aggiungo a un gruppo famiglia -> payer = false
                    return uTobeAdded;
                //} else {
                //    group.get().setId(null);
                //    throw new InvalidAddMemberGroupCorrectnessException(group.get());
                //}

            } else {
                if (u.getCommonFundGroups().contains(group.get())) {
                    uTobeAdded.addCommonFundGroup(group.get());
                    return uTobeAdded;
                } else {
                    group.get().setId(null);
                    throw new InvalidAddMemberGroupCorrectnessException(group.get());
                }

            }

        } else {
            throw new InvalidAddMemberGroupCorrectnessException(null);
        }
    }

    public User checkDeleteMember(Long id, JwtUser userJWT, long user) throws InvalidRemoveMemberGroupParametersException, InvalidRemoveMemberGroupCorrectnessException {

        User uTobeRemoved;
        try {
            uTobeRemoved = searchUser(user);
        } catch (Exception ex) {
            throw new InvalidRemoveMemberGroupParametersException(null);
        }

        Optional<Groups> groupO = groupsRepository.findById(id);
        if (groupO.isPresent()) {
            Groups group = groupO.get();
            User u = userRepository.findByUsername(userJWT.getUsername());
            if (u.getFamilyGroup().equals(group)) {
                Groups g= new Groups();
                g.setName("family group of"+uTobeRemoved.getUsername());
                g.setIsFamilyGroup(Boolean.TRUE);
                groupsRepository.save(g);
                uTobeRemoved.setFamilyGroup(g);
                return uTobeRemoved;
//                if (u.isPayer()) {
//                    // creare un nuovo gruppo per ospitarlo?
//                    //TODO decidere che fare quando si elimina da un guppo famiglia
//                    return null;
//                } else {
//                    group.setId(null);
//                    throw new InvalidRemoveMemberGroupCorrectnessException(group);
//                }

            } else {
                if (u.getCommonFundGroups().contains(group)) {
                    uTobeRemoved.removeCommonFundGroup(group);
                    return uTobeRemoved;
                } else {
                    group.setId(null);
                    throw new InvalidRemoveMemberGroupCorrectnessException(group);
                }

            }

        } else {
            throw new InvalidRemoveMemberGroupCorrectnessException(null);
        }
    }

    public User checkModifyMember(Long id, JwtUser userJWT, InputUser user) throws InvalidModifyMemberGroupParametersException, InvalidModifyMemberGroupCorrectnessException {

        User uTobeModified;
        uTobeModified = this.userRepository.findByEmail(user.getEmail());
        if (uTobeModified == null) {
            uTobeModified = this.userRepository.findByUsername(user.getUsername());
        }
        if (uTobeModified == null) {
            throw new InvalidModifyMemberGroupParametersException(null);
        }
        

        Optional<Groups> groupO = groupsRepository.findById(id);
        if (groupO.isPresent()) {
            Groups group = groupO.get();
            User u = userRepository.findByUsername(userJWT.getUsername());
            if (u.getFamilyGroup().equals(group) && uTobeModified.getFamilyGroup().equals(group)) {
                if (u.isPayer()) {
                    uTobeModified.setPayer(user.getIsPayer());
                    return uTobeModified;
                } else {
                    group.setId(null);
                    throw new InvalidModifyMemberGroupCorrectnessException(group);
                }
            } else {
                group.setId(null);
                throw new InvalidModifyMemberGroupCorrectnessException(group);
            }

        } else {
            throw new InvalidModifyMemberGroupCorrectnessException(null);
        }
    }

    private User searchUser(long userJWT) throws Exception {
        Optional<User> user = this.userRepository.findById(userJWT);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public Goal checkAddGoal(InputGoal goal, JwtUser userJWT) throws InvalidAddGoalParametersCorrectness {
        User u = userRepository.findByUsername(userJWT.getUsername());
        Optional<Groups> groupO = groupsRepository.findById(goal.getGroupID());

        if (groupO.isPresent()) {

            Groups group = groupO.get();
            Account acc = new Account();
            acc.setIsEasyPay(Boolean.FALSE);
            acc.setName("Goal:" + goal.getName());
            accountRepository.save(acc);

            if (u.getFamilyGroup().equals(group)) {
                return fillerGoal(new Goal(), goal, group, acc);
            } else {
                if (u.getCommonFundGroups().contains(group)) {
                    return fillerGoal(new Goal(), goal, group, acc);
                } else {
                    throw new InvalidAddGoalParametersCorrectness(null);
                }
            }

        } else {
            goal.setGroupID(null);
            throw new InvalidAddGoalParametersCorrectness(goal);
        }

    }

    private Goal fillerGoal(Goal newGoal, InputGoal goal, Groups group, Account account) {
        newGoal.setAmount(goal.getAmount());
        newGoal.setDeadLine(goal.getDeadLine());
        newGoal.setStartDate(goal.getStartDate());
        newGoal.setDescription(goal.getDescription());
        newGoal.setName(goal.getName());

        newGoal.setGroups(group);
        newGoal.setAccount(account);

        account.setGoal(newGoal);
        return newGoal;
    }

    public Goal checkModifyGoal(InputGoal goal, JwtUser userJWT) throws InvalidModifyGoalParametersCorrectness {
        User u = userRepository.findByUsername(userJWT.getUsername());
        Optional<Groups> groupO = groupsRepository.findById(goal.getGroupID());

        if (groupO.isPresent()) {

            Groups group = groupO.get();
            Optional<Goal> goalO = goalRepository.findById(goal.getGoalID());
            if (goalO.isPresent()) {
                if (group.getGoals().contains(goalO.get()) && u.getFamilyGroup().equals(group)) {
                    return fillerGoal(goalO.get(), goal, group, goalO.get().getAccount());
                } else {
                    if (u.getCommonFundGroups().contains(group) && group.getGoals().contains(goalO.get())) {
                        return fillerGoal(goalO.get(), goal, group, goalO.get().getAccount());
                    } else {
                        throw new InvalidModifyGoalParametersCorrectness(null);
                    }
                }
            } else {
                goal.setGoalID(null);
                throw new InvalidModifyGoalParametersCorrectness(goal);
            }

        } else {
            goal.setGroupID(null);
            throw new InvalidModifyGoalParametersCorrectness(goal);
        }
    }

    public Goal checkDeleteGoal(InputGoal goal, JwtUser userJWT) throws InvalidDeleteGoalParametersCorrectness {
        User u = userRepository.findByUsername(userJWT.getUsername());
        Optional<Groups> groupO = groupsRepository.findById(goal.getGroupID());

        if (groupO.isPresent()) {

            Groups group = groupO.get();
            Optional<Goal> goalO = goalRepository.findById(goal.getGoalID());
            if (goalO.isPresent()) {
                if (u.getFamilyGroup().equals(group) && group.getGoals().contains(goalO.get()) && u.isPayer()) {
                    return goalO.get();
                } else {
                    if (u.getCommonFundGroups().contains(group) && group.getGoals().contains(goalO.get())) {
                        return goalO.get();
                    } else {
                        throw new InvalidDeleteGoalParametersCorrectness(null);
                    }
                }
            } else {
                goal.setGoalID(null);
                throw new InvalidDeleteGoalParametersCorrectness(goal);
            }
        } else {
            goal.setGroupID(null);
            throw new InvalidDeleteGoalParametersCorrectness(goal);
        }
    }

    public Budget checkAddBudget(InputBudget budget, JwtUser userJWT) throws InvalidAddBudgetParametersCorrectness {
        User u = userRepository.findByUsername(userJWT.getUsername());
        Optional<Groups> groupO = groupsRepository.findById(budget.getGroupID());

        if (groupO.isPresent()) {

            Groups group = groupO.get();
            Optional<Category> c = catRepository.findById(budget.getCategoryID());
            if (c.isPresent() && (u.getFamilyGroup().equals(group) || u.getCommonFundGroups().contains(group))) {
                boolean find = false;
                for (Budget b : group.getBudgets()) {
                    if (b.getCategory().equals(c.get())) {
                        find = true;
                    }

                }

                if (!find) {
                    Budget b = new Budget();
                    b.setBudgetAmount(budget.getAmount());
                    b.setGroups(group);
                    b.setCategory(c.get());
                    return b;
                } else {

                    throw new InvalidAddBudgetParametersCorrectness(null);
                }
            } else {
                budget.setCategoryID(null);
                throw new InvalidAddBudgetParametersCorrectness(budget);
            }

        } else {
            budget.setGroupID(null);
            throw new InvalidAddBudgetParametersCorrectness(budget);
        }

    }

    public Budget checkModifyBudget(InputBudget budget, JwtUser userJWT) throws InvalidModifyBudgetParametersCorrectness {
        User u = userRepository.findByUsername(userJWT.getUsername());
        Optional<Groups> groupO = groupsRepository.findById(budget.getGroupID());

        if (groupO.isPresent()) {

            Groups group = groupO.get();
            Optional<Category> c = catRepository.findById(budget.getCategoryID());
            if (c.isPresent() && (u.getFamilyGroup().equals(group) || u.getCommonFundGroups().contains(group))) {
                Optional<Budget> bO = budgetRepository.findByGroupCategory(budget.getCategoryID(), budget.getGroupID());

                if (bO.isPresent()) {
                    Budget b = bO.get();
                    b.setBudgetAmount(budget.getAmount());
                    b.setGroups(group);
                    b.setCategory(c.get());
                    return b;
                } else {

                    throw new InvalidModifyBudgetParametersCorrectness(null);
                }
            } else {
                budget.setCategoryID(null);
                throw new InvalidModifyBudgetParametersCorrectness(budget);
            }

        } else {
            budget.setGroupID(null);
            throw new InvalidModifyBudgetParametersCorrectness(budget);
        }

    }

    public Budget checkDeleteBudget(InputBudget budget, JwtUser userJWT) throws InvalidDeleteBudgetParametersCorrectness {
        User u = userRepository.findByUsername(userJWT.getUsername());
        Optional<Groups> groupO = groupsRepository.findById(budget.getGroupID());

        if (groupO.isPresent()) {

            Groups group = groupO.get();
            Optional<Category> c = catRepository.findById(budget.getCategoryID());
            if (c.isPresent() && (u.getFamilyGroup().equals(group) || u.getCommonFundGroups().contains(group))) {
                Optional<Budget> bO = budgetRepository.findByGroupCategory(budget.getCategoryID(), budget.getGroupID());

//                if(u!=null)
//                    throw new IndexOutOfBoundsException(budget.getCategoryID()+","+budget.getGroupID());
                if (bO.isPresent()) {
                    Budget b = bO.get();
                    return b;
                } else {

                    throw new InvalidDeleteBudgetParametersCorrectness(null);
                }
            } else {
                budget.setCategoryID(null);
                throw new InvalidDeleteBudgetParametersCorrectness(budget);
            }

        } else {
            budget.setGroupID(null);
            throw new InvalidDeleteBudgetParametersCorrectness(budget);
        }

    }

}
