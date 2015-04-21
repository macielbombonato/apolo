package apolo.web.service;

import apolo.data.model.User;
import apolo.data.model.UserCustomField;
import apolo.data.model.UserCustomFieldValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserCustomFieldTLD extends TagSupport {
	
	private static final Logger log = LoggerFactory.getLogger(UserCustomFieldTLD.class);
	
	private boolean readOnly;
	
	private UserCustomField field;
	
	private User user;
	
	private List<UserCustomField> fieldList;
	
	public int doStartTag() throws JspException {
		int result = SKIP_BODY;
		
		JspWriter out = pageContext.getOut();
		try {
			Map<Long, String> fieldMap = new HashMap<Long, String>();
			if (user != null && user.getCustomFields() != null && !user.getCustomFields().isEmpty()) {
				for (UserCustomFieldValue field : user.getCustomFields()) {
					fieldMap.put(field.getUserCustomField().getId(), field.getValue());
				}
			}
			
			if (fieldList != null) {
				for (int i = 0; i < fieldList.size(); i++) {
					out.println(fieldList.get(i).getType().getHtml(readOnly, fieldMap, i, fieldList.get(i), user));	
				}				
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
        return result;
	}

	public UserCustomField getField() {
		return field;
	}

	public void setField(UserCustomField field) {
		this.field = field;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<UserCustomField> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<UserCustomField> fieldList) {
		this.fieldList = fieldList;
	}

}
