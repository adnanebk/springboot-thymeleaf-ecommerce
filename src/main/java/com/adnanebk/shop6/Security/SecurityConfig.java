//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Security;

import com.adnanebk.shop6.Services.LoginHandler;
import com.adnanebk.shop6.Services.UserDetailsServiceImp;
import com.zaxxer.hikari.HikariDataSource;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.ChannelSecurityConfigurer.RequiresChannelUrl;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.CompositeFilter;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    @Qualifier("UserDetailsServiceImp1")
    UserDetailsServiceImp userDetailsServiceImp;
    @Qualifier("oauth2ClientContext")
    @Autowired
    OAuth2ClientContext oauth2ClientContext;
    @Autowired
    HikariDataSource ds;

    public SecurityConfig() {
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
        web.ignoring().antMatchers(new String[]{"/h2-console/**"});
        web.ignoring().antMatchers(new String[]{"/static/productApi/**","/productApi/**","/assets/**", "assets/**", "/css/**", "/js/**", "/imgs/**", "/src/main/webapp/uploadingDir/**", "/resources/**", "/static/imgs/**", "/static/**", "resources/static/imgs/**"});
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
        ((HttpSecurity)((HttpSecurity)((HttpSecurity)((FormLoginConfigurer)((FormLoginConfigurer)((HttpSecurity)((AuthorizedUrl)((AuthorizedUrl)http.authorizeRequests().antMatchers(new String[]{"/checkout"})).authenticated().anyRequest()).permitAll().and()).formLogin().loginPage("/login").permitAll()).failureHandler(this.loginHandler())).and()).exceptionHandling().accessDeniedPage("/denied").and()).logout().logoutSuccessUrl("/").permitAll().and()).rememberMe().key("myUniqueKey").tokenValiditySeconds(10000000);
        http.addFilterBefore(this.ssoFilter(), BasicAuthenticationFilter.class);
        http.headers().addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy", new String[]{"script-src 'self'"})).addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy", new String[]{"default-src 'self'"})).addHeaderWriter(new StaticHeadersWriter("X-WebKit-CSP", new String[]{"default-src 'self'"}));
    }

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsServiceImp).passwordEncoder(this.passwordEncoder());
    }

    @Bean
    @ConfigurationProperties("facebook.client")
    public AuthorizationCodeResourceDetails facebook() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("facebook.resource")
    public ResourceServerProperties facebookResource() {
        return new ResourceServerProperties();
    }

    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResource() {
        return new ResourceServerProperties();
    }

    @Bean
    @ConfigurationProperties("google.client")
    public AuthorizationCodeResourceDetails google() {
        return new AuthorizationCodeResourceDetails();
    }

    private Filter ssoFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<OAuth2ClientAuthenticationProcessingFilter> filters = new ArrayList();
        OAuth2ClientAuthenticationProcessingFilter facebookFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/facebook");
        OAuth2RestTemplate facebookTemplate = new OAuth2RestTemplate(this.facebook(), this.oauth2ClientContext);
        facebookFilter.setRestTemplate(facebookTemplate);
        facebookFilter.setAuthenticationSuccessHandler(this.loginHandler());
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(this.facebookResource().getUserInfoUri(), this.facebook().getClientId());
        tokenServices.setRestTemplate(facebookTemplate);
        facebookFilter.setTokenServices(tokenServices);
        filters.add(facebookFilter);
        OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/google");
        OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(this.google(), this.oauth2ClientContext);
        googleFilter.setRestTemplate(googleTemplate);
        googleFilter.setAuthenticationSuccessHandler(this.loginHandler());
        tokenServices = new UserInfoTokenServices(this.googleResource().getUserInfoUri(), this.google().getClientId());
        tokenServices.setRestTemplate(googleTemplate);
        googleFilter.setTokenServices(tokenServices);
        filters.add(googleFilter);
        filter.setFilters(filters);
        return filter;
    }

    @Bean
    public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }
}
