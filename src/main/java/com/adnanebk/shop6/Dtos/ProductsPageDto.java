//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Dtos;

import java.util.List;

public class ProductsPageDto {
    private List<ProductDto> productsDto;
    private List<String> categories;
    private int prodCount;
    private int prodLimit;

    public ProductsPageDto() {
    }

    public List<ProductDto> getProductsDto() {
        return this.productsDto;
    }

    public void setProductsDto(List<ProductDto> productsDto) {
        this.productsDto = productsDto;
    }

    public List<String> getCategories() {
        return this.categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public int getProdCount() {
        return this.prodCount;
    }

    public void setProdCount(int prodCount) {
        this.prodCount = prodCount;
    }

    public int getProdLimit() {
        return this.prodLimit;
    }

    public void setProdLimit(int prodLimit) {
        this.prodLimit = prodLimit;
    }
}
