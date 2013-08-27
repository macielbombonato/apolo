<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<div class="panel panel-primary">
	<div class="panel-heading">
		<strong>
			<s:message code="user.group.view.title" />
		</strong>
	</div>
	<div class="panel-body">
		<jsp:include page='_permission-form.jsp'></jsp:include>
		
		<div class="form-actions">
			<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_PERMISSION_EDIT">
				<a href='<s:url value="/user-group/edit"></s:url>/${userGroup.id}' class="btn btn-default">
					<i class="glyphicon glyphicon-edit"></i>
					<s:message code="common.edit" />
				</a>
			</security:authorize>
	
			<a href='#' class="btn back btn-default" > 
				<i class="glyphicon glyphicon-backward"></i>
				<s:message code="common.back" />
			</a>
		</div>
	</div>
</div>
