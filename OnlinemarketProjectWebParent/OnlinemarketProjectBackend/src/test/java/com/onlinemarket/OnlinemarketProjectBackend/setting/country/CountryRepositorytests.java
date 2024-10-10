package com.onlinemarket.OnlinemarketProjectBackend.setting.country;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Country;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest(showSql=false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CountryRepositorytests {
    
    @Autowired
	private CountryRepository repo;

    @Test
    public void testCreateCountry(){
        Country country = repo.save(new Country("Canada", "CA"));
        assertThat(country).isNotNull();
        assertThat(country.getId()).isGreaterThan(0);
    }

    @Test
    public void testListCountriesOrderByNameAsc(){
        List<Country> countries = repo.findAllByOrderByNameAsc();
        assertThat(countries.size()).isEqualTo(2);
    }

    @Test
    public void testGetCountry(){
        Integer id = 2;
        Country country = repo.findById(id).get();
        assertThat(country).isNotNull();
    }

    @Test
    public void testDeleteCountry(){
        Integer id = 2;
        repo.deleteById(id);

        Optional<Country> country = repo.findById(id);
        assertThat(country.isEmpty());
    }
    
    @Test
    public void testUpdateCountry(){
        Integer id = 1;
        String name = "Republic of India";
        Country country = repo.findById(id).get();
        country.setName(name);

        Country updated = repo.save(country);
        assertThat(updated.getName()).isEqualTo(name);
    }
}
