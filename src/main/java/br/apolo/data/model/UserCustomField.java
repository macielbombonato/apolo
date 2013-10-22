package br.apolo.data.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

import br.apolo.common.util.InputLength;
import br.apolo.data.entitylistener.AuditLogListener;
import br.apolo.data.enums.FieldType;

@Entity
@EntityListeners(value = AuditLogListener.class)
@Table(name = "user_custom_field")
@AttributeOverride(name = "id", column = @Column(name = "user_custom_field_id"))
public class UserCustomField extends AuditableBaseEntity {

	private static final long serialVersionUID = 7470619953861313459L;
	
	@Column(name = "type", nullable = false)
	@Type(type = "br.apolo.data.enums.usertype.FieldTypeUserType")
	@NotNull
	private FieldType type;
	
	@Column(name = "name", length = InputLength.NAME, nullable = false, unique = true)
	@NotNull
	@Size(min = 1, max = InputLength.NAME)
	private String name;
	
	@Column(name = "label", length = InputLength.NAME, nullable = false)
	@NotNull
	@Size(min = 1, max = InputLength.NAME)
	private String label;
	
	@Column(name = "default_value", length = InputLength.NAME, nullable = true)
	@Size(min = 0, max = InputLength.NAME)
	private String defaultValue;
	
	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}
