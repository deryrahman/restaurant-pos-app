package com.blibli.future.pos.restaurant;

import com.blibli.future.pos.restaurant.common.ApplicationUtility;
import com.blibli.future.pos.restaurant.common.model.Config;
import com.blibli.future.pos.restaurant.service.BaseRESTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.apache.log4j.MDC;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;

@Provider
public class AuthenticationFitler implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Config config = (Config) ApplicationUtility.getServletContext().getAttribute("restaurantConfig");

        // Get POSRESTAURANT Cookie
        Map<String,Cookie> cookies = requestContext.getCookies();
        Cookie cookie = null;
        for (Map.Entry<String, Cookie> pair : cookies.entrySet()) {
            if(pair.getKey().equals("POSRESTAURANT")){
                cookie = pair.getValue();
            }
        }
        if(cookie == null){
            throw new NotFoundException("Cookie not found");
        }

        // Check if token is valid
        OkHttpClient client = new OkHttpClient();
        FormBody requestBody = new FormBody.Builder().add("token",cookie.getValue()).build();
        Request request = new Request.Builder()
                .url(config.getAuthService() + "/parseToken")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if(!response.isSuccessful()){
            throw new NotAuthorizedException("Invalid token");
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map = mapper.readValue(response.body().string(), Map.class);
        response.body().close();

        if(!map.get("status").equals("ok")){
            throw new NotAuthorizedException("Invalid token");
        }

        Map<String,Object> payload = (Map<String,Object>) map.get("payload");
        String refreshToken = payload.get("refreshToken").toString();
        Integer userId = Integer.valueOf(payload.get("id").toString());

        MDC.put("refreshToken",refreshToken);
        MDC.put("userId", userId);

        try {
            BaseRESTService.insertId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
