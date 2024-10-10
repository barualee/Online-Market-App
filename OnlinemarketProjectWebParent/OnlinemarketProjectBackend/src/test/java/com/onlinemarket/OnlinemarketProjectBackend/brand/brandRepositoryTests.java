package com.onlinemarket.OnlinemarketProjectBackend.brand;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Brand;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Category;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest(showSql=false)
//run test against real DB
@AutoConfigureTestDatabase(replace = Replace.NONE)
//commit the changes to DB
@Rollback(false)
public class brandRepositoryTests {

    @Autowired
    private BrandRepository repo;

    @Test
    public void testCreateBrand(){
        Brand brand = new Brand("Apple");
        Category laptop = new Category(3);
        
        brand.getCategories().add(laptop);
        Brand savedBrand = repo.save(brand);

        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateBrand2(){
        Brand brand = new Brand("Acer");
        Category smartphones = new Category(3);
        
        brand.getCategories().add(smartphones);
        Brand savedBrand = repo.save(brand);

        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testListBrands(){
        Iterable<Brand> brands = repo.findAll();
        assertThat(brands).isNotEmpty();
    }

    @Test
    public void testGetById(){
        Brand brand = repo.findById(1).get();
        assertThat(brand.getName()).isEqualTo("Apple");
    }
    
    @Test
    public void testUpdateName(){
        Brand brand = repo.findById(3).get();
        brand.setName("Samsung Electronics");
        Brand saved = repo.save(brand);

        assertThat(saved.getName()).isEqualTo("Samsung Electronics");
    }

    @Test
    public void testDeleteById(){
        repo.deleteById(5);
        Optional<Brand> result = repo.findById(4); 
        assertThat(result.isEmpty());
    }

    @Test
    public void testFindAll(){
        Iterable<Brand> list = repo.findAll();
        assertThat(list).isNotEmpty();
    }
}
