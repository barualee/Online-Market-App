package com.onlinemarket.OnlinemarketProjectBackend.setting.state;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Country;
import com.onlinemarket.OnlinemarketProjectCommon.entity.State;

public interface StateRepository extends CrudRepository<State, Integer> {

    public List<State> findByCountryOrderByNameAsc(Country country);
}
