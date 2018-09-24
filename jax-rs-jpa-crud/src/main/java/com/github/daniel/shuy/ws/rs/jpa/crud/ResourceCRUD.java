package com.github.daniel.shuy.ws.rs.jpa.crud;

import java.util.List;
import javax.transaction.Transactional;
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
 * Extend this class to create a CRUD JAX-RS Repository Class.
 *
 * @param <E> The Entity Class type
 */
public abstract class ResourceCRUD<E extends EntityCRUD> {
    /**
     * Override this method to provide a Repository instance.
     * 
     * @return A Repository instance.
     */
    public abstract RepositoryCRUD<E> getRepository();

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Transactional
    public E create(E content) {
        return getRepository().create(content);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<E> findAll() {
        return getRepository().findAll();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public E find(@PathParam("id") Long id) {
        return getRepository().find(id);
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<E> findRange(@PathParam("from") Long from, @PathParam("to") Long to) {
        return getRepository().findRange(from, to);
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        return String.valueOf(getRepository().count());
    }
    
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Transactional
    public E edit(E content) {
        if (content.getId() == null) {
            // consider wrapping this with a ConstraintViolationException if
            // Bean Validation is used.
            throw new IllegalArgumentException("id must not be null");
        }
        return getRepository().edit(content);
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void remove(@PathParam("id") Long id) {
        getRepository().remove(id);
    }
}
