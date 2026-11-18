package com.eazybytes.accounts.repository;

import com.eazybytes.accounts.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository <Customer, Long> {
// This JPA repository is going to accept the two parameters
    // 1. What is the entity class which is going to be handle by this repository class
    // 2. what is the data type of your primary key field inside your entity.

    // With the help of this Spring Data Jpa, we don't have to write any code to
    // execute the CRUD operations on our table, which is customer




    // abstract method or Derived name method because based upon the method name, Spring Data JPA is going to prepare the sql query
    // & run the same onto the db
    // if we need two columns then method name will be like findByMobileNumberAndEmail (String mobileNumber, String email)
    Optional<Customer> findByMobileNumber(String mobileNumber);
    // Because we need to identify the customer is already exists or not
    // Since, for new customer are we getting customer Id ? No, so, we need to validate the user by MobileNumber
    // Now, Spring Data JPA fetching the records based on the Mobile Number from the database to validate the already exists or not.

}

/*
*
*

Database
   ▲
   │
Entity
   ▲
   │
Repository (Hibernate looks at your entity and it's field, Hibernate understands the mapping & It generates SQL conceptually )
   ▲
   │
Service
   │
Mapper (Convert DTO to Entity)
   │
  DTO
   ▲
   │
Controller
   ▲
   │
Client


*
* */