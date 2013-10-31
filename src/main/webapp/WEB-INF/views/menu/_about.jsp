<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<ul class="nav navbar-nav">
	<li class="dropdown">
		<a href="<s:url value="/about"></s:url>" >
			<i class="glyphicon glyphicon-question-sign "></i>
			<span class="hidden-phone">
				<s:message code="view.about" />
			</span>
		</a>
	</li>           
</ul>
