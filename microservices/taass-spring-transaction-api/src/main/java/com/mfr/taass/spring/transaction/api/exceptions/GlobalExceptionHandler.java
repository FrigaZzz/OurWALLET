package com.mfr.taass.spring.transaction.api.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mfr.taass.spring.transaction.api.beans.BaseResponse;
import com.mfr.taass.spring.transaction.api.beans.ErrorsMeta;
import com.mfr.taass.spring.transaction.api.beans.MessageMeta;
import com.mfr.taass.spring.transaction.api.entities.Account;
import com.mfr.taass.spring.transaction.api.entities.Groups;
import com.mfr.taass.spring.transaction.api.entities.Transaction;
import com.mfr.taass.spring.transaction.api.utils.InputTransaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public final Long ERROR = -1L;

    @ExceptionHandler({JsonProcessingException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleJsonException(JsonProcessingException ex) {
        ErrorsMeta meta = new ErrorsMeta(400);
        meta.getErrors().add(ErrorsMeta.ERROR_INVALID_JSON);
        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidJWTTokenException.class})
    public final ResponseEntity<BaseResponse<MessageMeta, Object>> handleTokenInvalid(InvalidJWTTokenException ex) {
        MessageMeta meta = new MessageMeta(401, "Token non valido");
        BaseResponse<MessageMeta, Object> res = new BaseResponse<>(meta, null);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidAuthorizationHeaderException.class})
    public final ResponseEntity<BaseResponse<MessageMeta, Object>> handleRefreshError(InvalidAuthorizationHeaderException ex) {
        MessageMeta meta = new MessageMeta(400, "Authorization header must start with 'Bearer'");
        BaseResponse<MessageMeta, Object> res = new BaseResponse<>(meta, null);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    /**
     * ***************************************ADD*************************************************
     */
    @ExceptionHandler({InvalidAddTransactionParametersException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleATParams(InvalidAddTransactionParametersException ex) {
        ErrorsMeta meta = new ErrorsMeta(400);
        InputTransaction transaction = ex.getProvidedTransaction();
        Long amount = transaction.getAmount();
        Long groupSender = transaction.getGroupSenderID();
        Long accountSender = transaction.getAccountSenderID();

        if (amount == null) {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_MISSING_AMOUNT);
        }
        if (groupSender == null) {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_MISSING_GROUP_SENDER);
        }
        if (accountSender == null) {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_MISSING_ACCOUNT_SENDER);
        }
        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidAddTransactionCorrectnessParametersException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleATCorrectness(InvalidAddTransactionCorrectnessParametersException ex) {
        ErrorsMeta meta = new ErrorsMeta(401);
        InputTransaction transaction = ex.getProvidedTransaction();

        if (transaction != null) {
            Long groupSender = transaction.getGroupSenderID();
            Long accountSender = transaction.getAccountSenderID();
            Long categoryID = transaction.getCategoryID();
            Long accountReceiver = transaction.getAccountSenderID();

            if (groupSender == null) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_GROUP_PERMISSION_DENIED);
            }
            if (accountSender == null) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_ACCOUNT_PERMISSION_DENIED);
            }
            if (categoryID != null && categoryID.equals(ERROR)) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_ACCOUNT_CATEGORY_NOT_FOUND);
            }

            if (accountReceiver != null && accountReceiver.equals(ERROR)) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_ACCOUNT_RECEIVER_NOT_FOUND);
            }
        } else {
            meta.getErrors().add(ErrorsMeta.ERROR_BUDGET);

        }
        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    /**
     * ***************************************Modify*************************************************
     */
    @ExceptionHandler({InvalidModifyTransactionParametersException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidModifyTransactionParametersException ex) {
        ErrorsMeta meta = new ErrorsMeta(400);
        InputTransaction transaction = ex.getProvidedTransaction();
        Long amount = transaction.getAmount();
        Long id = transaction.getId();
        Long groupSender = transaction.getGroupSenderID();
        Long accountSender = transaction.getAccountSenderID();

        if (id == null) {
            meta.getErrors().add(ErrorsMeta.ERROR_MODIFY_MISSING_ID);
        }
        if (amount == null) {
            meta.getErrors().add(ErrorsMeta.ERROR_MODIFY_MISSING_AMOUNT);
        }
        if (groupSender == null) {
            meta.getErrors().add(ErrorsMeta.ERROR_MODIFY_MISSING_GROUP_SENDER);
        }
        if (accountSender == null) {
            meta.getErrors().add(ErrorsMeta.ERROR_MODIFY_MISSING_ACCOUNT_SENDER);
        }
        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidModifyTransactionCorrectnessParametersException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidModifyTransactionCorrectnessParametersException ex) {
        ErrorsMeta meta = new ErrorsMeta(401);
        InputTransaction transaction = ex.getProvidedTransaction();

        if (transaction != null) {
            Long groupSender = transaction.getGroupSenderID();
            Long accountSender = transaction.getAccountSenderID();
            Long categoryID = transaction.getCategoryID();
            Long accountReceiver = transaction.getAccountSenderID();

            if (groupSender == null) {
                meta.getErrors().add(ErrorsMeta.ERROR_MODIFY_GROUP_PERMISSION_DENIED);
            }
            if (accountSender == null) {
                meta.getErrors().add(ErrorsMeta.ERROR_MODIFY_ACCOUNT_PERMISSION_DENIED);
            }
            if (transaction.getId() == null) {
                meta.getErrors().add(ErrorsMeta.ERROR_MODIFY_ID_PERMISSION_DENIED);
            }
            if (categoryID != null && categoryID == -1) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_ACCOUNT_CATEGORY_NOT_FOUND);
            }
            if (accountReceiver != null && accountReceiver == -1) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_ACCOUNT_RECEIVER_NOT_FOUND);
            }
        } else {
            meta.getErrors().add(ErrorsMeta.ERROR_BUDGET);

        }

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    /**
     * ***************************************Delete*************************************************
     */
    @ExceptionHandler({InvalidDeleteTransactionCorrectnessParametersException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidDeleteTransactionCorrectnessParametersException ex) {
        ErrorsMeta meta = new ErrorsMeta(401);
        Transaction transaction = ex.getProvidedTransaction();
        if (transaction == null) {
            meta.getErrors().add(ErrorsMeta.ERROR_DELETE_MISSING_ID);
        } else {
            Groups group = transaction.getGroups();

            if (group == null) {
                meta.getErrors().add(ErrorsMeta.ERROR_DELETE_ID_PERMISSION_DENIED);
            }
        }

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    /**
     * ***************************************Get*************************************************
     */
    @ExceptionHandler({ForbiddenAccessToResourceException.class})
    public final ResponseEntity<BaseResponse<MessageMeta, Object>> handleForbiddenAccess(ForbiddenAccessToResourceException ex) {
        MessageMeta meta = new MessageMeta(403, "You cannot access this resource");
        BaseResponse<MessageMeta, Object> res = new BaseResponse<>(meta, null);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public final ResponseEntity<BaseResponse<MessageMeta, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        MessageMeta meta = new MessageMeta(404, "The requested resource does not exist");
        BaseResponse<MessageMeta, Object> res = new BaseResponse<>(meta, null);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }
}