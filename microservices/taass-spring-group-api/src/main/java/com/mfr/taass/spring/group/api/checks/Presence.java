/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.group.api.checks;

import com.mfr.taass.spring.group.api.exceptions.InvalidAddBudgetParametersException;
import com.mfr.taass.spring.group.api.exceptions.InvalidAddGoalParametersException;
import com.mfr.taass.spring.group.api.exceptions.InvalidRemoveMemberGroupParametersException;
import com.mfr.taass.spring.group.api.exceptions.InvalidAddMemberGroupParametersException;
import com.mfr.taass.spring.group.api.exceptions.InvalidModifyGoalParametersException;
import com.mfr.taass.spring.group.api.exceptions.InvalidNameGroupParametersException;
import com.mfr.taass.spring.group.api.exceptions.InvalidModifyMemberGroupParametersException;
import com.mfr.taass.spring.group.api.utils.InputBudget;
import com.mfr.taass.spring.group.api.utils.InputGoal;
import com.mfr.taass.spring.group.api.utils.InputGroup;
import com.mfr.taass.spring.group.api.utils.InputUser;

/**
 *
 * @author matteo
 */
public class Presence {


    public static void checkGroupName(InputGroup newGroup) throws InvalidNameGroupParametersException {
        boolean isValid = newGroup.getName() != null;
        if (!isValid) {
            throw new InvalidNameGroupParametersException(newGroup);
        }

    }

    public static void checkAddMember(InputUser user) throws InvalidAddMemberGroupParametersException {
        boolean isValid = user.getUsername() != null;
        if (!isValid) {
            throw new InvalidAddMemberGroupParametersException(user);
        }
    }

    public static void checkDeleteMember(InputUser user) throws InvalidRemoveMemberGroupParametersException {
        boolean isValid = user.getUsername() != null;
        if (!isValid) {
            throw new InvalidRemoveMemberGroupParametersException(user);
        }
    }

    public static void checkModifyMember(InputUser user) throws InvalidModifyMemberGroupParametersException {
        boolean isValid = user.getUsername() != null;
        if (!isValid || user.getIsPayer() == null) {
            throw new InvalidModifyMemberGroupParametersException(user);
        }
    }

    public static void checkAddGoal(InputGoal goal) throws InvalidAddGoalParametersException {
        boolean isValid = goal.getAmount()!=null && goal.getDeadLine()!=null &&  goal.getStartDate()!=null&& goal.getDescription()!=null;
        if (!isValid || goal.getAmount()<= 0 || goal.getDeadLine()<=0) {
            throw new InvalidAddGoalParametersException(goal);
        }
    }

    public static void checkModifyGoal(InputGoal goal) throws InvalidModifyGoalParametersException {
        boolean isValid = goal.getAmount()!=null && goal.getDeadLine()!=null;
        if (!isValid || goal.getAmount()<= 0 || goal.getDeadLine()<=0) {
            throw new InvalidModifyGoalParametersException(goal);
        }
    }

     public static void checkAddBudget(InputBudget goal) throws InvalidAddBudgetParametersException {
        boolean isValid = goal.getAmount()!=null && goal.getCategoryID()!=null;
        if (!isValid) {
            throw new InvalidAddBudgetParametersException(goal);
        }
    }
}
