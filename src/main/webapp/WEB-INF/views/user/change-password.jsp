<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<fieldset>
	<legend>
		<s:message code="user.change-password.title" />
	</legend>
	
	<form id="userForm" action="<s:url value="/user/change-password-save"></s:url>" method="post">
		<jsp:include page='_user-form.jsp'></jsp:include>
		
		<div class="form-actions">
			<button type="submit" class="btn btn-primary">
				<s:message code="common.save" /> 
			</button>

			<a href='<s:url value="/"></s:url>' class="btn">
				<s:message code="common.cancel" />
			</a>
		</div>	
	</form>
	
</fieldset>
