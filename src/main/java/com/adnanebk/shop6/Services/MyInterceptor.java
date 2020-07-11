//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Services;

import com.adnanebk.shop6.Models.CartLine;
import com.adnanebk.shop6.Repositories.CategoryRepo;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MyInterceptor implements HandlerInterceptor {
    private Cart cart;
    private CategoryRepo categoryRepo;

    @Autowired
    public MyInterceptor(Cart cart, CategoryRepo categoryRepo) {
        this.cart = cart;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.addHeader("strict-transport-security", "max-age=31622400; includeSubDomains");
        response.addHeader("Cache-Control", CacheControl.noCache().getHeaderValue());
        if (request.getMethod().equals("GET") && response.getStatus() == 200) {

            request.setAttribute("categories", this.categoryRepo.getAll());
            List<CartLine> cartlines = this.cart.GetCartLines();
            if (cartlines != null) {
                request.setAttribute("NumberOfCartLines", cartlines.size());
            }

            if (request.getUserPrincipal() != null) {
                String username;
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal instanceof UserDetails) {
                    username = ((UserDetails)principal).getUsername();
                }
                else if (principal instanceof OAuth2AuthenticatedPrincipal) {
                    username = ((OAuth2AuthenticatedPrincipal)principal).getAttribute("name");
                } else {
                    username = principal.toString();
                }
                request.setAttribute("username", username);
            }

        }

        return true;
    }


/*
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

        if (request.getUserPrincipal() != null) {
                String username;
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal instanceof UserDetails) {
                     username = ((UserDetails)principal).getUsername();
                }
                else if (principal instanceof OAuth2AuthenticatedPrincipal) {
                     username = ((OAuth2AuthenticatedPrincipal)principal).getAttribute("name");
                } else {
                     username = principal.toString();
                }
                modelAndView.addObject("username", username);
                request.setAttribute("username",username);
            }

        }

    }
*/

}
