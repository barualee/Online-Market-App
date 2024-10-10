package com.onlinemarket.OnlinemarketProjectFrontend.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Integer>, PagingAndSortingRepository<Product, Integer>{

    @Query("SELECT p FROM Product p WHERE p.enabled = true "
    + "AND (p.category.id = ?1)"
    + "ORDER BY p.name ASC")
    public Page<Product> listByCategory(Integer categoryId, String categoryIDMatch, Pageable pageable);
}
