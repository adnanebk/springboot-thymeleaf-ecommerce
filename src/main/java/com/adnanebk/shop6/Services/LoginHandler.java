//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Services;

import com.adnanebk.shop6.Repositories.UserRepo;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class LoginHandler implements AuthenticationFailureHandler {
    @Autowired
    UserRepo userRepo;

    public LoginHandler() {
    }

    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        String user = httpServletRequest.getParameter("username");
        if (this.userRepo.findByUsername(user) == null && this.userRepo.findByEmail(user) == null) {
            httpServletResponse.sendRedirect("/login?Uerror=UsernameError");
        } else {
            httpServletResponse.sendRedirect("/login?Perror=PasswordError");
        }

    }


}
