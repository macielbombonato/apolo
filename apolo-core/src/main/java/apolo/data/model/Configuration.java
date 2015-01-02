package apolo.data.model;

import apolo.data.entitylistener.AuditLogListener;
import apolo.data.enums.ConfigField;
import apolo.data.util.InputLength;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

@Entity
@EntityListeners(value = AuditLogListener.class)
@Table(name = "configuration")
@AttributeOverride(name = "id", column = @Column(name = "configuration_id"))
public class Configuration extends AuditableBaseEntity {

	private static final long serialVersionUID = 7470619953861313459L;

	@ManyToOne
	private Tenant tenant;
	
	@Column(name = "type", nullable = false)
	@Type(type = "apolo.data.enums.usertype.ConfigFieldUserType")
	@NotNull
	private ConfigField field;
	
	@Column(name = "value", length = InputLength.MEMO, nullable = false)
	@NotNull
	@Size(min = 1, max = InputLength.MEMO)
	private String value;

	public Tenant getTenant() {
		return this.tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public ConfigField getField() {
		return this.field;
	}

	public void setField(ConfigField field) {
		this.field = field;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
