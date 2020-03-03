//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class CartLine implements Serializable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int id = 0;
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "prod_id"
    )
    private Product product;
    @Transient
    private Cart cart;
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "order_id"
    )
    private Order order;
    private int quantity;

    public CartLine() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Cart getCart() {
        return this.cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
