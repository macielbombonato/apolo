package apolo.data.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

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
