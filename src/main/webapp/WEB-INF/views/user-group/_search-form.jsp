<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<form id="userForm" class="form-search" action="<s:url value="/user-group/search"></s:url>" method="post">
	<div class="input-append">
		<input type="text" id="param" name="param" class="search-query" placeholder='<s:message code="user.group.search.field" />'>
		<button type="submit" class="btn">
			<s:message code="common.search" /> 
		</button>
	</div>
</form>
