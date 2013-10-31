package br.apolo.web.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import br.apolo.web.enums.Navigation;
import br.apolo.web.model.BreadCrumbNode;
import br.apolo.web.model.BreadCrumbTree;
import br.apolo.web.service.BreadCrumbTreeService;

@Service("breadCrumbService")
public class BreadCrumbTreeServiceImpl implements BreadCrumbTreeService {

	@Override
	public void addNode(Navigation navigation, HttpServletRequest request) {
        BreadCrumbTree breadCrumb = (BreadCrumbTree) request.getSession().getAttribute("breadcrumb");
        
        if (breadCrumb == null) {
        	breadCrumb = new BreadCrumbTree();
            BreadCrumbNode home = new BreadCrumbNode(Navigation.HOME, Navigation.HOME.getPath());
            
            breadCrumb.getTree().add(home);
            
            request.getSession().setAttribute("breadcrumb", breadCrumb);
        }
        
        if ((breadCrumb.getTree().size() - 1) >= (navigation.getNodeLevel()) && navigation.getNodeLevel() != 0) {
        	for (int i = (breadCrumb.getTree().size() - 1); i >= (navigation.getNodeLevel()); i--) {
        		breadCrumb.getTree().remove(i);
			}
        }
        
        if (navigation.getNodeLevel() != 0) {
            BreadCrumbNode node = new BreadCrumbNode(navigation, navigation.getUrl(request));        
            breadCrumb.getTree().add(navigation.getNodeLevel(), node);        	
        } else {
        	for (int i = (breadCrumb.getTree().size() - 1); i >= 1; i--) {
        		breadCrumb.getTree().remove(i);
			}
        }
        
        if (navigation.getNodeLevel() > 0) {
        	BreadCrumbNode lastNode = breadCrumb.getTree().get(navigation.getNodeLevel() - 1);
        	
        	if (!Navigation.HOME.equals(lastNode.getNavigation())) {
        		breadCrumb.getTree().get(navigation.getNodeLevel() - 1).setValue(lastNode.getNavigation().getUrl(request));
        	}
        }
	}
	
	@Override
	public int getTreeSize(HttpServletRequest request) {
		int result = 0;
		
		BreadCrumbTree tree = (BreadCrumbTree) request.getSession().getAttribute("breadcrumb");
		
		if (tree != null && tree.getTree() != null) {
			result = tree.getTree().size();
		}
		
		return result;
	}

}
