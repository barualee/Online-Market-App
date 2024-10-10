package com.onlinemarket.OnlinemarketProjectFrontend.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.onlinemarket.OnlinemarketProjectCommon.entity.AuthenticationType;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Country;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Customer;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest(showSql=false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {

    @Autowired
	private CustomerRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    //to create the DB table, run the empty test without any statements.
    @Test
    public void testCreateCustomer(){
        Integer countryID = 2; //Canada
        Country country = entityManager.find(Country.class, countryID);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("Lee");
        customer.setLastName("Barua");
        customer.setEmail("barua.nishant97@gmail.com");
        customer.setPassword("abcd@1234");
        customer.setPhoneNumber("437-210-5106");
        customer.setAddressLine1("#2, 7940 Pie-IX");
        customer.setCity("Montreal");
        customer.setState("Quebec");
        customer.setPostalCode("H1Z3T3");
        customer.setCreatedTime(new Date());

        Customer savedCustomer = repo.save(customer);
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSecondCustomer(){
        Integer countryID = 1; //India
        Country country = entityManager.find(Country.class, countryID);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("Lisa");
        customer.setLastName("Barua");
        customer.setEmail("rikshitabarua56@gmail.com");
        customer.setPassword("abcd@1234");
        customer.setPhoneNumber("887-623-8807");
        customer.setAddressLine1("#1, BGR Township");
        customer.setCity("Bongaigaon");
        customer.setState("Assam");
        customer.setPostalCode("783385");
        customer.setCreatedTime(new Date());

        Customer savedCustomer = repo.save(customer);
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void testUpdateAuthenticationType(){
        repo.updateAuthenticationType(1, AuthenticationType.GOOGLE);
        Customer customer = repo.findById(1).get();
        
        assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.GOOGLE);
    }
}
