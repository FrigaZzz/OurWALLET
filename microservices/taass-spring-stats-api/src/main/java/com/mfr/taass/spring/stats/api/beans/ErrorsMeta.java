/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.stats.api.beans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luca
 */
public class ErrorsMeta extends StatusMeta {

    public static final String ERROR_ADD_MISSING_AMOUNT = "incorrectAmount";
    public static final String ERROR_ADD_MISSING_GROUP_SENDER = "missingGroupSender";
    public static final String ERROR_ADD_MISSING_ACCOUNT_SENDER = "missingAccountSender";
    public static final String ERROR_ADD_GROUP_PERMISSION_DENIED = "groupPermissionDenied";
    public static final String ERROR_ADD_ACCOUNT_PERMISSION_DENIED = "accountPermissionDenied";
    public static final String ERROR_ADD_ACCOUNT_RECEIVER_NOT_FOUND="receiverNotFound";
    public static final String ERROR_ADD_ACCOUNT_CATEGORY_NOT_FOUND="categoryNotFound";

    public static String ERROR_DELETE_MISSING_ID = "missingId";
    public static String ERROR_DELETE_ID_PERMISSION_DENIED ="deleteUnauthorized";

    public static String ERROR_MODIFY_GROUP_PERMISSION_DENIED="groupDenied";
    public static String ERROR_MODIFY_ACCOUNT_PERMISSION_DENIED="accountDenied";
    public static String ERROR_MODIFY_ID_PERMISSION_DENIED = "transactionModifyIDdenied";
    public static String ERROR_MODIFY_MISSING_ACCOUNT_SENDER = "accountSender";
    public static String ERROR_MODIFY_MISSING_GROUP_SENDER ="groupSender";
    public static String ERROR_MODIFY_MISSING_AMOUNT="missingAmount";
    public static String ERROR_MODIFY_MISSING_ID="missingId";
    public static String ERROR_INVALID_JSON="invalidJson";
    public static String ERROR_BUDGET = "BudgetNotRespected";

    private final List<String> errors;

    public ErrorsMeta(int status) {
        super(status);
        this.errors = new ArrayList<>();
    }

    public List<String> getErrors() {
        return errors;
    }
}