package com.blibli.future.pos.restaurant;

import com.blibli.future.pos.restaurant.common.ApplicationContex;
import com.blibli.future.pos.restaurant.common.model.Config;
import com.blibli.future.pos.restaurant.common.model.User;
import com.blibli.future.pos.restaurant.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotAuthorizedException;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.Map;

public class AuthenticationFitler implements Filter {

    private static final Config config = (Config) ApplicationContex.getServletContext().getAttribute("restaurantConfig");

    private Cookie getCookie(String key, HttpServletRequest httpServletRequest) {
        Cookie cookie = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie1 : cookies) {
                try {
                    if (cookie1.getName().equals(key)) {
                        cookie = cookie1;
                        break;
                    }
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
        if(cookie == null){
            throw new NotAuthorizedException("Not valid cookie");
        }
        return cookie;
    }

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

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        Cookie cookie = getCookie("POSRESTAURANT",httpServletRequest);
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
