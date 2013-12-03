package br.apolo.data.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Base entity class to be extended by all JPA entities.
 * User: rmberne
 * Date: 27/11/13
 */
@MappedSuperclass
public abstract class BaseEntity<ID extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Transient
    public Boolean isPersisted() {
        return this.getId() != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || (this.getClass() != obj.getClass())) {
            return false;
        }

        BaseEntity<?> other = (BaseEntity<?>) obj;

        if (this.getId() == null && other.getId() == null) {
            return false;
        } else if (this.getId() == null && other.getId() != null) {
            return false;
        } else if (!this.getId().equals(other.getId())) {
            return false;
        }

        return true;
    }
}
