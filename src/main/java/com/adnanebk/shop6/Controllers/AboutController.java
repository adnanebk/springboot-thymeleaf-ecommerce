//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/about"})
public class AboutController {
    public AboutController() {
    }

    @GetMapping
    public String aboutMe(Model m) {
        m.addAttribute("active", "about");
        return "about";
    }
}
