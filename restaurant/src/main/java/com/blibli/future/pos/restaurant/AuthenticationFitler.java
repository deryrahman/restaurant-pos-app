package com.blibli.future.pos.restaurant;

import com.blibli.future.pos.restaurant.common.ApplicationContex;
import com.blibli.future.pos.restaurant.common.ErrorMessage;
import com.blibli.future.pos.restaurant.common.model.Config;
import com.blibli.future.pos.restaurant.common.model.User;
import com.blibli.future.pos.restaurant.service.BaseRESTService;
import com.blibli.future.pos.restaurant.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.apache.log4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.servlet.http.Cookie;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;

public class AuthenticationFitler implements Filter {

    private static final Config config = (Config) ApplicationContex.getServletContext().getAttribute("restaurantConfig");

    HttpServletRequest httpServletRequest;

    private Cookie getCookie(String key) {
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
        return cookie;
    }
//
    private Map<String,Object> getTokenResponse(Cookie cookie) throws IOException {
        OkHttpClient client = new OkHttpClient();
        FormBody requestBody = new FormBody.Builder().add("token",cookie.getValue()).build();
        Request request = new Request.Builder()
                .url(config.getAuthService() + "/parseToken")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        String bodyStr = response.body().string();
        body.close();
        if(!response.isSuccessful()){
            throw new NotAuthorizedException("Invalid token");
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> result = mapper.readValue(bodyStr, Map.class);
        if(!result.get("status").equals("ok")){
            throw new NotAuthorizedException("Invalid token");
        }
        return result;
    }
//
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        httpServletRequest = (HttpServletRequest) request;
        Cookie cookie = getCookie("POSRESTAURANT");
        // get response from cookie
        Map<String,Object> responseMap = getTokenResponse(cookie);
        // get payload
        Map<String,Object> payload = (Map<String,Object>) responseMap.get("payload");

        String refreshToken = payload.get("refreshToken").toString();
        Integer userId = Integer.valueOf(payload.get("id").toString());
        String role = payload.get("role").toString();


        User user = null;
        try {
            user = UserService.getUser(userId);
            user.setRole(role);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(user == null){
            throw new NotAuthorizedException("");
        }

        httpServletRequest.setAttribute("user", user);
        httpServletRequest.setAttribute("refreshToken", refreshToken);
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
