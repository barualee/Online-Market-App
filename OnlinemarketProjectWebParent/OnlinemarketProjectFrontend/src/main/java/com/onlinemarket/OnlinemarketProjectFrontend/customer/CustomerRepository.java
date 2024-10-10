package com.onlinemarket.OnlinemarketProjectFrontend.customer;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.onlinemarket.OnlinemarketProjectCommon.entity.AuthenticationType;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer>{
    
    @Query("SELECT c from Customer c WHERE c.email = ?1")
    public Customer findByEmail(String email);

    @Query("SELECT c from Customer c WHERE c.verificationCode = ?1")
    public Customer findByVerificationCode(String email);

    @Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = NULL WHERE c.id = ?1")
    @Modifying
    public void enable(Integer id);

    @Query("UPDATE Customer c SET c.authenticationType = ?2 WHERE c.id = ?1")
    @Modifying
    public void updateAuthenticationType(Integer Id,AuthenticationType type);

}
