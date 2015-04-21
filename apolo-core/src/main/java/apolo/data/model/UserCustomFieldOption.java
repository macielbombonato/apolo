package apolo.data.model;

import apolo.data.util.InputLength;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user_custom_field_option")
@AttributeOverride(name = "id", column = @Column(name = "user_custom_field_option_id"))
public class UserCustomFieldOption extends BaseEntity<Long> {

	private static final long serialVersionUID = 7470619953861313459L;
	
	@Column(name = "value", length = InputLength.MEMO, nullable = true)
	@Size(min = 0, max = InputLength.MEMO)
	private String value;
	
	@ManyToOne
	private UserCustomField userCustomField;

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
}
