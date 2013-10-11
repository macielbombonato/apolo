<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<div class="panel panel-primary">
	<div class="panel-heading">
		<strong>
			<s:message code="user.group.new.title" />
		</strong>
	</div>
	<div class="panel-body">
		<form id="userGroupForm" action="<s:url value="/user-group/save"></s:url>" method="post" role="form">
			<jsp:include page='_permission-form.jsp'></jsp:include>
			
			<div class="form-actions">
				<button type="submit" class="btn btn-primary">
					<i class="glyphicon glyphicon-ok"></i>
					<s:message code="common.save" /> 
				</button>
	
				<c:choose>
					<c:when test="${userGroup.id != null}">
						<a href='<s:url value="/user-group/view"></s:url>/${userGroup.id}' class="btn btn-default">
							<i class="glyphicon glyphicon-remove-circle"></i>
							<s:message code="common.cancel" />
						</a>				
					</c:when>
					<c:otherwise>
						<a href='#' class="btn back btn-default" > 
							<i class="glyphicon glyphicon-remove-circle"></i>
							<s:message code="common.cancel" />
						</a>
					</c:otherwise>
				</c:choose>
			</div>
		</form>
	</div>
</div>
