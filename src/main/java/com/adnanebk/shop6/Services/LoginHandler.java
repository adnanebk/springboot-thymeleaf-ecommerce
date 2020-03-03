//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Services;

import com.adnanebk.shop6.Models.SocialUser;
import com.adnanebk.shop6.Repositories.UserRepo;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class LoginHandler implements AuthenticationFailureHandler, AuthenticationSuccessHandler {
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

    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        JSONObject jsonObject = new JSONObject(authentication);
        SocialUser socialUser = new SocialUser();
        String name = jsonObject.getJSONObject("userAuthentication").getJSONObject("details").getString("name");
        String email;
        if (jsonObject.getJSONObject("userAuthentication").getJSONObject("details").isNull("email")) {
            email = name + "@social.com";
        } else {
            email = jsonObject.getJSONObject("userAuthentication").getJSONObject("details").getString("email");
        }

        System.out.println("emmmmmmmmm---" + email);
        socialUser.setEmail(email);
        socialUser.setName(name);
        httpServletRequest.getSession().setAttribute("socialuser", socialUser);
        httpServletResponse.sendRedirect("/");
    }
}
