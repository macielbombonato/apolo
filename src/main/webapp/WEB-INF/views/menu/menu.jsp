<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
	
	
<div id="menu" class="navbar navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container-fluid">
			<a class="btn btn-navbar" data-toggle="collapse"
				data-target=".navbar-responsive-collapse"> 
				<span class="icon-bar"></span> 
				<span class="icon-bar"></span> 
				<span class="icon-bar"></span>
			</a> 
			
			<a class="brand" href='<s:url value="/"></s:url>'>
				<img src='<c:url value="/resources/app/img/favicon.png" />' width="20" height="20">
				<strong><s:message code="app.title" /></strong>
			</a>
			
			<div class="nav-collapse collapse navbar-responsive-collapse">
				<jsp:include page='_user.jsp'></jsp:include>
			</div>
			<!-- /.nav-collapse -->
		</div>
	</div>
	<!-- /navbar-inner -->
</div>