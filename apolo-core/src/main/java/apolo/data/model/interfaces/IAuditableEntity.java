package apolo.data.model.interfaces;

import apolo.data.model.User;

import java.util.Date;

public interface IAuditableEntity {

	Date getCreatedAt();

	void setCreatedAt(Date newCreatedAt);

	User getCreatedBy();

	void setCreatedBy(User newCreatedBy);

	Date getUpdatedAt();

	void setUpdatedAt(Date newUpdatedAt);

	User getUpdatedBy();

	void setUpdatedBy(User newUpdatedBy);
}
