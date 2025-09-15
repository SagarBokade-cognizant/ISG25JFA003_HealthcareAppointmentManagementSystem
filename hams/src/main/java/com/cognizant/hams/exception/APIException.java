package com.cognizant.hams.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class APIException extends RuntimeException {
    private static final long serialVersionUId = 1L;



    public APIException(String message) {
        super(message);
    }
}
