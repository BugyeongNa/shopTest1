package com.springstudy.shop.config.advice;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/*
@RestControllerAdvice
 - @Valid과@RestControllerAdvice정에서 문제가발생하면 처리하는 어노테이션
 - @RestControllerAdvice 이용하면 컨트롤러에서 발생하는 예외에 대해
   JSON형식으로 응답 메시지를 생성해서 보내고자할 때 사용
 - handlebindException()메서드는 컨트롤러에서 BindException이 던져지는 경우
   JSON메시지와 400에러(Bad Request)전송
 */
@RestControllerAdvice
@Log4j2
public class CustomRestAdvice {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handlebindException(BindException e){
        log.error(e);

        Map<String,String> errorMap = new HashMap<>();

        if (e.hasErrors()){
            BindingResult bindingResult = e.getBindingResult();
            bindingResult.getFieldErrors().forEach(fieldErr -> {
                errorMap.put(fieldErr.getField(), fieldErr.getCode());
            });
        }

        return ResponseEntity.badRequest().body(errorMap);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleFKException(Exception e){
        log.error(e);

        Map<String,String> errorMap = new HashMap<>();

        errorMap.put("time", ""+System.currentTimeMillis());
        errorMap.put("msg", "constraint fail:참조무결성위배");

        return ResponseEntity.badRequest().body(errorMap);
    }

    @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class})
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleNoSuchElementException(Exception e){
        log.error(e);

        Map<String,String> errorMap = new HashMap<>();

        errorMap.put("time", ""+System.currentTimeMillis());
        errorMap.put("msg", "존재하지 않는 댓글입니다.");

        return ResponseEntity.badRequest().body(errorMap);
    }


}
