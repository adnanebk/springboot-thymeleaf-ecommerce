//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(
        name = "orders"
)
public class Order implements Serializable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int id;
    private LocalDate OrderedDate;
    private boolean isShipped;
    private String Adresse;
    @Transient
    private String username;
    private String firstName;
    private String lastName;
    private String Country;
    private String zip;
    @OneToMany(
            mappedBy = "order",
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    private List<CartLine> cartLines = new ArrayList();
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "user_id"
    )
    private User user;

    public Order() {
    }

    public String getZip() {
        return this.zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return this.Country;
    }

    public void setCountry(String country) {
        this.Country = country;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.setOrderedDate(LocalDate.now());
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCartLine(CartLine cartLine) {
        this.cartLines.add(cartLine);
        cartLine.setOrder(this);
    }

    public void setAllCartLines(List<CartLine> cartLines) {
        this.cartLines.addAll(cartLines);
        this.cartLines.stream().peek((c) -> {
            c.setOrder(this);
        }).collect(Collectors.toList());
    }

    public LocalDate getOrderedDate() {
        return this.OrderedDate;
    }

    public void setOrderedDate(LocalDate orderedDate) {
        this.OrderedDate = orderedDate;
    }

    public boolean isShipped() {
        return this.isShipped;
    }

    public void setShipped(boolean shipped) {
        this.isShipped = shipped;
    }

    public String getAdresse() {
        return this.Adresse;
    }

    public void setAdresse(String adresse) {
        this.Adresse = adresse;
    }

    public List<CartLine> getCartLines() {
        return this.cartLines;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
