//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Services;

import com.adnanebk.shop6.Models.Cart;
import com.adnanebk.shop6.Models.CartLine;
import com.adnanebk.shop6.Models.Category;
import com.adnanebk.shop6.Models.SocialUser;
import com.adnanebk.shop6.Repositories.CategoryRepo;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class MyInterceptor implements HandlerInterceptor {
    private Cart cart;
    private CategoryRepo categoryRepo;

    @Autowired
    public MyInterceptor(Cart cart, CategoryRepo categoryRepo) {
        this.cart = cart;
        this.categoryRepo = categoryRepo;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("interceptor");
        response.addHeader("strict-transport-security", "max-age=31622400; includeSubDomains");
        //response.addHeader("Cache-Control", CacheControl.noCache().getHeaderValue());
        if (request.getMethod().equals("GET") && response.getStatus() == 200) {

            modelAndView.getModel().put("categories", this.categoryRepo.getAll());
            List<CartLine> cartlines = this.cart.GetCartLines();
            if (cartlines != null) {
                modelAndView.addObject("NumberOfCartLines", cartlines.size());
            }

            modelAndView.addObject("msg", "Welcome to My World!");
            SocialUser user = (SocialUser)request.getSession().getAttribute("socialuser");
            if (user != null) {
                modelAndView.addObject("username", user.getName());
            } else if (request.getUserPrincipal() != null) {
                modelAndView.addObject("username", request.getUserPrincipal().getName());
            }

        /*    String view = (String)request.getSession().getAttribute("view");
            if (view == null) {
                modelAndView.addObject("view", "grid");
            } else {
                modelAndView.addObject("view", view);
            }*/
        }

    }

}
