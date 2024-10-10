package com.onlinemarket.OnlinemarketProjectBackend.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Setting;
import com.onlinemarket.OnlinemarketProjectCommon.entity.SettingCategory;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

//
@DataJpaTest(showSql=false)
//run test against real DB
@AutoConfigureTestDatabase(replace = Replace.NONE)
//commit the changes to DB
@Rollback(false)
public class SettingRepositoryTests {

    @Autowired
	private SettingRepository repo;

    //to create the DB table, run the empty test without any statements.
    @Test
    public void testGeneralSetting(){
        Setting siteName = new Setting("SITE_NAME", "OnlineMarket", SettingCategory.GENERAL);
        Setting saved = repo.save(siteName);
        assertThat(saved).isNotNull();
    }

    @Test
    public void testRestGeneralSetting(){
        Setting siteLogo = new Setting("SITE_LOGO", "sitelogo.png", SettingCategory.GENERAL);
        Setting copyRight = new Setting("COPY_RIGHT", "Copyright (C) 2024 Onlinemarket Ltd.", SettingCategory.GENERAL);

        repo.saveAll(List.of(siteLogo, copyRight));

        Iterable<Setting> iter = repo.findAll();
        assertThat(iter).size().isGreaterThan(0);
    }

    @Test
    public void testCurrencySetting(){
        Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
        Setting symbol = new Setting("CURRENCY_SYMBOL", "$", SettingCategory.CURRENCY);
        Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY);
        Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
        Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY);
        Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY);

        repo.saveAll(List.of(currencyId, symbol, symbolPosition, decimalPointType, decimalDigits, thousandsPointType));

        Iterable<Setting> iter = repo.findAll();
        assertThat(iter).size().isGreaterThan(0);
    }

    @Test
    public void testListSettingByCategory() {
        List<Setting> settings = repo.findByCategory(SettingCategory.GENERAL);
        settings.forEach(System.out::println);
    }
}
