package com.onlinemarket.OnlinemarketProjectBackend.setting.state;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Country;
import com.onlinemarket.OnlinemarketProjectCommon.entity.State;
import com.onlinemarket.OnlinemarketProjectCommon.entity.StateDTO;

@RestController
public class StateRestController {

    @Autowired StateRepository repo;

    @GetMapping("/states/list_by_country/{id}")
    public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId){
        List<State> listStates = repo.findByCountryOrderByNameAsc(new Country(countryId));
        List<StateDTO> result = new ArrayList<>();

        for(State state: listStates){
            result.add(new StateDTO(state.getName(), state.getId()));
        }
        return result;
    }

    @PostMapping("/states/save")
    public String save(@RequestBody State state){
        State savedState = repo.save(state);
        return String.valueOf(savedState.getId());
    }

    @GetMapping("/states/delete/{id}")
    public void delete(@PathVariable("id") Integer id){
        repo.deleteById(id);
    }
}
