package com.blibli.future.pos.restaurant.service;

import com.blibli.future.pos.restaurant.common.ErrorMessage;
import com.blibli.future.pos.restaurant.common.model.BaseResponse;
import com.blibli.future.pos.restaurant.common.model.Category;
import com.blibli.future.pos.restaurant.common.model.Item;
import com.blibli.future.pos.restaurant.common.model.custom.ItemWithStock;
import com.blibli.future.pos.restaurant.dao.category.CategoryDAOMysql;
import com.blibli.future.pos.restaurant.dao.custom.itemwithstock.ItemWithStockDAOMysql;
import com.blibli.future.pos.restaurant.dao.item.ItemDAOMysql;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("ALL")
@Path("/categories")
public class CategoryService extends BaseRESTService {
    private CategoryDAOMysql categoryDAO = new CategoryDAOMysql();
    private ItemDAOMysql itemDAO = new ItemDAOMysql();
    private ItemWithStockDAOMysql itemWithStockDAO = new ItemWithStockDAOMysql();
    private List<Category> categories;
    private List<Item> items;
    private Category category;

    // ---- BEGIN /categories ----
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(List<Category> categories) throws Exception {
        initializeRole();
        if(!userIs(ADMIN)){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }
        if (categories.isEmpty()) {
            throw new BadRequestException();
        }

        for (Category category : categories) {
            if (category.notValidAttribute()) {
                throw new BadRequestException(ErrorMessage.requiredValue(category));
            }

            th.runTransaction(conn -> {
                categoryDAO.create(category);
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
        categories = (List<Category>) th.runTransaction(conn -> {
            List<Category> categories = categoryDAO.find("true");
            if (categories.size() == 0) {
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new Category()));
            }
            return categories;
        });

        baseResponse = new BaseResponse(true, 200, categories);
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
    // ---- END /categories ----

    // ---- BEGIN /categories/{id} ----
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response get(@PathParam("id") int id) throws Exception {
        category = (Category) th.runTransaction(conn -> {
            Category category = categoryDAO.findById(id);
            if (category.isEmpty()) {
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new Category()));
            }
            return category;
        });

        baseResponse = new BaseResponse(true, 200, category);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }

    @POST
    @Path("/{id}")
    @Produces("application/json")
    public Response create(@PathParam("id") int id) {
        throw new NotAllowedException(ErrorMessage.POST_NOT_ALLOWED, Response.status(405).build());
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") int id) throws Exception {
        initializeRole();
        if(!userIs(ADMIN)){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }
        if(id==1){
            throw new BadRequestException("Default category cannot be deleted");
        }
        th.runTransaction(conn -> {
            if(categoryDAO.findById(id).isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new Category()));
            }
            // update as default category
            List<Item> items = itemDAO.find("category_id="+id);
            for (Item item:items) {
                item.setCategoryId(1);
                itemDAO.update(item.getId(),item);
            }
            categoryDAO.delete(id);
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
    public Response update(@PathParam("id") int id, Category category) throws Exception {
        initializeRole();
        if(!userIs(ADMIN)){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }
        if(id==1){
            throw new BadRequestException("Default category cannot be update");
        }
        if (category.notValidAttribute()) {
            throw new BadRequestException(ErrorMessage.requiredValue(new Category()));
        }
        th.runTransaction(conn -> {
            this.category = categoryDAO.findById(id);

            if (this.category.isEmpty()) {
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new Category()));
            }

            if (this.category.getId() != id) {
                throw new BadRequestException("Id not match");
            }

            categoryDAO.update(id, category);

            return null;
        });

        baseResponse = new BaseResponse(true, 200);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }
    // ---- END /categories/{id} ----


    /**
     * special purpose of nested resources. Like /categories/1/items. It will call /items, on itemsResources
     */
    @GET
    @Path("/{categoryId}/items")
    @Produces("application/json")
    public Response getAllItem(@PathParam("categoryId") int categoryId) throws Exception {
        initializeRole();
        if(userIs(ADMIN)){
            items = (List<Item>) th.runTransaction(conn -> {
                // check categoryId first
                if (categoryDAO.findById(categoryId).isEmpty()) {
                    throw new NotFoundException(ErrorMessage.NotFoundFrom(new Category()));
                }

                // search specific item with category_id
                List<Item> items = itemDAO.find("category_id=" + categoryId);
                if (items.size() == 0) {
                    throw new NotFoundException(ErrorMessage.NotFoundFrom(new Item()));
                }
                return items;
            });

            baseResponse = new BaseResponse(true, 200, items);
            json = objectMapper.writeValueAsString(baseResponse);
            return Response.status(200).entity(json).build();
        }

        List<ItemWithStock> itemWithStockList = (List<ItemWithStock>) th.runTransaction(conn -> {
            // check categoryId first
            if (categoryDAO.findById(categoryId).isEmpty()) {
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new Category()));
            }

            // search specific item with category_id
            List<ItemWithStock> items = itemWithStockDAO.findByRestaurantId(this.restaurantId, "category_id=" + categoryId);
            if (items.size() == 0) {
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new ItemWithStock()));
            }
            return items;
        });

        baseResponse = new BaseResponse(true, 200, itemWithStockList);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }
}
