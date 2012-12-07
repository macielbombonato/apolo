<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html lang="en">
	<head>
		<title>
			<s:message code="app.title" />
		</title>
		
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		
		<link rel="stylesheet" href='<c:url value="/resources/plugin/bootstrap/css/bootstrap.min.css" />' />
		<link rel="stylesheet" href='<c:url value="/resources/plugin/bootstrap/css/bootstrap-responsive.min.css" />' />
		
	</head>
	
	<body>
		<div class="container">
			<div class="row">
				<div class="span12">
					<tiles:insertAttribute name="header" defaultValue="" />
				</div>
				
			</div>
			<div class="row">
				<div class="span12">
					<tiles:insertAttribute name="body" defaultValue="" />
				</div>
			</div>
			<div class="row">
				<div class="span12">
					<tiles:insertAttribute name="footer" defaultValue="" />
				</div>
			</div>
		</div>
		
		<script src="http://code.jquery.com/jquery-latest.js"></script>
		<script type="text/javascript" src='<c:url value="/resources/plugin/bootstrap/js/bootstrap.min.js" />'></script>
		
	</body>
</html>