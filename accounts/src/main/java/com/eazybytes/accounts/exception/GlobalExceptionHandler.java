package com.eazybytes.accounts.exception;

import com.eazybytes.accounts.dto.ErrorResponseDto;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice // If any exceptions happens in any of the controller then please invoke methods  write in this class
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override // we are overriding this method from the above extended class (ResponseEntityExceptionHandler class) this method will send the response to the client about the Validation portion
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> validationErrors = new HashMap<>(); // create empty key, value
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors(); // this will give all validation errors failed in the input data that we receive

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError)error).getField(); // getting the field
            String validationMsg = error.getDefaultMessage(); // getting the validation Message
            validationErrors.put(fieldName, validationMsg); // putting both the values in the HashMap
        });

        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);

    }




    // Below class is the Global class that will handle all types of RuntimeException
    @ExceptionHandler(Exception.class)  // Exception.class can represent all kinds of Checked and Unchecked Exceptions
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception,
                                                                  WebRequest webRequest){
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),  // will get only api path, if it is true then will get IP address as well and other details also
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(), // will get message from AccountsServiceImpl that "throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber"
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    @ExceptionHandler(ResourceNotFoundException.class)  // provide the Exception name
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                                 WebRequest webRequest){
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),  // will get only api path, if it is true then will get IP address as well and other details also
                HttpStatus.NOT_FOUND,
                exception.getMessage(), // will get message from AccountsServiceImpl that "%s not found with the given input data %s"
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

// Your global exception handler can then convert that exception into an appropriate HTTP response.
    @ExceptionHandler(CustomerAlreadyExistsException.class)  // provide the Exception name
    public ResponseEntity<ErrorResponseDto> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException exception,
                                                                                 WebRequest webRequest){
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),  // will get only api path, if it is true then will get IP address as well and other details also
                HttpStatus.BAD_REQUEST,
                exception.getMessage(), // will get message from AccountsServiceImpl that "throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber"
                LocalDateTime.now()

        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

}
