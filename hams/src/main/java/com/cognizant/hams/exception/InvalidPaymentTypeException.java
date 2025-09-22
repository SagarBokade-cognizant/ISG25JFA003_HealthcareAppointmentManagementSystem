package com.cognizant.hams.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidPaymentTypeException extends RuntimeException {
    public InvalidPaymentTypeException(String message) {
        super(message);
    }
}