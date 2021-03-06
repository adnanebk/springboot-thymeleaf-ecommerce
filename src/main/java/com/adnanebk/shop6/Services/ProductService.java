//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Services;

import com.adnanebk.shop6.Dtos.ProductDto;
import com.adnanebk.shop6.Dtos.ProductsPageDto;
import com.adnanebk.shop6.Models.Category;
import com.adnanebk.shop6.Models.Product;
import com.adnanebk.shop6.Repositories.CategoryRepo;
import com.adnanebk.shop6.Repositories.ProductRepo;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hibernate.annotations.Cache;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

    private ProductRepo productRepo;
    private ImageService imageService;
    private ModelMapper modelMapper;
    private CategoryRepo categoryRepo;


    public ProductService(ProductRepo productRepo, CategoryRepo categoryRepo,ImageService imageService, ModelMapper modelMapper) {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.imageService = imageService;
        this.modelMapper = modelMapper;
    }

    public List<Product> GetAllProducts() {
        return  this.productRepo.getAll();
    }




    public List<Category> GetAllCategories() {
        return this.categoryRepo.getAll();
    }


    @CachePut("prod")
    public List<Product> AddNewProduct(Product product) {
        List<Product> products=productRepo.getAll();
        boolean match=productRepo.getAll().stream().anyMatch((p) -> p.getId() == product.getId());

        if(!match)
        {
            products.add(0,this.productRepo.save(product));
        }
        else {
            for (Product p : products) {
                if (p.getId() == product.getId())
                    products.set(products.indexOf(p), productRepo.save(product));
            }
        }
        return products;
    }

    @CachePut("cat")
    public List<Category> AddNewCategory(Category cat) {
        if(categoryRepo.existsById(cat.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"this category is already exist");
         List<Category> categories=categoryRepo.getAll();
             categories.add(this.categoryRepo.save(cat));
             return categories;
    }


    @CachePut("prod")
    public List<Product> RemoveProduct(int id) {
        this.productRepo.deleteById(id);
        List<Product> products=this.productRepo.getAll();
        products.removeIf(p -> p.getId() == id);

       return products;
    }

    public Product Getproduct(String prodName) {
        return this.productRepo.getAll().stream().filter((p) -> p.getName().equals(prodName)).findFirst().orElse(null);
    }




    public List<Product> GetPagingAndSortingProducts(Stream<Product> products,int page, String sort, int pageSize) {


        Comparator<Product> comparator =
                sort.equalsIgnoreCase("name") ? Comparator.comparing(Product::getName):
                        sort.equalsIgnoreCase("lowprice") ?Comparator.comparing(Product::getPrice):
                                sort.equalsIgnoreCase("highprice") ? Comparator.comparing(Product::getPrice).reversed():null;

        if (comparator == null)
            return products.skip((page * pageSize)).limit(pageSize).collect(Collectors.toList());

        return products.sorted(comparator).skip((page * pageSize)).limit(pageSize).collect(Collectors.toList());

    }


    public List<Product> GetFiltredProducts(String search, String cat,int max,int min) {
        Stream<Product> filtredProds=this.productRepo.getAll().stream();
        if(search!=null && !search.isEmpty())
            filtredProds=filtredProds.filter(p->p.getName().toLowerCase().contains(search.toLowerCase()));
        if(max>0)
            filtredProds=filtredProds.filter((p) -> p.getPrice() >= (double)min && p.getPrice() <= (double)max);
        if(cat!=null && !cat.isEmpty())
        return  filtredProds.filter((p) -> p.getCategory().getName().equals(cat)).collect(Collectors.toList());
        else
        return filtredProds.collect(Collectors.toList());
    }

    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        productDto.setCategoryName(product.getCategory().getName());
        return productDto;
    }


    public Product addAndConvertToEntity(ProductDto productDto) throws ParseException {
        Product product = modelMapper.map(productDto, Product.class);
        Category category = GetAllCategories().stream().filter((c) -> c.getName().equals(productDto.getCategoryName())).findFirst().get();
        product.setCategory(category);
        String i1=imageService.HandleImage(product.getImageUrl(), "Image url 1");
        String i2=imageService.HandleImage(product.getImageUrl2(), "Image url 2");
        String i3=imageService.HandleImage(product.getImageUrl3(), "Image url 3");
        String i4=imageService.HandleImage(product.getImageUrl4(), "Image url 4");
        product.setImageUrl(i1);
        product.setImageUrl2(i2);
        product.setImageUrl3(i3);
        product.setImageUrl4(i4);
         AddNewProduct(product);
         return product;
    }


    public ProductsPageDto getAllProductsInPage(int page){
        List<ProductDto> productsDto = this.productRepo.getAll().stream().map((p) -> convertToDto(p)).skip((page * 5)).limit(5).collect(Collectors.toList());
        ProductsPageDto productsPageDto = new ProductsPageDto();
        productsPageDto.setProductsDto(productsDto);
        productsPageDto.setCategories(GetAllCategories().stream().map((c) -> c.getName()).collect(Collectors.toList()));
        productsPageDto.setProdCount(GetAllProducts().size());
        productsPageDto.setProdLimit(5);
         return productsPageDto;
    }




    public void CreateProductImages(MultipartFile[] images) {

        if(images.length>0) {
            try {
                imageService.CreateImages(images);
            } catch (IOException e) {
                throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());

            }
        }
    }


}
