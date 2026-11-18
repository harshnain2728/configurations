package com.eazybytes.accounts.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
// AllArgsConstructor -> Create all arguments constructor for this class to fetch all the records.
public class Customer extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="customer_id")
    private Long customerId;

    @Column
    private String name;

    @Column
    private String email;

    @Column(name="mobile_number") // optional because it is already mapped as we are having same name.
    private String mobileNumber;

}
