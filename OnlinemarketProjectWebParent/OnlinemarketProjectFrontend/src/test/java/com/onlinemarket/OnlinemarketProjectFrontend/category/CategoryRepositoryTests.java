package com.onlinemarket.OnlinemarketProjectFrontend.category;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

@DataJpaTest(showSql=false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository repo;

    @Test
    public void testListEnabledCategories(){
        List<Category> categories = repo.findAllEnabled();
        assertThat(categories.size()).isEqualTo(4);
    }

    @Test
    public void testFindCategoryByAlias(){
        String alias = "books";
        Category category = repo.findByAliasEnabled(alias);
        assertThat(category).isNotNull();
    }
}
