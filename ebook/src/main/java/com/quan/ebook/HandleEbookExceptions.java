package com.quan.ebook;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.quan.ebook.exceptions.BadResultException;
import com.quan.ebook.exceptions.EntityExistingException;
import com.quan.ebook.exceptions.EntityNotFoundException;
import com.quan.ebook.exceptions.ErrorResponse;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class HandleEbookExceptions {
     @ExceptionHandler({EntityNotFoundException.class, EntityExistingException.class, BadResultException.class}) 
    public ResponseEntity<Object> handlEntityException(RuntimeException ex) {
        ErrorResponse err = new ErrorResponse(Arrays.asList(ex.getMessage()));
        return new ResponseEntity<Object>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handlingArgumentException(MethodArgumentNotValidException ex) {
        ErrorResponse err = new ErrorResponse(Arrays.asList(ex.getMessage())); 
        return new ResponseEntity<Object>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handlingConstraintViolationException(ConstraintViolationException ex) {
        ErrorResponse err = new ErrorResponse(Arrays.asList(ex.getMessage())); 
        return new ResponseEntity<Object>(err, HttpStatus.BAD_REQUEST);
    }
}
