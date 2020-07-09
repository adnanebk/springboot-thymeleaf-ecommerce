//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Controllers;
import com.adnanebk.shop6.Models.Cart;
import com.adnanebk.shop6.Models.CartLine;
import com.adnanebk.shop6.Models.Product;
import com.adnanebk.shop6.Repositories.ProductRepo;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CartController {
    private ProductRepo productRepo;
    private Cart cart;

    @Autowired
    public CartController(ProductRepo productRepo, Cart cart) {
        this.productRepo = productRepo;
        this.cart = cart;
    }

    @GetMapping({"/cart"})
    public String ShowCart(Model m) {
        m.addAttribute("cartlines", this.cart.GetCartLines());
        m.addAttribute("total", this.cart.ComputeTotalValue());
        m.addAttribute("active", "cart");
        return "cart2";
    }



    @PostMapping(
            value = {"/cart/{prod}"},
            params = {"remove"}
    )
    public String RemoveCartLine2(@PathVariable(name = "prod") String prodName, HttpSession session) {
        Optional<Product> product = this.productRepo.getAll().stream().filter(p->p.getName().equalsIgnoreCase(prodName)).findFirst();
        this.cart.RemoveLine(product.get(), session);
        return "redirect:/cart";
    }

    @PostMapping(
            value = {"/cart/{prod}"},
            params = {"edit"}
    )
    public String EditCartLine2(@PathVariable(name = "prod") String prodName, HttpSession session,@RequestParam(defaultValue = "1") int quantity) {
        Optional<Product> product = this.productRepo.getAll().stream().filter(p->p.getName().equalsIgnoreCase(prodName)).findFirst();
        this.cart.AddItem(product.get(), quantity, session);
        return "redirect:/cart";
    }

    @PostMapping({"/api/item"})
    @ResponseBody
    public ResponseEntity<?> AddOrUpdateCartLineApi(@RequestBody String prodName, HttpSession session, @RequestParam(value = "quantity",defaultValue = "1") int quantity) {
        Optional<Product> product = this.productRepo.getAll().stream().filter(p->p.getName().equalsIgnoreCase(prodName)).findFirst();
        List<CartLine> cartlines = this.cart.GetCartLines();
        if (cartlines.stream().anyMatch((c) -> c.getProduct().getName().equals(prodName))) {
            this.cart.RemoveLine(product.get(), session);
        } else {
            this.cart.AddItem(product.get(), quantity, session);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping({"/api/item/remove/{prod}"})
    @ResponseBody
    public ResponseEntity<?> RemoveCartLineApi(@PathVariable("prod") String prodName, HttpSession session) {
        Optional<Product> product = this.productRepo.getAll().stream().filter(p->p.getName().equalsIgnoreCase(prodName)).findFirst();
        product.ifPresent(value -> this.cart.RemoveLine(value, session));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping({"/api/testpost"})
    @ResponseBody
    public String testpost(){

        return "worked";
    }
}
