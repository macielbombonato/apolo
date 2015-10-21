package apolo.data.model;

import apolo.data.entitylistener.AuditLogListener;
import apolo.data.enums.FieldType;
import apolo.data.model.base.AuditableBaseEntity;
import apolo.data.util.InputLength;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

@Entity
@EntityListeners(value = AuditLogListener.class)
@Table(name = "user_custom_field")
@AttributeOverride(name = "id", column = @Column(name = "user_custom_field_id"))
public class UserCustomField extends AuditableBaseEntity {

	private static final long serialVersionUID = 7470619953861313459L;
	
	@Column(name = "type", nullable = false)
	@Type(type = "apolo.data.enums.usertype.FieldTypeUserType")
	@NotNull
	private FieldType type;
	
	@ManyToOne
	private Tenant tenant;
	
	@Column(name = "name", length = InputLength.NAME, nullable = false, unique = true)
	@NotNull
	@Size(min = 1, max = InputLength.NAME)
	private String name;
	
	@Column(name = "label", length = InputLength.NAME, nullable = false)
	@NotNull
	@Size(min = 1, max = InputLength.NAME)
	private String label;
	
	@Column(name = "default_value", length = InputLength.MEMO, nullable = true)
	@Size(min = 0, max = InputLength.MEMO)
	private String defaultValue;
	
	@OneToMany(mappedBy = "userCustomField", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("value")
	private Set<UserCustomFieldOption> options;
	
	@Transient
	private Set<String> optionsStringList;
	
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

	public Set<UserCustomFieldOption> getOptions() {
		return options;
	}

	public void setOptions(Set<UserCustomFieldOption> options) {
		this.options = options;
	}

	public Set<String> getOptionsStringList() {
		if (options != null 
				&& !options.isEmpty()) {
			optionsStringList = new HashSet<String>();
			
			for (UserCustomFieldOption option : options) {
				optionsStringList.add(option.getValue());
			}
		}
		
		return optionsStringList;
	}

	public void setOptionsStringList(Set<String> optionsStringList) {
		this.optionsStringList = optionsStringList;
	}

	public Tenant getTenant() {
		return this.tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}
}
