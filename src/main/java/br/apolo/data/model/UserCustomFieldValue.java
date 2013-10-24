package br.apolo.data.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import br.apolo.common.util.InputLength;

@Entity
@Table(name = "user_custom_field_value")
@AttributeOverride(name = "id", column = @Column(name = "user_custom_field_value_id"))
public class UserCustomFieldValue extends BaseEntity {

	private static final long serialVersionUID = 7470619953861313459L;
	
	@ManyToOne
	private UserCustomField userCustomField;
	
	@ManyToOne
	private User user;
	
	@Column(name = "value", length = InputLength.MEMO, nullable = true)
	@Size(min = 0, max = InputLength.MEMO)
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public UserCustomField getUserCustomField() {
		return userCustomField;
	}

	public void setUserCustomField(UserCustomField userCustomField) {
		this.userCustomField = userCustomField;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
