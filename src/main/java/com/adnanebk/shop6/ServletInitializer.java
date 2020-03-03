//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
    public ServletInitializer() {
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(new Class[]{Shop6Application.class});
    }

    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.getSessionCookieConfig().setHttpOnly(false);
        servletContext.getSessionCookieConfig().setSecure(true);
        super.onStartup(servletContext);
    }
}
