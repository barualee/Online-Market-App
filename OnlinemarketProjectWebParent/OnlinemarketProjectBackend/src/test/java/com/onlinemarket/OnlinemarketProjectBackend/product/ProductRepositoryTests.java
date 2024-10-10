package com.onlinemarket.OnlinemarketProjectBackend.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Brand;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Category;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Product;


@DataJpaTest(showSql=false)
//run test against real DB
@AutoConfigureTestDatabase(replace = Replace.NONE)
//commit the changes to DB
@Rollback(false)
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository repo;

    @Autowired
	private TestEntityManager entityManager;

    @Test
    public void testCreateProduct(){
        Brand brand = entityManager.find(Brand.class, 3);

        Category category = entityManager.find(Category.class, 6);

        Product product = new Product();
        product.setName("Galaxy S24 Ultra");
        product.setAlias("Galaxy S24 Ultra");
        product.setPrice(2500);
        product.setBrand(brand);
        product.setCategory(category);
        product.setCreatedTime(new Date());

        Product saved = repo.save(product);
        assertThat(saved.getId()).isGreaterThan(0);
    }
    
    @Test
    public void testListAll(){
        List<Product> list = (List<Product>) repo.findAll();
        assertThat(list.size()).isEqualTo(3);
    }

    @Test
    public void testGetProduct(){
        Optional<Product> product = repo.findById(1);
        assertThat(product).isNotNull();
    }

    @Test
    public void testUpdateProduct(){
        Product product = repo.findById(3).get();
        product.setPrice(350);
        repo.save(product);

        Product updated = entityManager.find(Product.class,3);
        assertThat(updated.getPrice()).isEqualTo(350);
    }

    @Test
    public void testDeleteById(){
        repo.deleteById(3);
        Optional<Product> result = repo.findById(3); 
        assertThat(result.isEmpty());
    }

    // @Test
    // public void testSaveProductWithDetails(){
    //     Integer productId = 1;
    //     Product product = repo.findById(productId).get();

    //     product.addDetail("Device Memory","128GB");

    //     Product saved = repo.save(product);
    //     assertThat(saved.getDetails()).isNotEmpty();
    // }

    @Test
    public void testSaveProductWithImages(){
        Integer productId = 1;
        Product product = repo.findById(productId).get();

        product.setMainImage("main_image.jpg");
        product.addExtraImages("image1.jpg");
        product.addExtraImages("image2.jpg");

        Product saved = repo.save(product);
        assertThat(saved.getImages().size()).isEqualTo(2);
    }
}
