package com.eazybytes.accounts.controllers;

import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.AccountsContactInfoDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.dto.ResponseDto;
import com.eazybytes.accounts.service.IAccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;


@Tag(
        name = "CRUD REST APIs for Accounts in EazyBanks",
        description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH and DELETE accounts details"
)
@RestController
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})  // In this annotation we are using /api path as prefix
// which means any REST API which are going to implement inside this controller class, it is
// going to have this prefix path inside API.
// All REST APIs in this controller are going to support the response type of JSON

// @AllArgsConstructor  // Removed because of getting an error as "Parameter 1 of constructor in com.eazybytes.accounts.controllers.AccountsController required a bean of type 'java.lang.String' that could not be found."

@Validated // Tell to spring boot framework to perform validations on all the REST APIs that is defined inside this controller
public class AccountsController {

    // Autowiring the service class to this Controller class
    private final IAccountsService iAccountsService;  // calling interface here

    @Autowired
    public AccountsController(IAccountsService iAccountsService)
    {
        this.iAccountsService=iAccountsService;
    }


    @Value("${build.version}") // build.version defined inside app.yml
    private String buildVersion;  // created this to invoke the property from application.yml


    @Autowired
    private Environment environment;

    @Autowired
    private AccountsContactInfoDto accountsContactInfoDto;

    @Operation(
            summary = "Create Account REST API",
            description = "REST API to create a Customer and Account inside EazyBank"
    ) // Swagger Annotation
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class) // Because any API can have Runtime Exception that's why mentioned in all the APIs
                    )
            )
    }
) // Swagger Annotation
    @PostMapping("/create")  // This is a create operation, we need to use HTTP POST method
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        // @Valid -> to perform all the validation that i have added in the CustomerDto class
        iAccountsService.createAccount(customerDto); // it will check all these things like exception or other things written in service class.
        return ResponseEntity
                .status(HttpStatus.CREATED)  // given resource is CREATED successfully in Header.
                .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200)); // Body
        // Now we decide what data the client should receive. Whatever you place inside .body() becomes the response body.
        // Instead of writing the status and message here, we have stored these inside the constants (AccountsConstants)


    /*
    * Normally you can return:
        return customer;

        But sometimes you also want to return:

        HTTP Status Code
        Headers
        Body

ResponseEntity allows you to control the entire HTTP response.
    *
    * */

    /*   @RequestBody -> "Read the JSON coming from the client and convert it into a Java object."

    * Suppose Postman sends:

        {
            "name":"Harsh",
            "email":"harsh@gmail.com",
            "mobileNumber":"9876543210"
        }

        Spring automatically converts it into:

        CustomerDto customerDto = new CustomerDto();

        customerDto.setName("Harsh");
        customerDto.setEmail("harsh@gmail.com");
        customerDto.setMobileNumber("9876543210");

        You don't write this conversion code. Spring does it for you. Spring Boot uses the Jackson library by default to perform this conversion.
        Spring reads the body, not the URL.
    *
        @RequestBody converts the incoming JSON into a CustomerDto object.
        CustomerDto customerDto contains the request data as a Java object.
    * */
    }

    // Create below REST API that will fetch the account and customer details by giving input as mobile Number

    @Operation(
            summary = "Fetch Account Details REST API",
            description = "REST API to fetch Customer and Account details based on a mobile number"
    ) // Swagger Annotation
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class) // Because any API can have Runtime Exception that's why mentioned in all the APIs
                    )
            )
    }
    ) // Swagger Annotation
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam @Pattern(regexp ="(^S|[0-9]{10})",message = "Mobile number must be 10 digits") String mobileNumber) // @RequestParam - because we are giving only one input
    // @Pattern(regexp ="(^S|[0-9]{10})",message = "Mobile number must be 10 digits") - to perform all the validation that i have added in the CustomerDto class for Mobile Number
    {
        CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);

    }


    @Operation(
            summary = "Update Account Details REST API",
            description = "REST API to Update Customer and Account details based on a account number"
    ) // Swagger Annotation
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class) // Because any API can have Runtime Exception that's why mentioned in all the APIs
                    )
            )
    }
    ) // Swagger Annotation

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto){
        boolean isUpdated = iAccountsService.updateAccount(customerDto);
        if(isUpdated){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }
        else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417,AccountsConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(
            summary = "Delete Account & Customer Details REST API",
            description = "REST API to delete Customer and Account details based on a mobile number"
    ) // Swagger Annotation
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class) // Because any API can have Runtime Exception that's why mentioned in all the APIs
                    )
            )
    }
    ) // Swagger Annotation

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam @Pattern(regexp ="(^S|[0-9]{10})",message = "Mobile number must be 10 digits") String mobileNumber){
        boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);
        if(isDeleted){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200,AccountsConstants.MESSAGE_200));
        }
        else{
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(AccountsConstants.STATUS_417,AccountsConstants.MESSAGE_417_DELETE));
        }
    }


    @Operation(
            summary = " Get Build Information",
            description = "Get Build information that is deployed into accounts microservice"
    ) // Swagger Annotation
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class) // Because any API can have Runtime Exception that's why mentioned in all the APIs
                    )
            )
    }
    )

    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildVersion(){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }


    @Operation(
            summary = " Get Java Version",
            description = "Get Java Version that is installed into accounts microservice"
    ) // Swagger Annotation
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class) // Because any API can have Runtime Exception that's why mentioned in all the APIs
                    )
            )
    }
    )

    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion(){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(environment.getProperty("Java_Home"));
    }


    @Operation(
            summary = " Get Contact Info",
            description = "Contact Info details that can be reached out in case of any issues"
    ) // Swagger Annotation
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class) // Because any API can have Runtime Exception that's why mentioned in all the APIs
                    )
            )
    }
    )

    @GetMapping("/contact-info")
    public ResponseEntity<AccountsContactInfoDto> getContactInfo(){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsContactInfoDto);
    }

}
