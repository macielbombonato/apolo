<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<form id="userForm" action="<s:url value="/user/save"></s:url>" method="post">
	<div class="row">
		<div class="span6">
			<label for="name">
				<s:message code="user.username" />
			</label>
			<input type="text" id="name" name="name" value="${user.name}" <c:if test="${readOnly}">readonly="true"</c:if> />
		</div>
		<div class="span6">
			<label for="email">
				<s:message code="user.email" />
			</label>
			<input type="text" id="email" name="email" value="${user.email}" <c:if test="${readOnly}">readonly="true"</c:if>/>
		</div>
	</div>
	
	<c:if test="${not readOnly || changePassword}">
		<div class="row">
			<div class="span6">
				<label for="password">
					<s:message code="user.password" />
				</label>
				<input type="password" id="password" name="password" />
			</div>
			<div class="span6">
				<label for="passwordConfirmation">
					<s:message code="user.password" />
				</label>
				<input type="password" id="passwordConfirmation" name="passwordConfirmation" />
			</div>
		</div>
		
		<div class="row">
			<div class="span4 offset4">
				<button type="submit" class="btn btn-primary">
					<s:message code="common.save" /> 
				</button>
			</div>
		</div>	
	</c:if>
</form>