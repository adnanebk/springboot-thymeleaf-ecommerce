//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Controllers;

import com.adnanebk.shop6.Models.User;
import com.adnanebk.shop6.Repositories.UserRepo;
import com.adnanebk.shop6.Services.FormValidator;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    private final UserRepo userRepo;
    private final FormValidator formValidator;
    private AuthenticationManager authManager;

    @Autowired
    public LoginController(UserRepo userRepo, FormValidator formValidator, AuthenticationManager authManager) {
        this.userRepo = userRepo;
        this.formValidator = formValidator;
        this.authManager = authManager;
    }

    @GetMapping({"/register"})
    public String register(User u, Model md) {
        md.addAttribute("user", u);
        return "register";
    }

    @GetMapping({"/login"})
    public String loginGet(@ModelAttribute("user") User us, Principal principal) {
        return principal != null ? "redirect:/" : "login";
    }

    @PostMapping({"/register"})
    public String RegisterPost(@ModelAttribute("user") @Valid User u, BindingResult bindingResult, Model md, HttpServletRequest request) {
        this.formValidator.validate(u, bindingResult);
        if (bindingResult.hasErrors()) {
            return "register";
        } else {
            u.setRoles("ROLE-USER");
            this.userRepo.save(u);
            md.addAttribute("user", u);
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(u.getUsername(), u.getMatchPassword());
            authReq.setDetails(new WebAuthenticationDetails(request));
            Authentication authentication = this.authManager.authenticate(authReq);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/";
        }
    }

    @RequestMapping({"/user"})
    @ResponseBody
    public Principal user(Principal principal) {
        System.out.printf("loggggggggggg" + principal.getName());
        return principal;
    }

    @RequestMapping({"/login/facebook"})
    public String FacebookRegistration(Principal principal) {
        System.out.printf("loggggggfffffggggg" + principal.getName());
        return "redirect:/";
    }

    @RequestMapping({"/login/google"})
    @ResponseBody
    public Principal GoogleRegistration(Principal principal) {
        System.out.printf("loggggggfffffggggg2" + principal.getName());
        return principal;
    }
}
