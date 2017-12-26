package com.blibli.future.pos.restaurant;

import org.apache.log4j.MDC;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CookieResponseFilter implements ContainerResponseFilter {
    @Context
    HttpServletRequest httpRequest;
    @Context
    HttpServletResponse httpResponse;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Cookie cookie = null;
        if (httpRequest.getCookies() != null) {
            for (Cookie cookie1 : httpRequest.getCookies()) {
                if (cookie1.getName().equals("POSRESTAURANT")) {
                    cookie = cookie1;
                }
            }
        }
        if(cookie == null){
            throw new NotAuthorizedException("Not valid cookie");
        }
        cookie.setValue((String) MDC.get("refreshToken"));
        cookie.setMaxAge(60*60*24*365);
        cookie.setPath("/");
        httpResponse.addCookie(cookie);
    }
}
