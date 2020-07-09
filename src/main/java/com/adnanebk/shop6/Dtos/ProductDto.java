//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Dtos;

import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class ProductDto {
    @NotEmpty(message = "the name must not be empty")
    private String name;

    private int id;
    @NotEmpty(message = "the description must not be empty")
    private String description;

    @Min(value = 10,message = "the price must be greater or equal to 10")
    private double price;
    @NotEmpty(message = "Image url 1 is required")
    private String imageUrl;
    private String imageUrl2;
    private String imageUrl3;
    private String imageUrl4;
    @NotEmpty(message = "You must choose a category ")
    private String categoryName;

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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ProductDto() {
    }
}
