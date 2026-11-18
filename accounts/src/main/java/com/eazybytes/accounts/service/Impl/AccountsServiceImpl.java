package com.eazybytes.accounts.service.Impl;

import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exception.CustomerAlreadyExistsException;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountsMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountsRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor  // Lombok will generate constructor which will accept all arguments
public class AccountsServiceImpl implements IAccountsService {


    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;


    @Override
    public void createAccount(CustomerDto customerDto) {
        // Input received CustomerDto to Customer Entity
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer()); // mapped the CustomerDto to Customer and transfer all the data from customerDto to new Customer object
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());  // fetch the mobile number, first define this method in Customer Repo.
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    + customerDto.getMobileNumber());
        } else {
            // customer.setCreatedAt(LocalDateTime.now());  It will take local system date or date from the server
            // customer.setCreatedBy("Anonymous");  For this we have added the AuditAwareImpl class to check the auditor
            Customer savedCustomer = customerRepository.save(customer); // Spring Data JPA taking input, preparing SQL statement, creating connection with database, executing the statements, commit, closing the connection
            // When you write save then Hibernate looks at your entity and it's field & understands the mapping & It generates SQL conceptually.
            accountsRepository.save(createNewAccount(savedCustomer));   // creating the accounts details of this new customer by calling the below method

        }
    }
    /*
    * @param customer - Customer Object
    * @return the new account details
    * */
    private Accounts createNewAccount(Customer customer){
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);  // it will give me 10 digit random number

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
       // newAccount.setCreatedAt(LocalDateTime.now());  Fetch the CreatedAt from the Local system or server
        // newAccount.setCreatedBy("Anonymous"); // instead of creating this manually we have added the auditAwareImpl which will take care of the createdBy and updatedBy - means check the auditor
        return newAccount;
    }



    // @param mobileNumber - Input Mobile Number
    // @return Accounts Details based on a given mobile Number

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber",mobileNumber)
        );

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId",customer.getCustomerId().toString())
        );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto()); // map customer entity to CustomerDTO
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto())); // map account entity for the above customer to accountsDTO
        return customerDto;
    }



    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated=false;
        AccountsDto accountsDto = customerDto.getAccountsDto(); // get AccountsDto from the customerDto
        if(accountsDto !=null) {
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            ); // Fetch account number that we received from the client

            AccountsMapper.mapToAccounts(accountsDto, accounts); // DTO to Account Entity, this will be executed only if there is a record present in the database with the above account. Copied the data from AccountDTO to Account Entity
            accounts = accountsRepository.save(accounts); // save the updated data

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );

            CustomerMapper.mapToCustomer(customerDto, customer); // transfer all the updated data from DTO to Entity (database)
            customerRepository.save(customer); // save operation -> if we don't find any primary key values inside the given entity object, then it will simply do the insert operation otherwise it is going to perform the update operation based upon the primary key that we shared inside the POJO class.
            isUpdated = true;
        }
        return isUpdated;
    }


    /*
    * @param mobileNumber - Input Mobile NUmber
    * @return boolean indicating if the delete of Account details is Successful or not
    * */

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer","mobileNumber",mobileNumber)
        );

        accountsRepository.deleteByCustomerId(customer.getCustomerId()); // for any custom method we need to put Modifying and Transactional
        customerRepository.deleteById(customer.getCustomerId()); // this deleteById method is defined by SpringBoot framework, so, don't need to mention anything Modifying and Transactional
        return true;
    }


}



/*
*
*
*

HTTP
 ↓
Controller
 ↓
DTO
 ↓
Service
 ↓
Entity
 ↓
Repository
 ↓
Database
*
*
*
*
*Data Flow from DB to Client

Database
 ↓
Repository
 ↓
Entity
 ↓
Service
 ↓
DTO
 ↓
Controller
 ↓
HTTP Response
* */