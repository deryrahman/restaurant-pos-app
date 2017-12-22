package com.blibli.future.pos.restaurant;

import com.blibli.future.pos.restaurant.common.ApplicationUtility;
import com.blibli.future.pos.restaurant.common.model.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.parser.JSONParser;
import okhttp3.*;

import javax.security.sasl.AuthenticationException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Provider
public class AuthenticationFitler implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Config config = (Config) ApplicationUtility.getServletContext().getAttribute("restaurantConfig");

        Map<String,Cookie> cookies = requestContext.getCookies();
        String JWT = null;
        for (Map.Entry<String, Cookie> pair : cookies.entrySet()) {
            if(pair.getKey().equals("POSRESTAURANT")){
                JWT = pair.getValue().getValue();
            }
        }
        if(JWT == null){
            throw new NotFoundException("Cookie not found");
        }
        OkHttpClient client = new OkHttpClient();
        FormBody requestBody = new FormBody.Builder().add("token",JWT).build();
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
        if(map.get("status").equals("invalid")){
            throw new NotAuthorizedException("Invalid token");
        }
        
    }
}
