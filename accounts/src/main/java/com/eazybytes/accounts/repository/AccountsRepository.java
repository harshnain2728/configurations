package com.eazybytes.accounts.repository;

import com.eazybytes.accounts.entity.Accounts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

    Optional<Accounts> findByCustomerId(Long customerId);

    @Transactional
    @Modifying // this method is going to modify the data and above findByCustomerId is just to fetch the record
    // So, please execute the query of this method inside transaction. If there is some error happens during runtime then entire data will be rolled back by Spring Data JPA. so, no data loss
    void deleteByCustomerId(Long customerId);
}
