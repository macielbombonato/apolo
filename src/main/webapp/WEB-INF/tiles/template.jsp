<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html lang="en">
	<head>
	    <meta charset="utf-8">
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
		
		<title>
			<s:message code="app.title" />
		</title>
		
		<jsp:include page='/WEB-INF/views/include/css.jsp'></jsp:include>
		
	</head>
	
	<body>
		<tiles:insertAttribute name="header" defaultValue="" />
		
		<div class="container ">
			<jsp:include page='/WEB-INF/views/include/message.jsp'></jsp:include>
			
			<div class="row ">
				<div class="span12">
					<tiles:insertAttribute name="body" defaultValue="" />
				</div>
			</div>
		</div>
		
		<tiles:insertAttribute name="footer" defaultValue="" />

		<jsp:include page='/WEB-INF/views/include/loadingDialog.jsp'></jsp:include>
		
		<jsp:include page='/WEB-INF/views/include/editDataDialog.jsp'></jsp:include>
		
		<jsp:include page='/WEB-INF/views/include/removeConfirmationDialog.jsp'></jsp:include>
		
		<jsp:include page='/WEB-INF/views/include/javascript.jsp'></jsp:include>
	</body>
</html>
