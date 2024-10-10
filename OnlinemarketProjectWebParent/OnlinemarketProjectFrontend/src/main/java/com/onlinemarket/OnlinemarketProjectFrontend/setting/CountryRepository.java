package com.onlinemarket.OnlinemarketProjectFrontend.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Country;

public interface CountryRepository extends CrudRepository<Country, Integer>{

    public List<Country> findAllByOrderByNameAsc();
}
