//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Repositories;

import com.adnanebk.shop6.Models.Category;
import com.adnanebk.shop6.Models.Product;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepo extends CrudRepository<Product, Integer> {


    @Cacheable("prod")
    default List<Product>  getAll(){
        return findAll();
    }

    Optional<Product> findByName(String name);

    List<Product> findAll();
}
