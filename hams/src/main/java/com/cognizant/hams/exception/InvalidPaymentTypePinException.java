package com.cognizant.hams.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidPaymentTypePinException extends RuntimeException {
    public InvalidPaymentTypePinException(String message) {
        super(message);
    }
}
