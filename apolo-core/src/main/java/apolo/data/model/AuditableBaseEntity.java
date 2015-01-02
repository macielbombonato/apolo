package apolo.data.model;

import apolo.data.entitylistener.AuditListener;
import apolo.data.model.interfaces.IAuditableEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
@EntityListeners(value = AuditListener.class)
public abstract class AuditableBaseEntity extends BaseEntity<Long> implements IAuditableEntity {
	private static final long serialVersionUID = 7982765512946321298L;

	@Column(name = "creation_dt")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private User createdBy;

	@Column(name = "last_update_dt")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "last_updated_by")
	private User lastUpdatedBy;

	@Override
	public Date getCreationDate() {
		return creationDate;
	}

	@Override
	public void setCreationDate(Date newCreationDate) {
		this.creationDate = newCreationDate;
	}

	@Override
	public User getCreatedBy() {
		return createdBy;
	}

	@Override
	public void setCreatedBy(User newCreatedBy) {
		this.createdBy = newCreatedBy;
	}

	@Override
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	@Override
	public void setLastUpdateDate(Date newLastUpdateDate) {
		this.lastUpdateDate = newLastUpdateDate;
	}

	@Override
	public User getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	@Override
	public void setLastUpdatedBy(User newLastUpdatedBy) {
		this.lastUpdatedBy = newLastUpdatedBy;
	}

}
