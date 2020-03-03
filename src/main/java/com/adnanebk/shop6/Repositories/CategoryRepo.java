//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Repositories;

import com.adnanebk.shop6.Models.Category;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepo extends CrudRepository<Category, String> {

    @Cacheable("cat")
     default List<Category>  getAll(){
        return findAll();
    }

    List<Category> findAll();
}
