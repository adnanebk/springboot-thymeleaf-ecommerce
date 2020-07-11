//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Services;

import com.adnanebk.shop6.Models.User;
import com.adnanebk.shop6.Repositories.UserRepo;
import java.util.ResourceBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FormValidator implements Validator {
    final UserRepo userRepo;

    public FormValidator(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    public void validate(Object o, Errors errors) {
        User user = (User)o;
        System.out.printf("******" + user.getPassword() + "*****" + user.getMatchPassword());
        if (!User._passwordEncoder.matches(user.getMatchPassword(), user.getPassword())) {
            errors.rejectValue("MatchPassword", "err.password", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("passwordNotMatch"));
        } else if (this.userRepo.existsById(user.getUsername()) || this.userRepo.findByEmail(user.getEmail()) != null) {
            errors.rejectValue((String)null, "err.password", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("accountAlreadyExist"));
        }

    }
}
