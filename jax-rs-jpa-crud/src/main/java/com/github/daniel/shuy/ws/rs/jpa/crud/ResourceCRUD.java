package com.github.daniel.shuy.ws.rs.jpa.crud;

import java.io.Closeable;
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
            getRepository().add(content);
        }
        finally {
            close();
        }
    }

    @GET
    public default List<E> readAll() {
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
        try {
            return getRepository().getAll();
        }
        finally {
            close();
        }
    }

    @GET
    @Path("{id}")
    public default Response read(@PathParam("id") String id) {
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
        try {
            return doWithID(id, (idLong) -> {
                return Response.ok(getRepository().get(idLong)).build();
            });
        }
        finally {
            close();
        }
    }

    @PUT
    public default void update(E content) {
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
        try {
            getRepository().update(content);
        }
        finally {
            close();
        }
    }

    @DELETE
    @Path("{id}")
    public default void delete(@PathParam("id") String id) {
        try {
            doWithID(id, (idLong) -> {
                getRepository().remove(idLong);
            });
        }
        finally {
            close();
        }
    }

    @Override
    public default void close() {}

    // TODO: change to private in Java 9
    public default Response doWithID(String id, Function<Long, Response> function) {
        long idLong;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("id must be a Number").build();
        }

        return function.apply(idLong);
    }

    // TODO: change to private in Java 9
    public default void doWithID(String id, Consumer<Long> consumer) {
        doWithID(id, (idLong) -> {
            consumer.accept(idLong);
            return null;
        });
    }
}
