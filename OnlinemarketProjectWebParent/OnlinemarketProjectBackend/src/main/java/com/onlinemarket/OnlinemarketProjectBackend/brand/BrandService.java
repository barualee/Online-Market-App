package com.onlinemarket.OnlinemarketProjectBackend.brand;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Brand;

@Service
public class BrandService {

    @Autowired
    private BrandRepository repo;

    public List<Brand> findAll(){
        return (List<Brand>) repo.findAll();
    }

    public Brand save(Brand brand){
        return repo.save(brand);
    }

    public void delete(Integer id) throws BrandNotFoundException {
        Long countById = repo.countById(id);

        if(countById == null || countById == 0) {
            throw new BrandNotFoundException("Could not find brand with ID: "+id);
        }
        repo.deleteById(id);
    }

    public Brand get(Integer id) throws BrandNotFoundException{
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException e){
            throw new BrandNotFoundException("No brand with ID: "+id);
        }
    }

    public String checkUnique(Integer id, String name){
        boolean isCreatingNew = (id == null || id == 0);

        Brand brandByName = repo.findByName(name);
        if(isCreatingNew){
            if(brandByName != null){
                return "Duplicate";
            }
        } else {
            if(brandByName != null && brandByName.getId() != id){
                return "Duplicate";
            }
        }
        return "OK";
    }
}
