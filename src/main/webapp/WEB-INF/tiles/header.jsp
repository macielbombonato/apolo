<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<div class="navbar navbar-fixed-top">
	<ul class="nav">
		<li class="active"><a href="#">Home</a></li>
		<li><a href="#">Link</a></li>
		<li><a href="#">Link</a></li>
    </ul>
</div>

<div class="well">

	<div class="row">
		<div id="appHeaderlogo" class="three centered columns">
		
		</div>
		
		<div id="appHeaderTitle" class="six centered columns">
			<h1 style="text-align: center;">
				<s:message code="app.title" />
			</h1>
		</div>
		
		<div id="appHeaderRight" class="three columns">
			<p class="right">
				<security:authorize access="isAuthenticated()">
					<s:message code="user.hello" /> 
					<security:authentication property="principal.systemUser.name" />
					[
					<a href='<s:url value="/auth/logout"></s:url>' >
						<s:message code="user.logout" />
					</a>
					]
				</security:authorize>
				
				<security:authorize access="!isAuthenticated()">
					<a href='<s:url value="/auth/login"></s:url>' >
						<s:message code="user.restricted.area.access" />
					</a>
				</security:authorize>			
			</p>
		</div>
	</div>
</div>



