//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Category implements Serializable {
    @Id
    private String name;
    private String description;
    @OneToMany(
            mappedBy = "category",
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    private List<Product> products;

    public List<Product> getProducts() {
        return this.products;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category() {
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
