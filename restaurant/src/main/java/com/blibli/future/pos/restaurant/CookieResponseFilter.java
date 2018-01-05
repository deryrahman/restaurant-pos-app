package com.blibli.future.pos.restaurant;

import org.apache.log4j.MDC;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.NotAuthorizedException;
import java.io.IOException;

public class CookieResponseFilter implements Filter {

    HttpServletRequest httpServletRequest;
    HttpServletResponse httpServletResponse;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        httpServletRequest = (HttpServletRequest) request;
        httpServletResponse = (HttpServletResponse) response;

        Cookie cookie = null;
        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie1 : httpServletRequest.getCookies()) {
                if (cookie1.getName().equals("POSRESTAURANT")) {
                    cookie = cookie1;
                }
            }
        }
        if(cookie == null){
            throw new NotAuthorizedException("Not valid cookie");
        }
        String refreshToken = (String) httpServletRequest.getAttribute("refreshToken");
        cookie.setValue(refreshToken);
        cookie.setMaxAge(60*60*24*365);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
