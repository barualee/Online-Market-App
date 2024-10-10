package com.onlinemarket.OnlinemarketProjectBackend.setting;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Currency;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest(showSql=false)
//run test against real DB
@AutoConfigureTestDatabase(replace = Replace.NONE)
//commit the changes to DB
@Rollback(false)
public class CurrencyRepositoryTests {

    @Autowired
	private CurrencyRepository repo;

    //to create the DB table, run the empty test without any statements.
    @Test
    public void testCreateCurrencies(){
        List<Currency> currencies = List.of(
            new Currency("UnitedStates Dollar", "$", "USD"),
            new Currency("Indian Rupee", "₹", "INR"),
            new Currency("Euro", "€", "EUR"),
            new Currency("Japanaese Yen", "¥", "JPY")
        );

        repo.saveAll(currencies);
        Iterable<Currency> iter = repo.findAll();
        assertThat(iter).size().isEqualTo(4);
    }

    @Test
    public void testListAllOrderByNameAsc(){
        List<Currency> currencies = repo.findAllByOrderByNameAsc();
        assertThat(currencies.size()).isEqualTo(4);
    }
}
