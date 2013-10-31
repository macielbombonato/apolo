package br.apolo.web.model;

import java.io.Serializable;

import br.apolo.web.enums.Navigation;

public class BreadCrumbNode implements Serializable {

	private static final long serialVersionUID = 2751070342668448599L;
	
	private Navigation navigation;
	
	private String url;

	/**
	 * Node constructor
	 * 
	 * @param name of the node
	 * @param value of the node
	 * @param level in the breadcrumb ex root 0 menu 1 sub-menu 2 etc...
	 */
	public BreadCrumbNode(Navigation navigation, String url) {
		this.navigation = navigation;
		this.url = url;
	}

	public String getName() {
		return this.navigation.getNodeName();
	}

	public String getValue() {
		return url;
	}

	public void setValue(String value) {
		this.url = value;
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
