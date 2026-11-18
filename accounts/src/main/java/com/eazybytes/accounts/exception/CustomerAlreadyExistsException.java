package com.eazybytes.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST) // whenever client go into this exception they will get the 400 error code which is BAD REQUEST
public class CustomerAlreadyExistsException extends RuntimeException {
    // It keeps the code clean by avoiding throws declarations everywhere.

    // created this exception for handling the already exists data in db
    public CustomerAlreadyExistsException(String message){
        super(message);
}

}
