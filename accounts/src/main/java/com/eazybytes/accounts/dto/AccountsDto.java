package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(
        name = "Account",
        description = "Schema to hold Account information"
)
public class AccountsDto {

    @Schema(description = "Account number of Eazy Bank account", example = "1234567890")
    @NotEmpty(message = "Account number can not be a null or empty")
    @Pattern(regexp = ("^$|[0-9]10}"),message = "AccountNumber must be 10 digits")
    private Long accountNumber;

    @Schema(description = "Account Type of Eazy Bank account", example = "Savings")
    @NotEmpty(message = "Account Type should not be empty")
    private String accountType;

    @Schema(description = "Eazy Bank branch address", example = "123 New York")
    @NotEmpty(message = "BranchAddress can not be a null or empty")
    private String branchAddress;
}


// @Data -> going to generate all getters(), setters(), equals(), hashCode(), toString()

/* Suppose if one of the client app want both accounts and customer data together in a single request.
we cannot send two different objects, we can send only one single object inside a response.

We can create one more entity or POJO class inside them we can try to create the fields of accounts and customer.
That is the basic approach, we cannot do that because all these are entity classes, they are related to that database
layer. So, we should not touch these db related entity classes as they should clearly and purely represent the db tables.

So, to overcome this challenge, we can use the DTO pattern.

DTO -> Data Transfer Object

Whenever a client is trying to communicate with a backend application to get the data, we should follow the
DTO pattern, which is a design pattern that will allow to transfer data between different parts of our application.

Ex: Inside my app, we have db layer, presentation layer, service layer. So, to flow the data b/w multiple layers of
 our web app, we should use these DTO patterns. Inside these DTO patterns, we will create DTO classes
 which holds the fields that we want to transfer to and fro from the different layers of our app.

DTO pattern is a design pattern that allows you to transfer data b/w different parts of our app. DTOs are simple
objects that contain only data, and they do not contain any business logic. This makes them ideal for transferring
data b/w different layers of our app, such as the presentation layer & data access layer.

Why do we need a DTO ?
-> The client receives everything, including fields that should stay private.

With DTO

Database Entity (contains complete Database Information)
       │
       ▼
Mapper (Copies only required fields)
       │
       ▼
CustomerDetailsDTO (Safe object sent over the network)
       │
       ▼
    Client

-> Only the required fields are sent.


                                  DTO (Data Transfer Object) Pattern

                                    DTO Class
┌──────────────────────┐      ┌───────────────────────────────┐      ┌──────────────────┐      ┌───────────────────┐
│ Client Application   │ ───► │      CustomerDetailsDTO       │ ───► │      Mapper      │ ───► │ Customer Entity   │
│----------------------│      │-------------------------------│      │------------------│      │-------------------│
│ • Web Application    │ <--- │ • name                        │ <--- │ Manual Mapping   │ <--- │                   │
│ • Mobile Application │      │ • email                       │      │ ModelMapper      │      │ name              │
│ • REST API           │      │ • mobileNumber                │      │ MapStruct        │      │ email             │
│ • Postman            │      │ • accountNumber               │      └──────────────────┘      │ mobileNumber      │
└──────────────────────┘      │ • accountType                 │            ^   │               │                   │
                              │ • branchAddress               │            |   │               │                   │
                              └───────────────────────────────┘            |   │               └───────────────────┘
                                                                           |   │
                                                                           |   │
                                                                               ▼
                                                                  ┌────────────────────────┐
                                                                  │   Accounts Entity      │
                                                                  │------------------------│
                                                                  │ accountNumber          │
                                                                  │ accountType            │
                                                                  │ branchAddress          │
                                                                  │                        │
                                                                  │                        │
                                                                  └────────────────────────┘
                                                                            ^    │
                                                                            |    ▼
                                                                  ┌────────────────────────┐
                                                                  │     🗄️ Database        │
                                                                  │------------------------│
                                                                  │ Customer Table         │
                                                                  │ Accounts Table         │
                                                                  └────────────────────────┘


Mapper class: - that will take care of converting all our field values from DB entity to the DTO class.

Advantage: Reduce the network traffic because of one single request.

 */