package com.blibli.future.pos.restaurant.service;

import com.blibli.future.pos.restaurant.common.model.Member;
import com.blibli.future.pos.restaurant.common.model.Message;
import com.blibli.future.pos.restaurant.common.model.Metadata;
import com.blibli.future.pos.restaurant.dao.member.MemberDAOMysql;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/members")
public class MemberService {
    private MemberDAOMysql memberDAO = new MemberDAOMysql();
    private Gson gson = new Gson();
    private Message msg = new Message();

//    private Response get405Response(){
//        msg.setMessage("Method not allowed");
//        String json = gson.toJson(msg);
//        return Response.status(405).entity(json).build();
//    }
//
//    private Response get404Response(){
//        msg.setMessage("Not found");
//        String json = gson.toJson(msg);
//        return Response.status(404).entity(json).build();
//    }

    // ---- BEGIN /members ----
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(Member member) throws Exception {
        memberDAO.create(member);
        return Response.status(201).build();
    }
    @GET
    @Produces("application/json")
    public Response getAll() throws Exception {
        Gson gson = new Gson();
        List<Member> members = memberDAO.getBulk("true");

        Map<String, Object> map = new HashMap<>();
        Metadata metadata = new Metadata();
        metadata.setCount(members.size());
        metadata.setLimit(members.size());

        map.put("metadata", metadata);
        map.put("results", members);

        String json = gson.toJson(map);
        return Response.status(200).entity(json).build();
    }

    @DELETE
    @Produces("application/json")
    public Response delete() throws Exception {
        throw new Exception("Method not allowed");
    }

    @PUT
    @Produces("application/json")
    public Response update() throws Exception {
        throw new Exception("Method not allowed");
    }
    // ---- END /members ----

    // ---- BEGIN /members/{id} ----
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response get(@PathParam("id") int id) throws Exception {
        Gson gson = new Gson();
        Member member = memberDAO.getById(id);

        if(member == null){
            throw new Exception("Not found");
        }

        String json = gson.toJson(member);
        return Response.status(200).entity(json).build();
    }
    @POST
    @Path("/{id}")
    @Produces("application/json")
    public Response create(@PathParam("id") int id) throws Exception {

        throw new Exception("Method not allowed");
    }
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) throws Exception {
        memberDAO.delete(id);
        return Response.status(204).build();
    }
    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    public Response update(@PathParam("id") int id, Member member) throws Exception {
        Gson gson = new Gson();
        gson.toJson(member);
        memberDAO.update(id,member);
        return Response.status(204).build();
    }
    // ---- END /members/{id} ----
}
