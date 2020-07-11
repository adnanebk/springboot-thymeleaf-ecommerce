//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Models;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.adnanebk.shop6.Services.Cart;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(
        name = "Users"
)
public class User {
    public static final BCryptPasswordEncoder _passwordEncoder = new BCryptPasswordEncoder();
    @Id
    @NotEmpty(
            message = "The username is required"
    )
    @Length(
            min = 2,
            message = "You must have more than one character"
    )
    @Column(
            unique = true
    )
    private String username;
    private String password;
    @NotEmpty(
            message = "the email is required"
    )
    @Length(
            min = 2,
            message = "You must have more than one character"
    )
    @Email(
            message = "The Email field does not have a correct format"
    )
    @Column(
            unique = true
    )
    private String email;
    private String roles;
    @Transient
    private String MatchPassword;
    @OneToMany(
            mappedBy = "user",
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    private List<Order> orders;
    @OneToOne
    @Transient
    private Cart cart;

    public List<Order> getOrders() {
        return this.orders;
    }

    public User() {
    }

    public User( String username,  String email) {
        this.username = username;
        this.email = email;
    }

    public User(String username, String password, String roles) {
        this.username = username;
        this.password = _passwordEncoder.encode(password);
        this.roles = roles;
    }

    public String getMatchPassword() {
        return this.MatchPassword;
    }

    public void setMatchPassword(String matchPassword) {
        this.MatchPassword = matchPassword;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoles() {
        return this.roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = _passwordEncoder.encode(password);
    }
}
