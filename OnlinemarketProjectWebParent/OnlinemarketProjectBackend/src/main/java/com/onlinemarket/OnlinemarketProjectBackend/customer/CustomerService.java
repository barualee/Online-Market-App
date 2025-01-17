package com.onlinemarket.OnlinemarketProjectBackend.customer;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onlinemarket.OnlinemarketProjectBackend.setting.country.CountryRepository;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Country;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Customer;


import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {
    public static final int CUSTOMERS_PER_PAGE = 1;
    
    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword){
        
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum-1,CUSTOMERS_PER_PAGE, sort);
        if (keyword != null){
            return customerRepo.findAll(keyword, pageable);
        }
        return customerRepo.findAll(pageable);
    }

    public List<Country> listAllCountries(){
        return countryRepo.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(Integer id, String email) {
    	Customer customerByEmail = customerRepo.findByEmail(email);

        //another customer found.
        if (customerByEmail != null && customerByEmail.getId() != id){
            return false;
        }
    	return true;
    }

    public Customer get(Integer id) throws CustomerNotFoundException {
        try {
            return customerRepo.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new CustomerNotFoundException("Could not find Customer with ID: "+id);
        }
        
    }

    public void save(Customer customerInForm){

        if(!customerInForm.getPassword().isEmpty()){
            String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
            customerInForm.setPassword(encodedPassword);
        } else {
            Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();
            customerInForm.setPassword(customerInDB.getPassword());
        }
        customerRepo.save(customerInForm);
    }

    public void delete(Integer id) throws CustomerNotFoundException{
        
        //we only get the count of user in the db, without calling the entire user object.
        Long countById = customerRepo.countById(id);
        if(countById == null || countById == 0){
            throw new CustomerNotFoundException("Could not find Customer with ID: "+id);
        }
        customerRepo.deleteById(id);
    }

    public void updateCustomerEnabledStatus(Integer id, boolean status){
        customerRepo.updateEnabledStatus(id, status);
    }
}
