package com.mfr.taass.spring.group.api.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mfr.taass.spring.group.api.beans.BaseResponse;
import com.mfr.taass.spring.group.api.beans.ErrorsMeta;
import com.mfr.taass.spring.group.api.beans.MessageMeta;
import com.mfr.taass.spring.group.api.utils.InputBudget;
import com.mfr.taass.spring.group.api.utils.InputGoal;
import com.mfr.taass.spring.group.api.utils.InputGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({JsonProcessingException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleJsonException(JsonProcessingException ex) {
        ErrorsMeta meta = new ErrorsMeta(400);
        meta.getErrors().add(ErrorsMeta.ERROR_INVALID_JSON);
        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidJWTTokenException.class})
    public final ResponseEntity<BaseResponse<MessageMeta, Object>> handleTokenNotValid(InvalidJWTTokenException ex) {
        MessageMeta meta = new MessageMeta(401, "Token non valido");
        BaseResponse<MessageMeta, Object> res = new BaseResponse<>(meta, null);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidAuthorizationHeaderException.class})
    public final ResponseEntity<BaseResponse<MessageMeta, Object>> handleBearerNotFound(InvalidAuthorizationHeaderException ex) {
        MessageMeta meta = new MessageMeta(400, "Authorization header must start with 'Bearer'");
        BaseResponse<MessageMeta, Object> res = new BaseResponse<>(meta, null);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    /**
     * ***************************************ADD*************************************************
     */
    @ExceptionHandler({InvalidAddGroupParametersException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> InvalidAddGroupParametersException(InvalidAddGroupParametersException ex) {
        ErrorsMeta meta = new ErrorsMeta(400);
        InputGroup group = ex.getProvidedGroup();

        Long userId = group.getUserID();
        if (userId == null) {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_GROUP_MISSING_USERID);
        }

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidNameGroupParametersException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidNameGroupParametersException ex) {
        ErrorsMeta meta = new ErrorsMeta(400);

        meta.getErrors().add(ErrorsMeta.ERROR_ADD_GROUP_MISSING_NAME);

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidNameGroupCorrectnessException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidNameGroupCorrectnessException ex) {
        ErrorsMeta meta = new ErrorsMeta(401);
        InputGroup group = ex.getProvidedGroup();

        Long userId = group.getUserID();
        Long groupID = group.getGroupID();

        if (groupID == null) {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_GROUP_NOT_FOUND);
        } else {
            if (userId == null) {
                meta.getErrors().add(ErrorsMeta.ERROR_NAME_GROUP_NOT_AUTHORIZED);
            } else {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_GROUPF_NOT_AUTHORIZED);
            }
        }
        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidAddMemberGroupParametersException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidAddMemberGroupParametersException ex) {
        ErrorsMeta meta = new ErrorsMeta(400);

        if (ex.getProvidedUser() != null) {
            meta.getErrors().add(ErrorsMeta.ERROR_GROUP_MEMBER_ADD_MISSING_USERNAME);
        } else {
            meta.getErrors().add(ErrorsMeta.ERROR_GROUP_MEMBER_ADD_USER_NOT_FOUND);
        }

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidAddMemberGroupCorrectnessException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidAddMemberGroupCorrectnessException ex) {
        ErrorsMeta meta = new ErrorsMeta(401);

        if (ex.getProvidedGroup() != null) {
            meta.getErrors().add(ErrorsMeta.ERROR_GROUP_MEMBER_ADD_NOT_AUTHORIZED);
        } else {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_GROUP_NOT_FOUND);
        }

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidModifyMemberGroupParametersException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidModifyMemberGroupParametersException ex) {
        ErrorsMeta meta = new ErrorsMeta(400);

        if (ex.getProvidedUser() != null) {
            meta.getErrors().add(ErrorsMeta.ERROR_GROUP_MEMBER_MODIFY_MISSING_USERNAME);
        } else {
            meta.getErrors().add(ErrorsMeta.ERROR_GROUP_MEMBER_MODIFY_USER_NOT_FOUND);
        }

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidModifyMemberGroupCorrectnessException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidModifyMemberGroupCorrectnessException ex) {
        ErrorsMeta meta = new ErrorsMeta(401);

        if (ex.getProvidedGroup() != null) {
            meta.getErrors().add(ErrorsMeta.ERROR_GROUP_MEMBER_MODIFY_NOT_AUTHORIZED);
        } else {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_GROUP_NOT_FOUND);
        }

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidRemoveMemberGroupParametersException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidRemoveMemberGroupParametersException ex) {
        ErrorsMeta meta = new ErrorsMeta(400);

        if (ex.getProvidedUser() != null) {
            meta.getErrors().add(ErrorsMeta.ERROR_GROUP_MEMBER_REMOVE_MISSING_USERNAME);
        } else {
            meta.getErrors().add(ErrorsMeta.ERROR_GROUP_MEMBER_REMOVE_USER_NOT_FOUND);
        }

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidRemoveMemberGroupCorrectnessException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidRemoveMemberGroupCorrectnessException ex) {
        ErrorsMeta meta = new ErrorsMeta(401);

        if (ex.getProvidedGroup() != null) {
            meta.getErrors().add(ErrorsMeta.ERROR_GROUP_MEMBER_REMOVE_NOT_AUTHORIZED);
        } else {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_GROUP_NOT_FOUND);
        }

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidGroupAccountCorrectnessException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidGroupAccountCorrectnessException ex) {
        ErrorsMeta meta = new ErrorsMeta(400);

        if (ex.getProvidedGroup() != null) {
            meta.getErrors().add(ErrorsMeta.ERROR_GROUP_ACCOUNT_GET_NOT_AUTHORIZED);
        } else {
            meta.getErrors().add(ErrorsMeta.ERROR_GROUP_ACCOUNT_GET_NOT_FOUND);
        }

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    /*Goal*/
    @ExceptionHandler({InvalidAddGoalParametersException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidAddGoalParametersException ex) {
        ErrorsMeta meta = new ErrorsMeta(401);
        InputGoal goal = ex.getProvidedGoal();
        if (goal.getAmount() == null) {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_MISSING_AMOUNT);
        } else {
            if (goal.getAmount() <= 0) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_INCORRECT_AMOUNT);
            }
        }
        if (goal.getDeadLine() == null) {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_MISSING_DEADLINE);
        } else {
            if (goal.getDeadLine() <= 0) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_INCORRECT_DEADLINE);
            }
        }
        
        if (goal.getDescription() == null) {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_MISSING_DESCRIPTION);
        }

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidAddGoalParametersCorrectness.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidAddGoalParametersCorrectness ex) {
        ErrorsMeta meta = new ErrorsMeta(401);
        InputGoal goal = ex.getProvidedGoal();
        if (goal != null) {
            if (goal.getGroupID() == null) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_GROUP_NOT_FOUND);
            }
        } else {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_GROUP_NOT_AUTHORIZED);
        }

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidModifyGoalParametersException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidModifyGoalParametersException ex) {
        ErrorsMeta meta = new ErrorsMeta(401);
        InputGoal goal = ex.getProvidedGoal();
        if (goal.getAmount() == null) {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_MISSING_AMOUNT);
        } else {
            if (goal.getAmount() < 0) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_INCORRECT_AMOUNT);
            }
        }
        if (goal.getDeadLine() == null) {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_MISSING_DEADLINE);
        } else {
            if (goal.getDeadLine() <= 0) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_INCORRECT_DEADLINE);
            }
        }
        

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidModifyGoalParametersCorrectness.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidModifyGoalParametersCorrectness ex) {
        ErrorsMeta meta = new ErrorsMeta(401);
        InputGoal goal = ex.getProvidedGoal();
        if (goal != null) {
            if (goal.getGroupID() == null) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_GROUP_NOT_FOUND);
            }
            if (goal.getGoalID() == null) {
                meta.getErrors().add(ErrorsMeta.ERROR_MODIFY_GOAL_GOAL_NOT_FOUND);
            }

        } else {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_GROUP_NOT_AUTHORIZED);
        }

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidDeleteGoalParametersCorrectness.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidDeleteGoalParametersCorrectness ex) {
        ErrorsMeta meta = new ErrorsMeta(401);
        InputGoal goal = ex.getProvidedGoal();
        if (goal != null) {
            if (goal.getGroupID() == null) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_GROUP_NOT_FOUND);
            }
            if (goal.getGoalID() == null) {
                meta.getErrors().add(ErrorsMeta.ERROR_MODIFY_GOAL_GOAL_NOT_FOUND);
            }

        } else {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_GROUP_NOT_AUTHORIZED);
        }

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }

    @ExceptionHandler({InvalidGroupGoalCorrectnessException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidGroupGoalCorrectnessException ex) {
        ErrorsMeta meta = new ErrorsMeta(400);

        if (ex.getProvidedGroup() != null) {
            meta.getErrors().add(ErrorsMeta.ERROR_GROUP_GOAL_GET_NOT_AUTHORIZED);
        } else {
            meta.getErrors().add(ErrorsMeta.ERROR_GROUP_GOAL_GET_NOT_FOUND);
        }

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }
    
    
    
    /*Budget*/
    @ExceptionHandler({InvalidAddBudgetParametersException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidAddBudgetParametersException ex) {
        ErrorsMeta meta = new ErrorsMeta(400);
        InputBudget budget = ex.getProvidedGroup();
        if (budget.getAmount() == null) {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_MISSING_AMOUNT);
        } 
        if (budget.getCategoryID()== null) {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_BUDGET_MISSING_CAT);
        } 
        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }
    
    @ExceptionHandler({InvalidAddBudgetParametersCorrectness.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidAddBudgetParametersCorrectness ex) {
        ErrorsMeta meta = new ErrorsMeta(401);
        InputBudget budget = ex.getProvidedGroup();
        if (budget != null) {
            if (budget.getGroupID() == null) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_GROUP_NOT_FOUND);
            }
            if (budget.getCategoryID()== null) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_CAT_NOT_FOUND);
            }
        } else {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_GROUP_NOT_AUTHORIZED);
        }

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }
    
    @ExceptionHandler({InvalidModifyBudgetParametersCorrectness.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidModifyBudgetParametersCorrectness ex) {
        ErrorsMeta meta = new ErrorsMeta(401);
        InputBudget budget = ex.getProvidedGroup();
        if (budget != null) {
            if (budget.getGroupID() == null) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_GROUP_NOT_FOUND);
            }
            if (budget.getCategoryID()== null) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_CAT_NOT_FOUND);
            }
        } else {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_GROUP_NOT_AUTHORIZED);
        }

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }
    
    @ExceptionHandler({InvalidDeleteBudgetParametersCorrectness.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRefreshError(InvalidDeleteBudgetParametersCorrectness ex) {
        ErrorsMeta meta = new ErrorsMeta(401);
        InputBudget budget = ex.getProvidedGroup();
        if (budget != null) {
            if (budget.getGroupID() == null) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_GROUP_NOT_FOUND);
            }
            if (budget.getCategoryID()== null) {
                meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_CAT_NOT_FOUND);
            }
        } else {
            meta.getErrors().add(ErrorsMeta.ERROR_ADD_GOAL_GROUP_NOT_AUTHORIZED);
        }

        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }
    
    
}
