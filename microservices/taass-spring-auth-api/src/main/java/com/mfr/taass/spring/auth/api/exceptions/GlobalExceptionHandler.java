package com.mfr.taass.spring.auth.api.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mfr.taass.spring.auth.api.beans.BaseResponse;
import com.mfr.taass.spring.auth.api.beans.Credentials;
import com.mfr.taass.spring.auth.api.beans.ErrorsMeta;
import com.mfr.taass.spring.auth.api.beans.MessageMeta;
import com.mfr.taass.spring.auth.api.beans.UserInput;
import com.mfr.taass.spring.auth.api.entities.User;
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
    
    
    @ExceptionHandler({InvalidRegistrationParametersException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRegistrationError(InvalidRegistrationParametersException ex) {
        ErrorsMeta meta = new ErrorsMeta(400);
        UserInput user = ex.getProvidedUser();
        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();
        if (username == null) meta.getErrors().add(ErrorsMeta.ERROR_MISSING_USERNAME);
        if (email == null) meta.getErrors().add(ErrorsMeta.ERROR_MISSING_EMAIL);
        if (password == null) meta.getErrors().add(ErrorsMeta.ERROR_MISSING_PASSWORD);
        if (username != null && username.contains("@")) meta.getErrors().add(ErrorsMeta.ERROR_INVALID_USERNAME);
        if (email != null && !email.contains("@")) meta.getErrors().add(ErrorsMeta.ERROR_INVALID_EMAIL);
        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }
    
    @ExceptionHandler({CredentialsAlreadyExistsException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleRegistrationError(CredentialsAlreadyExistsException ex) {
        ErrorsMeta meta = new ErrorsMeta(409);
        if (ex.usernameExists()) meta.getErrors().add(ErrorsMeta.ERROR_USERNAME_EXISTS);
        if (ex.emailExists()) meta.getErrors().add(ErrorsMeta.ERROR_EMAIL_EXISTS);
        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }
    
    @ExceptionHandler({WrongCredentialsException.class})
    public final ResponseEntity<BaseResponse<MessageMeta, Object>> handleLoginError(WrongCredentialsException ex) {
        MessageMeta meta = new MessageMeta(401, "Username e/o password errati");
        BaseResponse<MessageMeta, Object> res = new BaseResponse<>(meta, null);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }
    
    @ExceptionHandler({InvalidLoginParametersException.class})
    public final ResponseEntity<BaseResponse<ErrorsMeta, Object>> handleLoginError(InvalidLoginParametersException ex) {
        ErrorsMeta meta = new ErrorsMeta(400);
        Credentials cred = ex.getProvidedCredentials();
        if (cred.getUsername() == null) meta.getErrors().add(ErrorsMeta.ERROR_MISSING_USERNAME);
        if (cred.getPassword() == null) meta.getErrors().add(ErrorsMeta.ERROR_MISSING_PASSWORD);
        BaseResponse<ErrorsMeta, Object> res = new BaseResponse<>(meta, null);
        return new ResponseEntity<>(res, HttpStatus.valueOf(meta.getStatus()));
    }
    
    @ExceptionHandler({InvalidJWTTokenException.class})
    public final ResponseEntity<BaseResponse<MessageMeta, Object>> handleRefreshError(InvalidJWTTokenException ex) {
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
    
}
