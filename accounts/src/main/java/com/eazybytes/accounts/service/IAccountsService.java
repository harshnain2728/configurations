package com.eazybytes.accounts.service;

import com.eazybytes.accounts.dto.CustomerDto;

public interface IAccountsService {

    /*
    *
    * @param customerDto - CustomerDto Object
    * */

    void createAccount(CustomerDto customerDto);
    // this method is going to accept the CustomerDto as a input parameters
    // above is the abstract method because there is no implementation here

    CustomerDto fetchAccount(String mobileNumber);

    boolean updateAccount(CustomerDto customerDto); // update the details except the account number

    boolean deleteAccount(String mobileNumber);

}


