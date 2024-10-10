package com.onlinemarket.OnlinemarketProjectFrontend.setting;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Setting;
import com.onlinemarket.OnlinemarketProjectCommon.entity.SettingCategory;

@DataJpaTest(showSql=false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTests {
    @Autowired
    SettingRepository repo;

    @Test
    public void testFindByTwoCategories() {
        List<Setting> settings = repo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
        assertThat(settings.size()).isGreaterThan(0);
    }
}
