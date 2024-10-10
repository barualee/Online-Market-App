package com.onlinemarket.OnlinemarketProjectCommon.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="categories")
public class Category {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(length=128, nullable=false, unique=true)
    private String name;

    @Column(length=64, nullable=false, unique=true)
    private String alias;

    @Column(length=128, nullable=false)
    private String image;

    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new HashSet<>();

    @Transient
    private boolean hasChildren;

    public Category(Integer id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    public static Category copyIdAndName(Category category) {
        Category copCategory = new Category();
        copCategory.setId(category.getId());
        copCategory.setName(category.getName());

        return copCategory;
    }

    public static Category copyIdAndName(String name, Integer id) {
        Category copCategory = new Category();
        copCategory.setId(id);
        copCategory.setName(name);

        return copCategory;
    }

    public static Category copyFull(Category category) {
        Category copCategory = new Category();
        copCategory.setId(category.getId());
        copCategory.setName(category.getName());
        copCategory.setAlias(category.getAlias());
        copCategory.setEnabled(category.isEnabled());
        copCategory.setImage(category.getImage());
        copCategory.setHasChildren(category.getChildren().size() > 0);

        return copCategory;
    }

    public static Category copyFull(Category category, String name) {
        Category copCategory = Category.copyFull(category);
        copCategory.setName(name);
        
        return copCategory;
    }

    public Category(String name) {
        this.name = name;
        this.alias = name;
        this.image = "default.png";
    }

    public Category(Integer id) {
        this.id = id;
    }

    public Category(String name, Category parent) {
        this(name);
        this.parent = parent;
    }

    public Category() {
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }

    @Transient
    public String getImagePath(){
        if(this.id == null || this.image.equals("")){
            return "/images/default_image_thumbnail.png";
        }
        return "/category-photos/"+this.id+"/"+this.image;
    }

    public boolean isHasChildren(){
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren){
        this.hasChildren = hasChildren;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
