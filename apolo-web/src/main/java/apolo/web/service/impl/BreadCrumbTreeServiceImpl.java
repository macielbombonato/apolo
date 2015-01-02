package apolo.web.service.impl;

import apolo.business.service.BaseService;
import apolo.web.enums.Navigation;
import apolo.web.model.BreadCrumbNode;
import apolo.web.model.BreadCrumbTree;
import apolo.web.service.BreadCrumbTreeService;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("breadCrumbService")
public class BreadCrumbTreeServiceImpl implements BreadCrumbTreeService {

	protected static final Logger LOG = LoggerFactory.getLogger(BaseService.class);
	
	@Override
	public void addNode(Navigation navigation, HttpServletRequest request) {
        try {
			BreadCrumbTree breadCrumb = (BreadCrumbTree) request.getSession().getAttribute("breadcrumb");
			
			// Validation to prevent index out of bounds
			int position = 0;
			
			if (getTreeSize(request) <= navigation.getNodeLevel()) {
				position = getTreeSize(request) - 1;
			} else {
				position = navigation.getNodeLevel() - 1;
			}
			
			if (position < 0) {
				position = 0;
			}
			
			if (breadCrumb == null) {
				breadCrumb = new BreadCrumbTree();
			    BreadCrumbNode home = new BreadCrumbNode(Navigation.USER_INDEX);
			    
			    breadCrumb.getTree().add(home);
			    
			    request.getSession().setAttribute("breadcrumb", breadCrumb);
			}
			
			if ((breadCrumb.getTree().size() - 1) >= (navigation.getNodeLevel()) && navigation.getNodeLevel() != 0) {
				for (int i = (breadCrumb.getTree().size() - 1); i >= (navigation.getNodeLevel()); i--) {
					breadCrumb.getTree().remove(i);
				}
			}
			
			if (navigation.getNodeLevel() != 0) {
			    BreadCrumbNode node = new BreadCrumbNode(navigation);        
			    breadCrumb.getTree().add(navigation.getNodeLevel(), node);        	
			} else {
				for (int i = (breadCrumb.getTree().size() - 1); i >= 1; i--) {
					breadCrumb.getTree().remove(i);
				}
			}
			
			if (navigation.getNodeLevel() > 0) {
				BreadCrumbNode lastNode = breadCrumb.getTree().get(position);
				
				if (!Navigation.USER_INDEX.equals(lastNode.getNavigation())) {
					breadCrumb.getTree().get(position).setNavigation(lastNode.getNavigation());
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
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
