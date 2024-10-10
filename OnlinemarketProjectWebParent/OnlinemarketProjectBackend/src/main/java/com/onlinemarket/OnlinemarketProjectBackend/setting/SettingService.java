package com.onlinemarket.OnlinemarketProjectBackend.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Setting;
import com.onlinemarket.OnlinemarketProjectCommon.entity.SettingCategory;

@Service
public class SettingService {
    @Autowired
    private SettingRepository repo;

    public List<Setting> listAllSettings(){
        return (List<Setting>) repo.findAll();
    }

    public GeneralSettingBag getGeneralSettings(){
        List<Setting> settings = new ArrayList<>();

        List<Setting> generalSettings = repo.findByCategory(SettingCategory.GENERAL);
        List<Setting> currencySettings = repo.findByCategory(SettingCategory.CURRENCY);

        settings.addAll(currencySettings);
        settings.addAll(generalSettings);

        return new GeneralSettingBag(settings);
    }

    public void saveAll(Iterable<Setting> settings){
        repo.saveAll(settings);
    }

    public List<Setting> getMailServerSettings(){
        return repo.findByCategory(SettingCategory.MAIL_SERVER);
    }

    public List<Setting> getMailTemplatesSettings(){
        return repo.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }
}
