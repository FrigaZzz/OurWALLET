/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.mfr.taass.spring.group.api.beans;

import com.mfr.taass.spring.group.api.beans.StatusMeta;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luca
 */
public class ErrorsMeta extends StatusMeta {

    public static String ERROR_ADD_GROUP_MISSING_NAME = "missingName";
    public static String ERROR_NAME_GROUP_NOT_AUTHORIZED = "notAuthorized";
    public static String ERROR_ADD_GROUP_NOT_FOUND = "groupNotFound";
    public static String ERROR_ADD_GROUPF_NOT_AUTHORIZED = "familyNotAuthorized";
    public static String ERROR_GROUP_MEMBER_ADD_MISSING_USERNAME = "missingUsername";
    public static String ERROR_GROUP_MEMBER_ADD_USER_NOT_FOUND = "userNotFound";
    public static String ERROR_GROUP_MEMBER_ADD_NOT_AUTHORIZED = "addMemberNotAutorized";
    public static String ERROR_GROUP_MEMBER_REMOVE_MISSING_USERNAME = "missingUSername";
    public static String ERROR_GROUP_MEMBER_REMOVE_USER_NOT_FOUND = "userNotFound";
    public static String ERROR_GROUP_MEMBER_REMOVE_NOT_AUTHORIZED = "notAuthorized";
    public static String ERROR_GROUP_MEMBER_MODIFY_MISSING_USERNAME = "missingUsername";
    public static String ERROR_GROUP_MEMBER_MODIFY_USER_NOT_FOUND = "userNotFOud";
    public static String ERROR_GROUP_MEMBER_MODIFY_NOT_AUTHORIZED = "notAuthorized";
    public static String ERROR_GROUP_ACCOUNT_GET_NOT_FOUND = "notFound";
    public static String ERROR_INVALID_JSON = "InvalidJson";
    public static String ERROR_ADD_GROUP_MISSING_USERID="missingUser";
    public static String ERROR_GROUP_ACCOUNT_GET_NOT_AUTHORIZED="notAuthorized";
    public static String ERROR_ADD_GOAL_MISSING_AMOUNT="missingAmount";
    public static String ERROR_ADD_GOAL_MISSING_DEADLINE="missingDeadline";
    public static String ERROR_ADD_GOAL_MISSING_ACCOUNT="missingAccount";
    public static String ERROR_ADD_GOAL_ACCOUNT_NOT_AUTHORIZED="notAuhorized";
    public static String ERROR_ADD_GOAL_GROUP_NOT_FOUND="GroupnotFound";
    public static String ERROR_ADD_GOAL_GROUP_NOT_AUTHORIZED="notAuthorized";
    public static String ERROR_MODIFY_GOAL_GOAL_NOT_FOUND="goalNotFound";
    public static String ERROR_GROUP_GOAL_GET_NOT_FOUND="notFound";
    public static String ERROR_GROUP_GOAL_GET_NOT_AUTHORIZED="notAuthorized";
    public static String ERROR_ADD_GOAL_MISSING_DESCRIPTION = "missingDescription";
    public static String ERROR_ADD_GOAL_INCORRECT_AMOUNT="INCORRECtAmount";
    public static String ERROR_ADD_GOAL_INCORRECT_DEADLINE="INCORRECtDeadline";
    public static String ERROR_ADD_BUDGET_MISSING_CAT="missingCategory";
    public static String ERROR_ADD_GOAL_CAT_NOT_FOUND="CategoryNotFound";


    private final List<String> errors;

    public ErrorsMeta(int status) {
        super(status);
        this.errors = new ArrayList<>();
    }

    public List<String> getErrors() {
        return errors;
    }
}
