//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Repositories;

import com.adnanebk.shop6.Models.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, String> {
    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAll();
}
