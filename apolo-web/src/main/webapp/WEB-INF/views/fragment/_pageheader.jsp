<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<s:message code="app.title" var="appTitle"/>

<security:authorize ifAnyGranted="ROLE_AFTER_AUTH_USER">
	<security:authentication property="systemUser" var="currentUser" />
	
	<c:if test="${currentUser != null && currentUser.tenant != null && currentUser.tenant.name != null}">
		<c:set var="appTitle" value="${currentUser.tenant.name}" />
	</c:if>
</security:authorize>

<head>
    <meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
	<title>
		${appTitle}
	</title>
	
	<jsp:include page='/WEB-INF/views/fragment/_css.jsp'></jsp:include>
	
</head>

