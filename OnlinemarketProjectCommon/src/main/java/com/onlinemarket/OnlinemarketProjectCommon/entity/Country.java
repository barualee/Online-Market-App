package com.onlinemarket.OnlinemarketProjectCommon.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "countries")
public class Country {
   
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(length=45, nullable=false)
    private String name;

    @Column(length=5, nullable=false)
    private String code;

    @OneToMany(mappedBy = "country")
    private Set<State> states;

    public Country(Integer id) {
        this.id = id;
    }

    public Country(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Country() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // public Set<State> getStates() {
    //     return states;
    // }

    // public void setStates(Set<State> states) {
    //     this.states = states;
    // }

    @Override
    public String toString() {
        return "Country [name=" + name + ", code=" + code + "]";
    }

    
}
