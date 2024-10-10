package com.onlinemarket.OnlinemarketProjectCommon.entity;

public class StateDTO {
    private String name;
    private Integer id;
    public StateDTO(String name, Integer id) {
        this.name = name;
        this.id = id;
    }
    public StateDTO() {
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    

}

