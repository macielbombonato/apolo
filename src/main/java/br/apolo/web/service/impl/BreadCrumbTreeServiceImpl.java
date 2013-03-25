package br.apolo.web.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import br.apolo.common.util.MessageBundle;
import br.apolo.web.enums.Navigation;
import br.apolo.web.model.BreadCrumbNode;
import br.apolo.web.model.BreadCrumbTree;
import br.apolo.web.service.BreadCrumbTreeService;

@Service("breadCrumbService")
public class BreadCrumbTreeServiceImpl implements BreadCrumbTreeService {

	@Override
	public void addNode(String nodeName, int level, HttpServletRequest request) {
        String referrer = request.getHeader("referer");
        BreadCrumbNode node = new BreadCrumbNode(nodeName, referrer, level);
        BreadCrumbTree tree = (BreadCrumbTree) request.getSession().getAttribute("breadcrumb");
        if (tree == null) {
            tree = new BreadCrumbTree();
            BreadCrumbNode home = new BreadCrumbNode(MessageBundle.getMessageBundle("breadcrumb.home"), Navigation.HOME.getPath(), 0);
            tree.addNode(home);
            
            request.getSession().setAttribute("breadcrumb", tree);
        }
        tree.addNode(node);
	}

}
