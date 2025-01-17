package com.onlinemarket.OnlinemarketProjectBackend.product;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductRepository repo;

    public List<Product> findAll(){
        return (List<Product>) repo.findAll();
    }

    public Product save(Product product){
        if(product.getId() == null){
            product.setCreatedTime(new Date());
        }
        if(product.getAlias() == null || product.getAlias().isEmpty()){
            String defaultAlias = product.getName().replaceAll(" ", "-");
            product.setAlias(defaultAlias);
        } else {
            product.setAlias(product.getName().replaceAll(" ", "-"));
        }
        product.setUpdatedTime(new Date());
        return repo.save(product);
    }

    public String checkUnique(Integer id, String name){
        boolean isCreatingNew = (id == null || id == 0);

        Product productName = repo.findByName(name);
        if(isCreatingNew){
            if(productName != null){
                return "Duplicate";
            }
        } else {
            if(productName != null && productName.getId() != id){
                return "Duplicate";
            }
        }
        return "OK";
    }

    public void updateProductEnabledStatus(Integer id, boolean enabled){
        repo.updateEnabledStatus(id, enabled);
    }

    public void delete(Integer id) throws ProductNotFoundException{
        Long countById = repo.countById(id);
        if(countById == null || countById == 0){
            throw new ProductNotFoundException("Could not find product with ID: "+id);
        }
        repo.deleteById(id);
    }

    public Product get(Integer id) throws ProductNotFoundException{
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new ProductNotFoundException("Could not find product with ID :"+id);
        }
    }
}
