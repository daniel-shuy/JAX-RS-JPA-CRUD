package com.github.daniel.shuy.ws.rs.jpa.crud;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(EntityCRUD.class)
public abstract class EntityCRUD_ {
    public static volatile SingularAttribute<EntityCRUD, Long> id;
}
