<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="pl" xml:lang="pl">
	<head>
		<title>
			<s:message code="app.title" />
		</title>
		
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		
		<link rel="stylesheet" href='<c:url value="/resources/plugin/foundation/stylesheets/foundation.min.css" />' />
		<link rel="stylesheet" href='<c:url value="/resources/plugin/foundation/stylesheets/app.css" />' />

	</head>
	
	<body>
		<div class="twelve columns">
			<tiles:insertAttribute name="header" defaultValue="" />
		</div>
		
		<div class="twelve columns">
			<tiles:insertAttribute name="body" defaultValue="" />
		</div>
		
		<div class="twelve columns">
			<tiles:insertAttribute name="footer" defaultValue="" />
		</div>
		
	</body>
</html>