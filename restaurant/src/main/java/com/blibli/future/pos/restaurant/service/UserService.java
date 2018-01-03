package com.blibli.future.pos.restaurant.service;

import com.blibli.future.pos.restaurant.common.ApplicationContex;
import com.blibli.future.pos.restaurant.common.ErrorMessage;
import com.blibli.future.pos.restaurant.common.model.BaseResponse;
import com.blibli.future.pos.restaurant.common.model.Config;
import com.blibli.future.pos.restaurant.common.model.User;
import com.blibli.future.pos.restaurant.dao.user.UserDAOMysql;
import okhttp3.*;

import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
@Path("/users")
public class UserService extends BaseRESTService {
    private static final UserDAOMysql userDAO = new UserDAOMysql();
    private User user;
    private List<User> users;

    public static void initUser(Integer id) throws Exception {
        userId = id;
        User user = (User) th.runTransaction(conn -> {
            return (new UserDAOMysql()).findById(id);
        });
        restaurantId = user.getRestaurantId();
        ROLE = user.getRole();
    }

    private boolean createIdentity(User user) throws Exception {
        class Identity{
            private Integer id;
            private String username;
            private String password;
            private String role;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }
        }
        Identity identity = new Identity();
        identity.setId(user.getId());
        identity.setUsername(user.getUsername());
        identity.setRole(user.getRole());
        identity.setPassword(user.getPassword());
        json = objectMapper.writeValueAsString(identity);

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HttpSession session = (HttpSession) ApplicationContex.getServletContext().getAttribute("session");
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        final Request original = chain.request();

                        final Request authorized = original.newBuilder()
                                .addHeader("Cookie", "POSRESTAURANT="+((String) session.getAttribute("refreshToken")))
                                .build();

                        return chain.proceed(authorized);
                    }
                })
                .build();
        RequestBody body = RequestBody.create(JSON, json);
        Config config = (Config) ApplicationContex.getServletContext().getAttribute("restaurantConfig");
        String url = config.getAuthService() + "/userIdentities";
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        if(response.isSuccessful()){
            return true;
        }
        return false;
    }

    // ---- BEGIN /users ----
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(List<User> users) throws Exception {

        if(!(userIs(ADMIN) || userIs(MANAGER))){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }
        if(users.isEmpty()){
            throw new BadRequestException();
        }

        this.users = new ArrayList<>();
        for (User user: users) {
            if(user.notValidAttribute()){
                throw new BadRequestException(ErrorMessage.requiredValue(user));
            }
            if(user.getUsername() == null || user.getUsername().isEmpty()){
                throw new BadRequestException(ErrorMessage.requiredValue(user));
            }
            if(user.getPassword() == null || user.getPassword().isEmpty()){
                throw new BadRequestException(ErrorMessage.requiredValue(user));
            }

            User user1 = (User) th.runTransaction(conn -> {
                if(!userDAO.findById(user.getEmail()).isEmpty()){
                    throw new BadRequestException("Email already taken");
                }
                if(userIs(MANAGER)){
                    user.setRestaurantId(this.restaurantId);
                    if(user.getRole() == ADMIN){
                        user.setRole(MANAGER);
                    }
                }
                if(userIs(ADMIN)){
                    if(user.getRole() == CASHIER){
                        user.setRole(MANAGER);
                    }
                }
                userDAO.create(user);
                return user;
            });

//            if(!createIdentity(user)){
//                delete(user.getId());
//                throw new BadRequestException();
//            }

            this.users.add(user);
        }

        baseResponse = new BaseResponse(true, 201, users);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(201).entity(json).build();
    }

    @GET
    @Produces("application/json")
    public Response getAll() throws Exception {

        if(userIs(ADMIN)) {
            users = (List<User>) th.runTransaction(conn -> {
                List<User> users = userDAO.find("role='admin' OR role='manager'");
                if (users.size() == 0) {
                    throw new NotFoundException(ErrorMessage.NotFoundFrom(new User()));
                }
                return users;
            });

            baseResponse = new BaseResponse(true, 200, users);
            json = objectMapper.writeValueAsString(baseResponse);
            return Response.status(200).entity(json).build();
        }
        if(userIs(MANAGER)){
            users = (List<User>) th.runTransaction(conn -> {
                List<User> users = userDAO.find("restaurant_id="+this.restaurantId+" AND (role='manager' OR role='cashier')");
                if (users.size() == 0) {
                    throw new NotFoundException(ErrorMessage.NotFoundFrom(new User()));
                }
                return users;
            });

            baseResponse = new BaseResponse(true, 200, users);
            json = objectMapper.writeValueAsString(baseResponse);
            return Response.status(200).entity(json).build();
        }
        throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
    }

    @DELETE
    @Produces("application/json")
    public Response delete() throws Exception {
        throw new NotAllowedException(ErrorMessage.DELETE_NOT_ALLOWED, Response.status(405).build());
    }

    @PUT
    @Produces("application/json")
    public Response update() throws Exception {
        throw new NotAllowedException(ErrorMessage.PUT_NOT_ALLOWED, Response.status(405).build());
    }
    // ---- END /users ----

    // ---- BEGIN /users/{id} ----
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response get(@PathParam("id") int id) throws Exception {

        this.user = (User) th.runTransaction(conn -> {
            User user = userDAO.findById(id);
            if(user.isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(user));
            }
            if(userIs(MANAGER)){
                if(user.getRestaurantId()!=this.restaurantId){
                    throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
                }
            }
            if(userIs(CASHIER)){
                if(user.getId()!=id){
                    throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
                }
            }
            return user;
        });

        baseResponse = new BaseResponse(true,200,user);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }

    @POST
    @Path("/{id}")
    @Produces("application/json")
    public Response create(@PathParam("id") int id) throws Exception {
        throw new NotAllowedException(ErrorMessage.POST_NOT_ALLOWED, Response.status(405).build());
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") int id) throws Exception {

        if(!(userIs(ADMIN) || userIs(MANAGER))){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }
        th.runTransaction(conn -> {
            User user = userDAO.findById(id);
            if(user.isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new User()));
            }
            if(userIs(MANAGER)){
                if(user.getRestaurantId()!=this.restaurantId){
                    throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
                }
            }
            userDAO.delete(id);
            return null;
        });

        baseResponse = new BaseResponse(true,200);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response update(@PathParam("id") int id, User user) throws Exception {

        if(!(userIs(ADMIN) || userIs(MANAGER))){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }
//        if(user.notValidAttribute()){
//            throw new BadRequestException(ErrorMessage.requiredValue(user));
//        }
        th.runTransaction(conn -> {
            this.user = userDAO.findById(id);

            if(this.user.isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(this.user));
            }

            if(this.user.getId() != id){
                throw new BadRequestException("Id not match");
            }

            if(userIs(MANAGER)){
                if(this.user.getRestaurantId()!=this.restaurantId){
                    throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
                }
            }

            // fill if have empty
            if(user.getRole() == null){
                user.setRole(this.user.getRole());
            }
            if(user.getEmail() == null){
                user.setEmail(this.user.getEmail());
            }
            if(user.getRestaurantId() == null){
                user.setRestaurantId(this.user.getRestaurantId());
            }
            if(user.getName() == null){
                user.setName(this.user.getName());
            }
            userDAO.update(id,user);

            return null;
        });

        baseResponse = new BaseResponse(true,200);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }
    // ---- END /users/{id} ----
}
