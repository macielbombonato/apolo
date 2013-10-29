package br.apolo.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BreadCrumbTree implements Serializable {
	 
	private static final long serialVersionUID = -6089164246689886746L;
	
	private List<BreadCrumbNode> breadCrumb;
 
    private BreadCrumbNode findNode(BreadCrumbNode node) {
        for (BreadCrumbNode n : breadCrumb) {
            if (n.getLevel() == node.getLevel()) {
                return n;
            }
        }
        return null;
    }
 
    /**
     * Constructor
     */
    public BreadCrumbTree() {
        breadCrumb = new ArrayList<BreadCrumbNode>();
    }
 
    /**
     * Add node in the breadcrumb
     * @param node
     */
    public void addNode(BreadCrumbNode node) {
    	BreadCrumbNode c = findNode(node);
        if (c != null) {
            int position = breadCrumb.indexOf(c);
            for (int i = breadCrumb.size() - 1; i >= position; i--) {
                breadCrumb.remove(i);
            }
        } else {
            if (breadCrumb.size() > 0) {
                breadCrumb.get(breadCrumb.size() - 1).setValue(node.getValue());
            }
        }
        breadCrumb.add(node);
 
        List<BreadCrumbNode> nodeToRemove = new ArrayList<BreadCrumbNode>();
        for (int i = 0; i < breadCrumb.size()-1; i++) {             
            if (breadCrumb.get(i).getLevel() >= breadCrumb.get(breadCrumb.size() - 1).getLevel()) {
                nodeToRemove.add(breadCrumb.get(i));
            }
        }
 
        for (BreadCrumbNode remove : nodeToRemove) {
            breadCrumb.remove(remove);
        }
    }
 
    /**
     * Return the entire breadcrumb
     * @return
     */
    public List<BreadCrumbNode> getTree() {
        return breadCrumb;
    }
}
