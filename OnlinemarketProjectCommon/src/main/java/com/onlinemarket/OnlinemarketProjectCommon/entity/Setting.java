package com.onlinemarket.OnlinemarketProjectCommon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="settings")
public class Setting {
    
    @Id
    //we need backticks here as key is a reserve word in SQL
    @Column(name = "`key`",nullable = false, length = 128)
	private String key;

    @Column(nullable = false, length = 1024)
	private String value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 45)
    private SettingCategory category;

    public Setting(String key) {
        this.key = key;
    }

    public Setting(String key, String value, SettingCategory category) {
        this.key = key;
        this.value = value;
        this.category = category;
    }

    //JPA hibernate needs blank constructor
    public Setting() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SettingCategory getCategory() {
        return category;
    }

    public void setCategory(SettingCategory category) {
        this.category = category;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Setting other = (Setting) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Setting [key=" + key + ", value=" + value + "]";
    }
}
