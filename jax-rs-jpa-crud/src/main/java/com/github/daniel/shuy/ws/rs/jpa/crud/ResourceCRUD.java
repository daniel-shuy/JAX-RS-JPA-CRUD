/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daniel.shuy.ws.rs.jpa.crud;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Extend this class to create a CRUD RepositoryCRUD Class.
 * @param <E> The Entity Class type
 * @param <R> The Repository Class type
 */
public abstract class ResourceCRUD<E extends EntityCRUD, R extends RepositoryCRUD<E>> {
    private final R repository;
    
    public ResourceCRUD(R repository) {
        this.repository = repository;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void create(E content) {
        repository.add(content);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<E> readAll() {
        return repository.getAll();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response read(@PathParam("id") String id) {
        return doWithID(id, (idLong) -> {
            return Response.ok(repository.get(idLong)).build();
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(E content) {
        repository.update(content);
    }
    
    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") String id) {
        doWithID(id, (idLong) -> {
            repository.remove(idLong);
        });
    }
    
    private Response doWithID(String id, Function<Long, Response> function) {
        long idLong;
        try {
            idLong = Long.parseLong(id);
        }
        catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("id must be a Number").build();
        }
        
        return function.apply(idLong);
    }
    private void doWithID(String id, Consumer<Long> consumer) {
        doWithID(id, (idLong) -> {
            consumer.accept(idLong);
            return null;
        });
    }
}
