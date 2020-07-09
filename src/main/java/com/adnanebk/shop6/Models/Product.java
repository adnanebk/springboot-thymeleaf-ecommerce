//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

@Entity
public class Product implements Serializable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int id;
    private String name;
    @Column(
            length = 800
    )
    private String description;
    private double price;
    private String imageUrl;
    private String imageUrl2;
    private String imageUrl3;
    private String imageUrl4;
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "category_id"
    )
    private Category category;
    @OneToMany(
            mappedBy = "product",
            cascade = {CascadeType.ALL}
    )
    private List<CartLine> cartLines = new ArrayList();

    public Product() {
    }

    public void setCartLines(List<CartLine> cartLines) {
        this.cartLines = cartLines;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CartLine> getCartLines() {
        return this.cartLines;
    }

    public void setCartLine(CartLine cartLines) {
        this.cartLines.add(cartLines);
    }

    public String getImageUrl2() {
        return this.imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

    public String getImageUrl3() {
        return this.imageUrl3;
    }

    public void setImageUrl3(String imageUrl3) {
        this.imageUrl3 = imageUrl3;
    }

    public String getImageUrl4() {
        return this.imageUrl4;
    }

    public void setImageUrl4(String imageUrl4) {
        this.imageUrl4 = imageUrl4;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
