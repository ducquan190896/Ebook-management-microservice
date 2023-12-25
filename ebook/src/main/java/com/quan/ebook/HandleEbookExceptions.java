package com.quan.ebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import com.quan.ebook.exceptions.BadResultException;
import com.quan.ebook.exceptions.EntityExistingException;
import com.quan.ebook.exceptions.EntityNotFoundException;
import com.quan.ebook.exceptions.ErrorResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import jakarta.validation.ConstraintViolationException;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class HandleEbookExceptions extends ResponseEntityExceptionHandler {

    @Override
    protected Mono<ResponseEntity<Object>> handleWebExchangeBindException(WebExchangeBindException ex,
            HttpHeaders headers, HttpStatusCode status, ServerWebExchange exchange) {
        ErrorResponse err = new ErrorResponse(ex.getBindingResult().getFieldError().getDefaultMessage());
        return Mono.just(new ResponseEntity<Object>(err, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public Mono<ResponseEntity<Object>> handlConstraintViolationException(ConstraintViolationException ex) {
        ErrorResponse err = new ErrorResponse(ex.getLocalizedMessage());
        return Mono.just(new ResponseEntity<Object>(err, HttpStatus.BAD_REQUEST));
    }


    @ExceptionHandler({ EntityNotFoundException.class })
    public Mono<ResponseEntity<Object>> handlEntityException(RuntimeException ex) {
        ErrorResponse err = new ErrorResponse(ex.getLocalizedMessage());
        return Mono.just(new ResponseEntity<Object>(err, HttpStatus.NOT_FOUND));
    }

     @ExceptionHandler({ BadResultException.class })
    public Mono<ResponseEntity<Object>> handlBadResultException(RuntimeException ex) {
        ErrorResponse err = new ErrorResponse(ex.getLocalizedMessage());
        return Mono.just(new ResponseEntity<Object>(err, HttpStatus.BAD_REQUEST));
    }



    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<Object>> handlingRuntimeException(RuntimeException ex) {
        ErrorResponse err = new ErrorResponse(ex.getLocalizedMessage());
        return Mono.just(new ResponseEntity<Object>(err, HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
