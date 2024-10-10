package com.onlinemarket.OnlinemarketProjectBackend.brand;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Brand;

public interface BrandRepository extends CrudRepository<Brand, Integer>, PagingAndSortingRepository<Brand, Integer> {

    public Long countById(Integer id);

    public Brand findByName(String name);

    @Query("SELECT b FROM Brand b WHERE b.name like %?1%")
    public Page<Brand> findAll(String keyword, Pageable pageable);

    //JPA projection
    @Query("SELECT NEW Brand(b.id, b.name) b FROM Brand b ORDER BY b.name ASC")
    public List<Brand> findAll();
}
