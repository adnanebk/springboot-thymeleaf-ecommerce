//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Controllers;

import com.adnanebk.shop6.Dtos.ProductDto;
import com.adnanebk.shop6.Dtos.ProductsPageDto;
import com.adnanebk.shop6.Models.Category;
import com.adnanebk.shop6.Models.Product;
import com.adnanebk.shop6.Services.ImageService;
import com.adnanebk.shop6.Services.ProductService;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping({"/productApi"})
public class ApiProductController {

    @Autowired
    ResourceLoader resourceLoader;
    private ProductService productService;
    //private ImageService imageService;
    private boolean start = true;
    //private CompletableFuture future;
    //private Thread worker;
    //private ExecutorService executors= Executors.newSingleThreadExecutor();




    @Autowired
    public ApiProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/Product/{id}"})
    public ResponseEntity<?> getProductById(@PathVariable("id") int id) {
        Optional<Product> Product = this.productService.GetAllProducts().stream().filter((p) -> p.getId() == id).findFirst();
        return !Product.isPresent() ?  ResponseEntity.notFound().build() :ResponseEntity.ok(Product.get());
    }

    @PostMapping({"/create"})
    public ResponseEntity<?> create(@RequestBody @Valid ProductDto productDto, UriComponentsBuilder ucBuilder) throws ParseException {
        System.out.println("cccc");
        Product product = this.productService.addAndConvertToEntity(productDto);
        //HttpHeaders headers = new HttpHeaders();
        //headers.setLocation(ucBuilder.path("/Product/{id}").buildAndExpand(product.getId()).toUri());
        return  ResponseEntity.created(ucBuilder.path("/Product/{id}").buildAndExpand(product.getId()).toUri()).build();
    }

    @PostMapping({"/image/create"})
    public void createImages(@RequestParam("files") MultipartFile[] images) {

        //if (this.start) {
            //this.start = false;

                try {
                    productService.CreateProductImages(images);
                } catch (IOException | InterruptedException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
                }
        //}
     /*   if (this.start) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            this.start = true;
        }*/
    }


    @GetMapping({"/products"})
    public ProductsPageDto getAllProducts(@RequestParam(defaultValue = "0") int page) {
        this.start = true;
    return productService.getAllProductsInPage(page);
    }

    @PutMapping({"/update"})
    public ResponseEntity<?> updateProduct(@RequestBody @Valid ProductDto productDto) throws ParseException {
       this.productService.addAndConvertToEntity(productDto);
        return  ResponseEntity.ok().build();
    }

    @DeleteMapping(
            value = {"/delete/{id}"},
            headers = {"Accept=application/json"}
    )
    public ResponseEntity<?> deleteProduct(@PathVariable("id") int id) {
        Optional<Product> Product = this.productService.GetAllProducts().stream().filter((p) -> p.getId() == id).findFirst();
        if (!Product.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            this.productService.RemoveProductById(id);
            return  ResponseEntity.noContent().build();
        }
    }

    @PostMapping({"/category/create"})
    public ResponseEntity<Category> createCategory(@RequestBody @Valid Category cat) throws ParseException {
        this.productService.AddNewCategory(cat);
        return new ResponseEntity(cat, HttpStatus.CREATED);
    }

    @GetMapping({"/cancelOperation"})
    public void cancelUploading()
    {
        //this.start = true;
        //future.cancel(true);
    }


    public void cleanResources(HttpServletResponse response) throws IOException {

           //imageService.cleanResources(this.productService.GetAllProducts());
        response.sendRedirect("/products");
    }

}
