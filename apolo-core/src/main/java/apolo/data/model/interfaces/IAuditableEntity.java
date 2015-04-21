package apolo.data.model.interfaces;

import apolo.data.model.User;

import java.util.Date;

public interface IAuditableEntity {

	public Date getCreationDate();

	public void setCreationDate(Date newCreationDate);

	public User getCreatedBy();

	public void setCreatedBy(User newCreatedBy);

	public Date getLastUpdateDate();

	public void setLastUpdateDate(Date newLastUpdateDate);

	public User getLastUpdatedBy();

	public void setLastUpdatedBy(User newLastUpdatedBy);
}
