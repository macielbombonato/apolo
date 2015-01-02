package apolo.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BreadCrumbTree implements Serializable {
	 
	private static final long serialVersionUID = -6089164246689886746L;
	
	private List<BreadCrumbNode> breadCrumb;
 
    /**
     * Constructor
     */
    public BreadCrumbTree() {
        breadCrumb = new ArrayList<BreadCrumbNode>();
    }
 
    /**
     * Return the entire breadcrumb
     * @return
     */
    public List<BreadCrumbNode> getTree() {
        return breadCrumb;
    }
}
