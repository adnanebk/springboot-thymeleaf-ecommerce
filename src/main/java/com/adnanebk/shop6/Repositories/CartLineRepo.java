//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Repositories;

import com.adnanebk.shop6.Models.CartLine;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CartLineRepo extends CrudRepository<CartLine, Integer> {
    List<CartLine> findAll();
}
