<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<form id="userForm" class="form-search form-inline" role="form" action="<s:url value="/user-custom-field/search"></s:url>" method="post">
	<div class="form-group">
		<div class="input-group">
			<input type="text" id="searchParameter" name="searchParameter" value="${searchParameter}" class="search-query form-control" placeholder='<s:message code="user.custom.field.search.field" />'>
			<span class="input-group-btn">
				<button type="submit" class="btn btn-info">
					<i class="glyphicon glyphicon-search"></i>
					<s:message code="common.search" /> 
				</button>
			</span>
		</div>
	</div>
</form>
