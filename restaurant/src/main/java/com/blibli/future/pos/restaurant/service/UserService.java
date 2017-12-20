package com.blibli.future.pos.restaurant.service;

import com.blibli.future.pos.restaurant.common.model.BaseResponse;
import com.blibli.future.pos.restaurant.common.model.User;
import com.blibli.future.pos.restaurant.dao.user.UserDAOMysql;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("ALL")
@Path("/users")
public class UserService extends BaseRESTService {
    private static UserDAOMysql userDAO = new UserDAOMysql();

    private User user;
    private List<User> users;

    // ---- BEGIN /users ----
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(User user) throws Exception {
        if(user.notValidAttribute()){
            throw new BadRequestException(ErrorMessage.requiredValue(user));
        }

        th.runTransaction(conn -> {
            if(!userDAO.findById(user.getEmail()).isEmpty()){
                throw new BadRequestException("Email already taken");
            }
            userDAO.create(user);
            return null;
        });

        baseResponse = new BaseResponse(true, 201);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(201).entity(json).build();
    }

    @GET
    @Produces("application/json")
    public Response getAll() throws Exception {
        users = (List<User>) th.runTransaction(conn -> {
            List<User> users = userDAO.find("true");
            if(users.size()==0){
                throw new NotFoundException("User not found");
            }
            return users;
        });

        baseResponse = new BaseResponse(true,200,users);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
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
    public Response delete(@PathParam("id") int id) throws Exception {

        th.runTransaction(conn -> {
            if(userDAO.findById(id).isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(user));
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
    public Response update(@PathParam("id") int id, User user) throws Exception {
        th.runTransaction(conn -> {
            this.user = userDAO.findById(id);

            if(this.user.isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(this.user));
            }

            if(this.user.getId() != id){
                throw new BadRequestException("Id not match");
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
