package com.onlinemarket.OnlinemarketProjectFrontend.customer;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import net.bytebuddy.utility.RandomString;

import com.onlinemarket.OnlinemarketProjectCommon.entity.AuthenticationType;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Country;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Customer;
import com.onlinemarket.OnlinemarketProjectFrontend.setting.CountryRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private CountryRepository countryRepo;

    public List<Country> listAllCountries(){
        return countryRepo.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(String email){
        Customer customer = customerRepo.findByEmail(email);
        return customer == null;
    }

    public void registerCustomer(Customer customer){
        encodePassword(customer);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());

        String randomCode = RandomString.make(64);
        customer.setVerificationCode(randomCode);

        customerRepo.save(customer);
    }

    private void encodePassword(Customer customer) {
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
    }

    public boolean verify(String verification){
        Customer customer = customerRepo.findByVerificationCode(verification);
        if(customer == null || customer.isEnabled()){
            return false;
        } else {
            customerRepo.enable(customer.getId());
            return true;
        }
    }

    public void updateAuthentication(Customer customer, AuthenticationType type){
        if(!customer.getAuthenticationType().equals(type)){
            customerRepo.updateAuthenticationType(customer.getId(), type);
        }
    }
}
