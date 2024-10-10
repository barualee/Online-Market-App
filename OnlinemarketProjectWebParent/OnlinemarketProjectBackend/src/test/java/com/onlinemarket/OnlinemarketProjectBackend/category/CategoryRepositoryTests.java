package com.onlinemarket.OnlinemarketProjectBackend.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Category;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest(showSql=false)
//run test against real DB
@AutoConfigureTestDatabase(replace = Replace.NONE)
//commit the changes to DB
@Rollback(false)
public class CategoryRepositoryTests {
    
    @Autowired
    private CategoryRepository repo;

    @Test
    public void testCreateRootCategory(){
        Category category = new Category("Computers");
        Category savedCategory = repo.save(category);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubCategory(){
        Category parent = new Category(7);

        Category subCategory = new Category("Memory",parent);
        Category savedCategory = repo.save(subCategory);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetCategories(){
        List<Category> categories = (List<Category>) repo.findAll();

        assertThat(categories.size()).isEqualTo(10);
    }

    @Test
    public void testGetChildCategory(){
        Category parent = repo.findById(2).get();
        Set<Category> children = parent.getChildren();

        assertThat(children.size()).isEqualTo(3);
    }

    @Test
    public void testGetRootCategories(){
        List<Category> parent = repo.findRootCategories(Sort.by("name").ascending());

        assertThat(parent.size()).isEqualTo(3);
    }

    @Test
    public void testGetCategoryByName(){
        String name = "Laptops";
        Category category = repo.findByName(name);
        assertThat(category.getName()).isEqualTo(name);
    }

    @Test
    public void testGetCategoryByAlias(){
        String alias = "Laptops";
        Category category = repo.findByAlias(alias);
        assertThat(category.getName()).isEqualTo(alias);
    }
}
