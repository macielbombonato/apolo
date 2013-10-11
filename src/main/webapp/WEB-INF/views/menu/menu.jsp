<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
	
	
<nav id="menu" class="navbar navbar-default navbar-fixed-top" role="navigation">
	<!-- Brand and toggle get grouped for better mobile display -->
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
			<span class="sr-only">Toggle navigation</span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
		</button>
		<a class="navbar-brand" href='<s:url value="/"></s:url>'>
			<strong style="font-family: monospace; font-size: xx-large;">
				<img src='<c:url value="/resources/app/img/favicon.png" />' width="25" height="25" style="width: 25px; height: 25px;" />
				<s:message code="app.title" />
			</strong>
		</a>
	</div>
	
	<!-- Collect the nav links, forms, and other content for toggling -->
	<div class="collapse navbar-collapse navbar-ex1-collapse">
		<jsp:include page='_system-admin.jsp'></jsp:include>
		<jsp:include page='_user.jsp'></jsp:include>
	</div>
</nav>