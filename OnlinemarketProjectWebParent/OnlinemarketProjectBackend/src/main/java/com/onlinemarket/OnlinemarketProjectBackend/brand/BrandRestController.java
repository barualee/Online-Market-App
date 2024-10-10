package com.onlinemarket.OnlinemarketProjectBackend.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Brand;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Category;


@RestController
public class BrandRestController {
    @Autowired
    private BrandService service;

    @PostMapping("/brands/check_unique")
    public String checkUnique(@Param("id") Integer id, @Param("name") String name, @Param("alias") String alias) {
        return service.checkUnique(id, name);
    }

    @GetMapping("/brands/{id}/categories")
    public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws BrandNotFoundRestException {
        List<CategoryDTO> listCat = new ArrayList<>();
        try{
            Brand brand = service.get(brandId);
            Set<Category> categories = brand.getCategories();
            for(Category cat : categories){
                CategoryDTO dto = new CategoryDTO(cat.getId(), cat.getName());
                listCat.add(dto);
            }
            return listCat;
        } catch (BrandNotFoundException e){
            throw new BrandNotFoundRestException();
        }
    }

}
