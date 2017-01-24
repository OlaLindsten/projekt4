/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.services;


import javax.ejb.EJB;
import javax.json.JsonArray;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nu.te4.beans.recipeBean;
import nu.te4.support.User;

@Path("/")
public class recipeService {

    @EJB
    recipeBean RecipeBean;
    
    @GET
    @Path("recipes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecipe() {

        JsonArray data = RecipeBean.getRecipe();

        if (data == null) {
            return Response.serverError().build();
        }

        return Response.ok(data).build();
    }
    
    @GET
    @Path("ingredient")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIngredient() {

        JsonArray data = RecipeBean.getIngredient();

        if (data == null) {
            return Response.serverError().build();
        }

        return Response.ok(data).build();
    }
    
    @GET
    @Path("recipe/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecipeId(@PathParam("id") int id) {

        JsonArray data = RecipeBean.getRecipeId(id);

        if (data == null) {
            return Response.serverError().build();
        }

        return Response.ok(data).build();
    }
    
    @POST
    @Path("recipe")
    @Consumes(MediaType.APPLICATION_JSON)

    public Response addRecipe(String body, @Context HttpHeaders httpHeaders) {
        
        if (!User.Authoricate(httpHeaders)) {
            return Response.status(401).build();
        }
        
        System.out.println(body);
        if (!RecipeBean.addRecipe(body)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.CREATED).build();
    }
    
    
    @POST
    @Path("ingredient")
    @Consumes(MediaType.APPLICATION_JSON)

    public Response addIngredient(String body) {
        
        System.out.println(body);
        if (!RecipeBean.addIngredient(body)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.CREATED).build();
    }
    
    @POST
    @Path("rec_ing")
    @Consumes(MediaType.APPLICATION_JSON)

    public Response addRec_ing(String body) {
        
        System.out.println(body);
        if (!RecipeBean.addRec_ing(body)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.CREATED).build();
    }
    
    @DELETE
    @Path("recipe/{id}")
    public Response deleteRecipe(@PathParam("id") int id) {
        
        System.out.println(id);
        if (!RecipeBean.deleteRecipe(id)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok().build();
    }
    
    @DELETE
    @Path("ingredient/{id}")
    public Response deleteIngredient(@PathParam("id") int id) {
        
        System.out.println(id);
        if (!RecipeBean.deleteIngredient(id)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok().build();
    }
    
    @PUT
    @Path("recipe")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRecipe(String body) {
        
        System.out.println("fel" + body);
        
        if (!RecipeBean.updateRecipe(body)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }
    
    @PUT
    @Path("ingredient")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateIngredient(String body) {
        
        System.out.println("fel" + body);
        
        if (!RecipeBean.updateIngredient(body)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }
}
