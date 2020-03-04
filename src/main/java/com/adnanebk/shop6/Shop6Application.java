//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6;

import com.adnanebk.shop6.Services.MyInterceptor;
import java.util.Locale;
import javax.servlet.Filter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@SpringBootApplication
//@EnableCaching
@EnableAsync
public class Shop6Application implements WebMvcConfigurer {
    @Autowired
    MyInterceptor myInterceptor;

    public Shop6Application() {
    }

    public static void main(String[] args) {
        SpringApplication.run(Shop6Application.class, args);
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("prod","cat","prodfiltred","prodpaged"); // Cache Vendor
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.myInterceptor).excludePathPatterns(new String[]{"/assets/**", "/error/**", "/error", "/imgs/**", "/productApi/**","/uploadingDir/**"});
        registry.addInterceptor(this.localeChangeInterceptor());
    }



}
