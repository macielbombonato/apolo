package br.apolo.web.service;

import javax.servlet.http.HttpServletRequest;

import br.apolo.web.enums.Navigation;

public interface BreadCrumbTreeService {
	 
    /**
     * Add a node in the breadcrumb
     * @param nodeName for displaying
     * @param level in a tree, ex: root 0, menu 1, sub-menu 2 etc... 0
     * is a root and last number ex: 3 is a leaf.
     * @param request
     */
    void addNode(Navigation navigation, HttpServletRequest request);
    
    /**
     * Return the nodes tree size
     * @param request
     * @return int - tree size
     */
    int getTreeSize(HttpServletRequest request);
}
