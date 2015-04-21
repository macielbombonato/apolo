package apolo.data.model;

import apolo.data.enums.DatabaseTransactionType;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "audit_log")
@AttributeOverride(name = "id", column = @Column(name = "audit_id"))
public class AuditLog extends BaseEntity<Long> {
	
	private static final long serialVersionUID = -2751247878900350794L;
	
	@Column(name = "transaction_type", nullable = false)
	@Type(type = "apolo.data.enums.usertype.DatabaseTransactionTypeUserType")
	private DatabaseTransactionType transactionType;
	
	@Column(name = "tenant_id", nullable = false)
	private Long tenantId;

	@Column(name = "entity_name", nullable = false)
	private String entityName;

	@Column(name = "registry_id", nullable = false)
	private Long registryId;

	@Column(name = "operation_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date operationDate;

	@Column(name = "executed_by", nullable = false)
	private Long executedById;
	
	@Transient
	private User executedBy;
	
	@Transient
	private Tenant tenant;

	public DatabaseTransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(DatabaseTransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Long getRegistryId() {
		return registryId;
	}

	public void setRegistryId(Long registryId) {
		this.registryId = registryId;
	}

	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public Long getExecutedById() {
		return executedById;
	}

	public void setExecutedById(Long executedById) {
		this.executedById = executedById;
	}

	public User getExecutedBy() {
		return executedBy;
	}

	public void setExecutedBy(User executedBy) {
		this.executedBy = executedBy;
	}

	public Long getTenantId() {
		return this.tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Tenant getTenant() {
		return this.tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

}
