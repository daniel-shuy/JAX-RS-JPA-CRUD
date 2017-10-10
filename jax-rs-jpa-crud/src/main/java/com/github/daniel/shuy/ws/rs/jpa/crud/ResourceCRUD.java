package com.github.daniel.shuy.ws.rs.jpa.crud;

import java.io.Closeable;
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
 * Extend this class to create a CRUD JAX-RS Repository Class.
 *
 * @param <E> The Entity Class type
 */
public interface ResourceCRUD<E extends EntityCRUD> extends Closeable {
    public abstract RepositoryCRUD<E> getRepository();

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public default void create(E content) {
        try {
            getRepository().create(content);
        }
        finally {
            close();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public default List<E> findAll() {
        try {
            return getRepository().findAll();
        }
        finally {
            close();
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public default E find(@PathParam("id") Long id) {
        try {
            return getRepository().find(id);
        }
        finally {
            close();
        }
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public default List<E> findRange(@PathParam("from") Long from, @PathParam("to") Long to) {
        try {
            return getRepository().findRange(from, to);
        }
        finally {
            close();
        }
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public default String count() {
        try {
            return String.valueOf(getRepository().count());
        }
        finally {
            close();
        }
    }
    
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public default void edit(E content) {
        try {
            getRepository().edit(content);
        }
        finally {
            close();
        }
    }

    @DELETE
    @Path("{id}")
    public default void remove(@PathParam("id") Long id) {
        try {
            getRepository().remove(id);
        }
        finally {
            close();
        }
    }

    @Override
    public default void close() {}
}
