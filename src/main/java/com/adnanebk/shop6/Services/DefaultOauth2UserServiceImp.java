package com.adnanebk.shop6.Services;

import com.adnanebk.shop6.Models.User;
import com.adnanebk.shop6.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class DefaultOauth2UserServiceImp extends DefaultOAuth2UserService {


    private final UserRepo userRepo;

    public DefaultOauth2UserServiceImp(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
       OAuth2User oAuth2User = super.loadUser(userRequest);
        String name = oAuth2User.getAttribute("name");
        if(!userRepo.existsById(name))
        {
           String email = oAuth2User.getAttribute("email");
            userRepo.save(new User(name,email!=null?email:name+"@domain.com"));
        }
        return  oAuth2User;
    }
}
