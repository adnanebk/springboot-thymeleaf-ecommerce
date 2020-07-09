//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Controllers;

import com.adnanebk.shop6.Models.Cart;
import com.adnanebk.shop6.Models.CartLine;
import com.adnanebk.shop6.Models.Product;
import com.adnanebk.shop6.Services.ImageService;
import com.adnanebk.shop6.Services.ProductService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class HomeController {
    private Cart cart;
    private ProductService productService;
    private ImageService imageService;

    @Autowired
    public HomeController(Cart cart, ProductService productService, ImageService imageService) {
        this.cart = cart;
        this.productService = productService;
        this.imageService = imageService;
    }


    @RequestMapping("/")
    public String Welcome(Model m, @RequestParam(defaultValue = "Newest") String sort, @RequestParam(defaultValue = "1") int page,
                          @RequestParam(required = false) String cat,@RequestParam(defaultValue = "grid") String view,
                          @RequestParam(name = "search",required = false) String prodSearch,
                          @RequestParam(defaultValue = "0") int min, @RequestParam(defaultValue = "0") int max,
                          @RequestParam(defaultValue = "6") int pageSize) {
        List<Product> products;
        products= this.productService.GetFiltredProducts(prodSearch,cat,max,min);
        int size=products.size();
        if ((page-1) * pageSize >= size) {
            page--;
        }
        m.addAttribute("CurrentPage", page);
        page--;
        if(size>0)
            products= this.productService.GetPagingAndSortingProducts(products.stream(),page, sort,pageSize);

        double pageNum = StrictMath.ceil((double)size/ pageSize);
        int[] pages =  IntStream.range(1, (int) pageNum+1).toArray();





        if(prodSearch!=null && !prodSearch.isEmpty()){
            m.addAttribute("search", prodSearch);
        }

        m.addAttribute("pageNum", pageNum);
        m.addAttribute("prodsIdsInCart", this.cart.GetCartLines().stream().map((c) -> c.getProduct().getId()).collect(Collectors.toList()));
        m.addAttribute("count", products.size());
        m.addAttribute("pages", pages);
        m.addAttribute("showCont", true);

        m.addAttribute("sort", sort);
        m.addAttribute("products", products);
        m.addAttribute("active", "home");
        m.addAttribute("categ", cat);
        m.addAttribute("min", min);
        m.addAttribute("max", max);
        m.addAttribute("view", view);
        return "index";
    }


    @GetMapping({"/item/{prod}"})
    public String GetProductDetails(Model m, @PathVariable(name = "prod") String prodName) {
        Product product = this.productService.Getproduct(prodName);
        m.addAttribute("product", product);
        m.addAttribute("total", product.getPrice());
        List<CartLine> cartlines = this.cart.GetCartLines();
        if (cartlines != null && product != null) {
            Optional<CartLine> cartLine = cartlines.stream().filter((c) -> c.getProduct().getId() == product.getId()).findFirst();
            if (cartLine.isPresent()) {
                m.addAttribute("quantity", (cartLine.get()).getQuantity());
                m.addAttribute("total", (double)(cartLine.get()).getQuantity() * product.getPrice());
            }
        }

        return "productDetails";
    }

    @GetMapping({"/products"})
    public String toString(Model m, HttpServletRequest request) {
        m.addAttribute("http", request.isSecure());
        m.addAttribute("localname", request.getLocalName());
        m.addAttribute("localadresse", request.getLocalAddr());
        m.addAttribute("active", "products");
        try {
            imageService.cleanResources(productService.GetAllProducts());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        return "productManage";
    }

}
