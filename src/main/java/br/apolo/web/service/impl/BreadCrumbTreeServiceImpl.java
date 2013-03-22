package br.apolo.web.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

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
            request.getSession().setAttribute("breadcrumb", tree);
        }
        tree.addNode(node);
	}

}
