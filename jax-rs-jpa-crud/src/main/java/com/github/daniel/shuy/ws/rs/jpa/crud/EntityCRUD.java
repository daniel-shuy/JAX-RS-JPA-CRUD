package com.github.daniel.shuy.ws.rs.jpa.crud;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Extend this class to create a CRUD JPA Entity Class.
 * <p>
 * The corresponding database table must have a sequential number Surrogate
 * Primary Key.
 */
@MappedSuperclass
public abstract class EntityCRUD implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    public EntityCRUD() {
    }

    public EntityCRUD(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        /*
        JPA entities should return constant hash code to allow lookups of a persisted managed entity
        that was added to a HashSet/HashMap while transient.
        See https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
         */
        return 0;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!getClass().isInstance(object)) {
            return false;
        }

        /*
        Transient JPA entities should only be equal if they have referential equality.
        See https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
         */
        Long id = getId();
        if (id == null) {
            return false;
        }

        EntityCRUD other = (EntityCRUD) object;
        return id.equals(other.id);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ id=" + id + " ]";
    }
}
