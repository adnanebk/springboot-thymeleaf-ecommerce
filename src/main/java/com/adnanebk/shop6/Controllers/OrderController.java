//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Controllers;

import com.adnanebk.shop6.Services.Cart;
import com.adnanebk.shop6.Models.CartLine;
import com.adnanebk.shop6.Models.Order;
import com.adnanebk.shop6.Models.Product;
import com.adnanebk.shop6.Models.User;
import com.adnanebk.shop6.Repositories.CartLineRepo;
import com.adnanebk.shop6.Repositories.OrderRepo;
import com.adnanebk.shop6.Repositories.UserRepo;
import com.adnanebk.shop6.Services.ProductService;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {
    private UserRepo userRepo;
    private CartLineRepo cartLineRepo;
    private OrderRepo orderRepo;
    private Cart cart;
    private ProductService productService;
    CartLine cartLine;
    List<CartLine> cartLines;

    @Autowired
    public OrderController(UserRepo userRepo, CartLineRepo cartLineRepo, OrderRepo orderRepo, Cart cart, ProductService productService) {
        this.userRepo = userRepo;
        this.cartLineRepo = cartLineRepo;
        this.orderRepo = orderRepo;
        this.cart = cart;
        this.productService = productService;
    }

    @GetMapping("/orders")
    public String Myorders(HttpServletRequest request) {
       // boolean istrue=SecurityContextHolder.getContext().getAuthentication().isAuthenticated();

        Object attr = request.getAttribute("username");
        String username = (String) request.getAttribute("username");
        if (username != null) {
            User user = this.userRepo.findByUsername(username);
            if (user != null) {
                List<CartLine> cartlines = user.getOrders().stream().flatMap((o) -> o.getCartLines().stream()).collect(Collectors.toList());
                if (cartlines.size() > 0) {
                    request.setAttribute("cartlines", cartlines);
                    request.setAttribute("total", cartlines.stream().mapToDouble((c) -> (double)c.getQuantity() * c.getProduct().getPrice()).sum());
                }

            }

        }
        request.setAttribute("active", "order");
        return "orders";
    }

    @GetMapping({"/cart/checkout/"})
    public String CartCheckout() {
        return "checkout";
    }

    @GetMapping({"/checkout"})
    public String Checkout( Model m, Order order, @RequestParam(value = "prod",required = false) String productName, @RequestParam(value = "qt",required = false) String qt) {
            if (productName != null && qt != null) {
                Product product = this.productService.GetAllProducts().stream().filter((p) -> p.getName().equals(productName)).findFirst().orElse(null);
                if (product != null) {
                    m.addAttribute("product", product);
                    m.addAttribute("total", product.getPrice() * (double)Integer.parseInt(qt));
                    this.cartLine = new CartLine();
                    this.cartLine.setQuantity(Integer.parseInt(qt));
                    this.cartLine.setProduct(product);
                }
            } else {
                this.cartLines = this.cart.GetCartLines();
                m.addAttribute("cartLines", this.cartLines);
                m.addAttribute("total", this.cart.ComputeTotalValue());
                m.addAttribute("NumberOfCartLines", this.cartLines.size());
            }

            m.addAttribute("order", order);
            return "checkout";

    }

    @PostMapping({"/checkout"})
    public String SaveOrders(@Valid Order order, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "checkout";

        }
           String userName = order.getUsername();
        User user = this.userRepo.findByUsername(userName);


        order.setUser(user);
            if (this.cartLine != null) {
                CartLine SavedCartline = this.cartLineRepo.save(this.cartLine);
                order.setCartLine(SavedCartline);
                this.orderRepo.save(order);
                this.cartLine = null;
            } else {
                if (this.cartLines != null) {
                    List<CartLine> cartLineList =  this.cartLineRepo.saveAll(this.cartLines);
                    order.setAllCartLines(cartLineList);
                    this.orderRepo.save(order);
                    session.removeAttribute("cartlines");
                }

                this.cart.Clear();
            }

            return "redirect:/orders";
    }

    @PostMapping(
            value = {"/order"},
            params = {"remove"}
    )
    public String RemoveOrder(@RequestParam(name = "orderId") Integer orderId, @RequestParam(name = "lineId") Integer lineId) {
        this.cartLineRepo.deleteById(lineId);
        if (this.orderRepo.findById(orderId).get().getCartLines() == null || this.orderRepo.findById(orderId).get().getCartLines().size() == 0) {
            this.orderRepo.deleteById(orderId);
        }

        return "redirect:/orders";
    }
}
