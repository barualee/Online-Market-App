package com.onlinemarket.OnlinemarketProjectBackend.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Integer>{

    //this is special function name with column name embedded, doesnt require SQL syntax additionally.
    public List<Currency> findAllByOrderByNameAsc();
}
