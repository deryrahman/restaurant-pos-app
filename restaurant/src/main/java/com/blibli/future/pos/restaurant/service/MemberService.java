package com.blibli.future.pos.restaurant.service;

import com.blibli.future.pos.restaurant.common.ErrorMessage;
import com.blibli.future.pos.restaurant.common.model.BaseResponse;
import com.blibli.future.pos.restaurant.common.model.Member;
import com.blibli.future.pos.restaurant.dao.member.MemberDAOMysql;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("ALL")
@Path("/members")
public class MemberService extends BaseRESTService {
    private MemberDAOMysql memberDAO = new MemberDAOMysql();
    private Member member;
    private List<Member> members;

    // ---- BEGIN /members ----
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(List<Member> members) throws Exception {
        initializeRole();
        if(!(userIs(CASHIER) || userIs(MANAGER))){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }
        if (members.isEmpty()) {
            throw new BadRequestException();
        }
        for (Member member : members) {
            if (member.notValidAttribute()) {
                throw new BadRequestException(ErrorMessage.requiredValue(new Member()));
            }
            th.runTransaction(conn -> {
                memberDAO.create(member);
                return null;
            });
        }

        baseResponse = new BaseResponse(true, 201);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(201).entity(json).build();
    }

    @GET
    @Produces("application/json")
    public Response getAll() throws Exception {
        members = (List<Member>) th.runTransaction(conn -> {
            List<Member> members = memberDAO.find("true");
            if (members.size() == 0) {
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new Member()));
            }
            return members;
        });

        baseResponse = new BaseResponse(true, 200, members);
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
    // ---- END /members ----

    // ---- BEGIN /members/{id} ----
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response get(@PathParam("id") int id) throws Exception {

        this.member = (Member) th.runTransaction(conn -> {
            Member member = memberDAO.findById(id);
            if (member.isEmpty()) {
                throw new NotFoundException(ErrorMessage.NotFoundFrom(member));
            }
            return member;
        });

        baseResponse = new BaseResponse(true, 200, member);
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
        throw new NotAllowedException(ErrorMessage.POST_NOT_ALLOWED, Response.status(405).build());
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response update(@PathParam("id") int id, Member member) throws Exception {
        initializeRole();
        if(!(userIs(CASHIER) || userIs(MANAGER))){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }
        if (member.notValidAttribute()) {
            throw new BadRequestException(ErrorMessage.requiredValue(member));
        }
        th.runTransaction(conn -> {
            this.member = memberDAO.findById(id);

            if (this.member.isEmpty()) {
                throw new NotFoundException(ErrorMessage.NotFoundFrom(this.member));
            }

            if (this.member.getId() != id) {
                throw new BadRequestException("Id not match");
            }

            memberDAO.update(id, member);

            return null;
        });

        baseResponse = new BaseResponse(true, 200);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }
    // ---- END /members/{id} ----
}
