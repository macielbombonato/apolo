package br.apolo.web.service;

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

public class SystemAdministratorTLD extends TagSupport {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(SystemAdministratorTLD.class);
	
	@Autowired
	private UserService userService;
	
	public int doStartTag() throws JspException {
		if (userService == null) {
			ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();

			userService = (UserService) ctx.getBean("userService");
		}
		
        try {
        	User user = userService.getSystemAdministrator();
        	
        	if (user != null) {
                JspWriter out = pageContext.getOut();
                out.println(MessageBundle.getMessageBundle("common.admin") + ": <a href='mailto:" + user.getEmail() + "' >" + user.getName() + "</a>");        		
        	}

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        
        return SKIP_BODY;

	}

}
