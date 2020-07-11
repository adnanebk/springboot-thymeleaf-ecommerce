//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Security;

import com.adnanebk.shop6.Services.DefaultOauth2UserServiceImp;
import com.adnanebk.shop6.Services.LoginHandler;
import com.adnanebk.shop6.Services.Oid2UserServiceImp;
import com.adnanebk.shop6.Services.UserDetailsServiceImp;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ChannelSecurityConfigurer.RequiresChannelUrl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private Oid2UserServiceImp oid2UserServiceImp;
    private DefaultOauth2UserServiceImp defaultOauth2UserServiceImp;
    @Qualifier("UserDetailsServiceImp1")
    final UserDetailsServiceImp userDetailsServiceImp;
    final HikariDataSource ds;

    public SecurityConfig(Oid2UserServiceImp oid2UserServiceImp, DefaultOauth2UserServiceImp defaultOauth2UserServiceImp , UserDetailsServiceImp userDetailsServiceImp, HikariDataSource ds) {
        this.oid2UserServiceImp = oid2UserServiceImp;
        this.defaultOauth2UserServiceImp = defaultOauth2UserServiceImp;
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.ds = ds;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LoginHandler loginHandler() {
        return new LoginHandler();
    }

    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/h2-console/**");
        web.ignoring().antMatchers("/static/productApi/**","/productApi/**","/assets/**", "assets/**", "/css/**", "/js/**", "/imgs/**", "/src/main/webapp/uploadingDir/**", "/resources/**", "/static/imgs/**", "/static/**", "resources/static/imgs/**");
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    protected void configure(HttpSecurity http) throws Exception {
        ((RequiresChannelUrl)http.requiresChannel().requestMatchers(new RequestMatcher[]{(r) -> {
            return r.getHeader("X-Forwarded-Proto") != null && r.getHeader("X-Forwarded-Proto").indexOf("https") != 0;
        }})).requiresSecure();
        http.headers().httpStrictTransportSecurity().includeSubDomains(true);
        http.authorizeRequests().antMatchers("/checkout").authenticated().anyRequest().permitAll().and()
                .formLogin().loginPage("/login").permitAll()
                .failureHandler(this.loginHandler()).and().exceptionHandling().accessDeniedPage("/denied")
                .and().logout().logoutSuccessUrl("/").permitAll().and()
                .rememberMe().key("myUniqueKey").tokenValiditySeconds(10000000);
       // http.addFilterBefore(this.ssoFilter(), BasicAuthenticationFilter.class);
        http.oauth2Login().loginPage("/oauth_login")
                .userInfoEndpoint().oidcUserService(oid2UserServiceImp).userService(defaultOauth2UserServiceImp);
        http.headers().addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy", "script-src 'self'"))
                .addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy", "default-src 'self'"))
                .addHeaderWriter(new StaticHeadersWriter("X-WebKit-CSP", "default-src 'self'"));
    }

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsServiceImp).passwordEncoder(this.passwordEncoder());
    }


}
