package apolo.data.model.base;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class BaseEntity<ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
