package com.onlinemarket.OnlinemarketProjectBackend.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer>, PagingAndSortingRepository<Customer, Integer> {
    
    @Query("SELECT c FROM Customer c WHERE CONCAT(c.email, ' ', c.firstName, ' ', c.lastName, ' ', c.addressLine1, ' ', c.addressLine2, ' ', c.city, ' ', c.state, ' ', c.postalCode, ' ', c.country.name) LIKE %?1%")
    public Page<Customer> findAll(String keyword, Pageable pageable);

    public long countById(Integer id);

    @Query("SELECT c from Customer c WHERE c.email = ?1")
    public Customer findByEmail(String email);

    @Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);
}
