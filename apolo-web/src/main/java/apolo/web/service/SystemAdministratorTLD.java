package apolo.web.service;

import apolo.business.service.UserService;
import apolo.common.util.MessageBundle;
import apolo.data.model.User;
import apolo.web.enums.Navigation;

import javax.servlet.RequestDispatcher;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

public class SystemAdministratorTLD extends TagSupport {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(SystemAdministratorTLD.class);
	
	@Autowired
	private UserService userService;
	
	/**
	 * The System administrator is a class field to be re used and go less times to data base.
	 */
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
        	
        	/*
        	 * If systemAdmin do not exist, the system has to be setuped. The user will be forwarded to this page
        	 */
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
