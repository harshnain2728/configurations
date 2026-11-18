package com.eazybytes.accounts.entity;

import com.eazybytes.accounts.audit.AuditAwareImpl;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // indicates to the Spring Data JPA that this class is going to act as a superclass for all my entities,
// wherever I'm trying to extend this base entity class. because we are using the below four entities in all the Entities and these are common

@EntityListeners(AuditingEntityListener.class) // to perform the auditing by the Spring Data framework from this class

@Getter @Setter @ToString // lombok is taking care of creating the getter and setter method
// for the below entities, it is not showing those methods here but behind the scenes all the getter and setter methods are there just because of lombok
// To String -> to convert the baseEntity Java object to String
public class BaseEntity {

    @CreatedDate // Telling to Spring framework whenever you will see these four columns, createdAt, createdBy, updatedAt, updatedBy then please update or insert the table based on the time.
    @Column(updatable=false) // mention the name of the table field to whom we need to map it with. Since, it is already mapped because we use the same name.
    // whenever a field is updated in the db, i don't want that this column to be considered by Spring Data JPA to populate and update the value.
    // means this field will not be updated whenever I'm trying to update my record, because i only want to maintain this created at time to represent the what is the original time when my record is inserted.
    private LocalDateTime createdAt;

    @CreatedBy // for this we are not sure who is updating and creating the data, for that we need to write the small code inside audit package AuditAwareImpl
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(insertable = false)
    private String updatedBy;
}
