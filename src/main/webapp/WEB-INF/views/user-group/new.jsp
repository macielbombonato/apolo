<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<fieldset>
	<legend>
		<s:message code="user.group.new.title" />
	</legend>
	
	<form id="userGroupForm" action="<s:url value="/user-group/save"></s:url>" method="post" class="form-inline">
		<jsp:include page='_permission-form.jsp'></jsp:include>
		
		<div class="form-actions">
			<button type="submit" class="btn btn-primary">
				<s:message code="common.save" /> 
			</button>

			<c:choose>
				<c:when test="${userGroup.id != null}">
					<a href='<s:url value="/user-group/view"></s:url>/${userGroup.id}' class="btn">
						<i class="icon-remove-circle"></i>
						<s:message code="common.cancel" />
					</a>				
				</c:when>
				<c:otherwise>
					<a href='#' class="btn back" > 
						<i class="icon-remove-circle"></i>
						<s:message code="common.cancel" />
					</a>
				</c:otherwise>
			</c:choose>
		</div>
	</form>

</fieldset>
