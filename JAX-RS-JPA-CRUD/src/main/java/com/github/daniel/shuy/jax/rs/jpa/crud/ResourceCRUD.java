/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daniel.shuy.jax.rs.jpa.crud;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
    public E read(@PathParam("id") String id) {
        long idLong;
        try {
            idLong = Long.parseLong(id);
        }
        catch (NumberFormatException e) {
            return null;
        }
        
        return repository.get(idLong);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(E content) {
        repository.update(content);
    }
    
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public void delete(E content) {
        repository.remove(content);
    }
}
