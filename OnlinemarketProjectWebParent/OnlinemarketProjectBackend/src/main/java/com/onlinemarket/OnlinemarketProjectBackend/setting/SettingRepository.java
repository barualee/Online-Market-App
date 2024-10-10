package com.onlinemarket.OnlinemarketProjectBackend.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Setting;
import com.onlinemarket.OnlinemarketProjectCommon.entity.SettingCategory;

//the primary key is string type hence 2nd arg.
public interface SettingRepository extends CrudRepository<Setting, String>{

    public List<Setting> findByCategory(SettingCategory category);
}
