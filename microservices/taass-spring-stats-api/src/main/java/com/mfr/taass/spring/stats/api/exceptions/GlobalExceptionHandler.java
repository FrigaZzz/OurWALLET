package com.mfr.taass.spring.stats.api.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mfr.taass.spring.stats.api.beans.BaseResponse;
import com.mfr.taass.spring.stats.api.beans.ErrorsMeta;
import com.mfr.taass.spring.stats.api.beans.MessageMeta;
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
}