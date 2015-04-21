package apolo.business.model;

import apolo.data.model.User;

import java.io.Serializable;

public class InstallFormModel implements Serializable {
	
	private static final long serialVersionUID = 6702732675706021107L;
	
	private User user;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
