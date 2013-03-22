package br.apolo.web.model;

public class BreadCrumbNode {

	private String name;
	private String url;
	private int level;

	/**
	 * Node constructor
	 * 
	 * @param name of the node
	 * @param value of the node
	 * @param level in the breadcrumb ex root 0 menu 1 sub-menu 2 etc...
	 */
	public BreadCrumbNode(String name, String value, int level) {
		this.name = name;
		this.url = value;
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return url;
	}

	public void setValue(String value) {
		this.url = value;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
