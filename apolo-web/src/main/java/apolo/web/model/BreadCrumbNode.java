package apolo.web.model;

import apolo.web.enums.Navigation;

import java.io.Serializable;

public class BreadCrumbNode implements Serializable {

	private static final long serialVersionUID = 2751070342668448599L;
	
	private Navigation navigation;
	
	/**
	 * Node constructor
	 * 
	 * @param name of the node
	 * @param value of the node
	 * @param level in the breadcrumb ex root 0 menu 1 sub-menu 2 etc...
	 */
	public BreadCrumbNode(Navigation navigation) {
		this.navigation = navigation;
	}

	public String getName() {
		return this.navigation.getNodeName();
	}

	public int getLevel() {
		return this.navigation.getNodeLevel();
	}

	public Navigation getNavigation() {
		return navigation;
	}

	public void setNavigation(Navigation navigation) {
		this.navigation = navigation;
	}

}
