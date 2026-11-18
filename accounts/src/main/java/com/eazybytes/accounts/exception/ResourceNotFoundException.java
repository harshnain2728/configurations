package com.eazybytes.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND) // whenever client go into this exception they will get the 400 error code which is BAD REQUEST
public class ResourceNotFoundException extends RuntimeException {
    // It keeps the code clean by avoiding throws declarations everywhere.

    // created this exception for handling the already exists data in db
    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue)
    {
        super(String.format("%s not found with the given input data %s : '%s'", resourceName, fieldName, fieldValue));
    }

}
