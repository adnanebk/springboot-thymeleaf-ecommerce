//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Services;

import com.adnanebk.shop6.Models.User;
import com.adnanebk.shop6.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("UserDetailsServiceImp1")
public class UserDetailsServiceImp implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    public UserDetailsServiceImp() {
    }

    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = this.userRepo.findByUsername(s);
        if (user == null) {
            user = this.userRepo.findByEmail(s);
            if (user == null) {
                throw new UsernameNotFoundException("User Not found");
            }
        }

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername()).password(user.getPassword()).roles(new String[]{user.getRoles()}).build();
    }
}
