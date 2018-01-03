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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;

@Provider
public class AuthenticationFitler implements ContainerRequestFilter {

    @Context
    HttpServletRequest httpRequest;

    private static final Config config = (Config) ApplicationContex.getServletContext().getAttribute("restaurantConfig");
    private ContainerRequestContext requestContext;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        this.requestContext = requestContext;
        HttpSession session = httpRequest.getSession();
        // get cookie
        Cookie cookie = getCookie("POSRESTAURANT");
        // get response from cookie
        Map<String,Object> response = getTokenResponse(cookie);
        // get payload
        Map<String,Object> payload = (Map<String,Object>) response.get("payload");

        String refreshToken = payload.get("refreshToken").toString();
        Integer userId = Integer.valueOf(payload.get("id").toString());

        if(session.getAttribute("userId") == null || session.getAttribute("userId") != userId){
            System.out.println("init User : " + userId);
            try {
                User user = UserService.getUser(userId);
                session.setAttribute("user",user);
            } catch (Exception e) {
                throw new IOException(e.toString());
            }
        }
        session.setAttribute("refreshToken",refreshToken);
        ApplicationContex.getServletContext().setAttribute("session", session);
    }

    private Cookie getCookie(String key) {
        Cookie cookie = null;
        Map<String,Cookie> cookies = requestContext.getCookies();
        for (Map.Entry<String, Cookie> pair : cookies.entrySet()) {
            if(pair.getKey().equals(key)){
                cookie = pair.getValue();
            }
        }
        if(cookie == null){
            throw new NotFoundException("Cookie not found");
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
}
