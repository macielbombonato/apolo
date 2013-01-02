package br.apolo.data.model.interfaces;

import java.util.Date;

import br.apolo.data.model.User;

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
