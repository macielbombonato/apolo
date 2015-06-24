package apolo.data.model;

import apolo.data.entitylistener.AuditListener;
import apolo.data.model.interfaces.IAuditableEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EntityListeners(value = AuditListener.class)
public abstract class AuditableBaseEntity extends BaseEntity<Long> implements IAuditableEntity {
	private static final long serialVersionUID = 7982765512946321298L;

	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	private Date createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private User createdBy;

	@Column(name = "updated_at")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	private Date updatedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by")
	private User updatedBy;

	@Transient
	private Boolean disableAuditLog;

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Boolean isDisableAuditLog() {
		return disableAuditLog;
	}

	public void setDisableAuditLog(Boolean disableAuditLog) {
		this.disableAuditLog = disableAuditLog;
	}
}
