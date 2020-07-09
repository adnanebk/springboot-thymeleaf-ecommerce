//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Controllers;

import com.adnanebk.shop6.Dtos.ProductDto;
import com.adnanebk.shop6.Dtos.ProductsPageDto;
import com.adnanebk.shop6.Models.Category;
import com.adnanebk.shop6.Models.Product;
import com.adnanebk.shop6.Services.ProductService;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping({"/api"})
@CrossOrigin
public class ApiProductController {


    private ProductService productService;





    @Autowired
    public ApiProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/products/{id}"})
    public ResponseEntity<?> getProductById(@PathVariable("id") int id) {
        Optional<Product> Product = this.productService.GetAllProducts().stream().filter((p) -> p.getId() == id).findFirst();
        return !Product.isPresent() ?  ResponseEntity.notFound().build() :ResponseEntity.ok(Product.get());
    }

    @PostMapping({"/products"})
    public ResponseEntity<?> create(@RequestBody @Valid ProductDto productDto, UriComponentsBuilder ucBuilder)  {
        System.out.println("cccc");
        Product product = null;
        try {
            product = this.productService.addAndConvertToEntity(productDto);
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        //HttpHeaders headers = new HttpHeaders();
        //headers.setLocation(ucBuilder.path("/Product/{id}").buildAndExpand(product.getId()).toUri());
        return  ResponseEntity.created(ucBuilder.path("/Product/{id}").buildAndExpand(product.getId()).toUri()).build();
    }

    @PostMapping({"/images"})
    public Callable<ResponseEntity<?>> createImages(@RequestParam("files") MultipartFile[] images) {


       return    ()-> {
            productService.CreateProductImages(images);
            return ResponseEntity.ok().build();
        };


    }


    @GetMapping({"/products"})
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "0") int page) {
        //this.start = true;
    return ResponseEntity.ok(productService.getAllProductsInPage(page));
    }

    @PutMapping({"/products"})
    public ResponseEntity<?> updateProduct( @Valid @RequestBody ProductDto productDto) throws ParseException {
       this.productService.addAndConvertToEntity(productDto);
        return  ResponseEntity.ok().build();
    }

    @DeleteMapping(
            value = {"/products/{id}"},
            headers = {"Accept=application/json"}
    )
    public ResponseEntity<?> deleteProduct(@PathVariable("id") int id) {
        Product Product = this.productService.GetAllProducts().stream().filter((p) -> p.getId() == id).findFirst().orElse(null);
        if (Product==null) {
            return ResponseEntity.notFound().build();
        } else {
            this.productService.RemoveProduct(id);
            return  ResponseEntity.noContent().build();
        }
    }

    @PostMapping({"/categories"})
    public ResponseEntity<Category> createCategory(@RequestBody @Valid Category cat) throws ParseException {
        this.productService.AddNewCategory(cat);
        return new ResponseEntity(cat, HttpStatus.CREATED);
    }


}
