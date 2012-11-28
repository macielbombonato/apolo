package br.apolo.data.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.Hibernate;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {
	private static final long serialVersionUID = -5925658342836351396L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long newId) {
		this.id = newId;
	}

	@Transient
	public Boolean isPersisted() {
		return this.getId() != null && this.getId() != 0L;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || (Hibernate.getClass(this) != Hibernate.getClass(obj))) {
			return false;
		}

		BaseEntity other = (BaseEntity) obj;

		if (this.getId() == null && other.getId() != null) {
			return false;
		} else if (!this.getId().equals(other.getId())) {
			return false;
		}

		return true;
	}
}
