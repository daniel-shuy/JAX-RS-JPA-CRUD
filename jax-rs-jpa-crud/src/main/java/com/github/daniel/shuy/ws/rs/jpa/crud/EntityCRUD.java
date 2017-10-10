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
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EntityCRUD) || !this.getClass().isInstance(object)) {
            return false;
        }
        EntityCRUD other = (EntityCRUD) object;
        if (this.id == null) {
            return (other.id == null);
        } else {
            return this.id.equals(other.id);
        }
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ id=" + id + " ]";
    }
}
