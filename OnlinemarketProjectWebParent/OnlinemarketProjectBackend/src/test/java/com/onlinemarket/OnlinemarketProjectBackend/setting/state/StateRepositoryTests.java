package com.onlinemarket.OnlinemarketProjectBackend.setting.state;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Country;
import com.onlinemarket.OnlinemarketProjectCommon.entity.State;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql=false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StateRepositoryTests {
    
    @Autowired
	private StateRepository repo;

    @Autowired TestEntityManager entityManager;

    @Test
    public void testCreateState(){
        Integer countryId = 1;
        Country country = entityManager.find(Country.class, countryId);

        // State state = repo.save(new State("Assam",country));
        State state1 = repo.save(new State("Karnataka",country));
        State state2 = repo.save(new State("Punjab",country));

        assertThat(state1).isNotNull();
        assertThat(state2).isNotNull();
    }

    @Test
    public void listStatesByCountry(){
        Integer countryId = 1;
        Country country = entityManager.find(Country.class, countryId);

        List<State> listStates = repo.findByCountryOrderByNameAsc(country);

        assertEquals(listStates.size(), 3);
    }

    @Test
    public void testGetState(){
        Integer id = 2;
        Optional<State> findByID = repo.findById(id);
        assertThat(findByID.isPresent());
    }

    @Test
    public void testDeleteState(){
        Integer id = 2;
        repo.deleteById(id);

        Optional<State> state = repo.findById(id);
        assertThat(state.isEmpty());
    }
    
    @Test
    public void testUpdateState(){
        Integer id = 1;
        String name = "Tamil Nadu";
        State state = repo.findById(id).get();
        state.setName(name);

        State updated = repo.save(state);
        assertThat(updated.getName()).isEqualTo(name);
    }
}
