//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.OneToOne;
import javax.servlet.http.HttpSession;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service
@Scope(
        value = "session",
        proxyMode = ScopedProxyMode.TARGET_CLASS
)
public class Cart implements Serializable {
    @OneToOne
    private User user;
    private List<CartLine> lineCollection;

    public Cart() {
        System.out.println("cartttttttttt");
        this.lineCollection = new ArrayList();
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void AddItem(Product product, int quantity, HttpSession session) {
        this.lineCollection = (List)session.getAttribute("cartlines");
        if (this.lineCollection == null) {
            this.lineCollection = new ArrayList();
        }

        Optional<CartLine> line = this.lineCollection.stream().filter((p) -> {
            return p.getProduct().getId() == product.getId();
        }).findFirst();
        if (!line.isPresent()) {
            CartLine cartline = new CartLine();
            cartline.setProduct(product);
            cartline.setQuantity(quantity);
            this.lineCollection.add(cartline);
        } else {
            ((CartLine)line.get()).setQuantity(quantity);
            ((CartLine)this.lineCollection.stream().filter((p) -> {
                return p.getProduct().getId() == product.getId();
            }).findFirst().get()).setQuantity(quantity);
        }

        session.setAttribute("cartlines", this.lineCollection);
    }

    public void RemoveLine(Product product, HttpSession session) {
        this.lineCollection = (List)session.getAttribute("cartlines");
        this.lineCollection.removeIf((l) -> {
            return l.getProduct().getId() == product.getId();
        });
        session.setAttribute("cartlines", this.lineCollection);
    }

    public double ComputeTotalValue() {
        return this.lineCollection == null ? 0.0D : this.lineCollection.stream().mapToDouble((e) -> {
            return e.getProduct().getPrice() * (double)e.getQuantity();
        }).sum();
    }

    public void Clear() {
        this.lineCollection.clear();
    }

    public List<CartLine> GetCartLines() {
        return this.lineCollection == null ? null : this.lineCollection;
    }
}
