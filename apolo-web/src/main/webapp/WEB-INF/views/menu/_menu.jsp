<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<security:authentication property="systemUser" var="currentUser" />
<c:url value="/resources/app/img/logo.png" var="logo" />
<c:set var="logoWidth" value="80" />
<c:set var="logoHeight" value="20" />
<c:set var="skin" value="skin-5" />

<c:if test="${currentUser != null && currentUser.tenant != null}">
	<c:if test="${currentUser.tenant.logo != null}">
		<s:url value="/${currentUser.tenant.url}/uploadedfiles/Tenant/${currentUser.tenant.id}/${currentUser.tenant.logo}" var="logo"/>
		<c:set var="logoWidth" value="${currentUser.tenant.logoWidth}" />
		<c:set var="logoHeight" value="${currentUser.tenant.logoHeight}" />		
	</c:if>
	
	<c:if test="${currentUser.tenant.skin != null}">
		<c:set var="skin" value="${currentUser.tenant.skin.code}" />
	</c:if>
</c:if>

<div id="top-nav" class="fixed ${skin}">
	<a class="brand" href='<s:url value="/${currentUser.tenant.url}/user"></s:url>'>
		<strong style="font-family: monospace; font-size: xx-large;">
			<img src='${logo}' width="${logoWidth}" height="${logoHeight}" style="width: ${logoWidth}px; height: ${logoHeight}px;" />
		</strong>
	</a>

	<security:authorize ifAnyGranted="ROLE_AFTER_AUTH_USER">
		<button type="button" class="navbar-toggle pull-left" id="sidebarToggle">
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
		</button>
		<button type="button" class="navbar-toggle pull-left hide-menu" id="menuToggle">
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
		</button>
	</security:authorize>
	<ul class="nav-notification clearfix">
		<li class="profile dropdown">
			<jsp:include page='_menu-user.jsp'></jsp:include>
		</li>
	</ul>
</div><!-- /top-nav-->