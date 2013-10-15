package br.apolo.web.service;

import javax.servlet.RequestDispatcher;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import br.apolo.business.service.UserService;
import br.apolo.common.util.MessageBundle;
import br.apolo.data.model.User;
import br.apolo.web.enums.Navigation;

public class SystemAdministratorTLD extends TagSupport {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(SystemAdministratorTLD.class);
	
	@Autowired
	private UserService userService;
	
	private User systemAdmin;
	
	public int doStartTag() throws JspException {
		int result = SKIP_BODY;
		
		if (userService == null) {
			ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();

			userService = (UserService) ctx.getBean("userService");
		}
		
        try {
        	if (systemAdmin == null) {
        		systemAdmin = userService.getSystemAdministrator();
        	}
        	
        	if (systemAdmin != null) {
                JspWriter out = pageContext.getOut();
                out.println(MessageBundle.getMessageBundle("common.admin") + ": <a href='mailto:" + systemAdmin.getEmail() + "' >" + systemAdmin.getName() + "</a>");        		
        	} else {
        		String caminho = Navigation.INSTALL.getPath();
        		RequestDispatcher redirection = pageContext.getRequest().getRequestDispatcher(caminho);
        		redirection.forward(pageContext.getRequest(), pageContext.getResponse());
        		
        		result = SKIP_PAGE;
        	}

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        
        return result;

	}

}
