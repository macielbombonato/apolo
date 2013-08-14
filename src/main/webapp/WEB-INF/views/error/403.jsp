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
		
		<link rel="stylesheet" href='<%=request.getContextPath()%>/resources/plugin/bootstrap/css/bootstrap.min.css' />
		
	</head>
	
	<body>
		<div class="container ">
			<div class="row ">
				<div class="span12">
					<jsp:include page='/WEB-INF/tiles/header.jsp'></jsp:include>
				</div>
			</div>
			<div class="row ">
				<br /><br /><br />
				<div class="span12">
					<div class="alert alert-error">
						<h2>
							403
						</h2>
						<h3>
							<s:message code="error.403" />
						</h3>
						<p>
							<s:message code="error.403.msg" />
						</p>
					</div>
				</div>
			</div>
			<div class="row ">
				<div class="span12">
					<jsp:include page='/WEB-INF/tiles/footer.jsp'></jsp:include>
				</div>
			</div>
		</div>
		
		<script src="http://code.jquery.com/jquery-latest.js"></script>
		<script type="text/javascript" src='<%=request.getContextPath()%>/resources/plugin/bootstrap/js/bootstrap.min.js'></script>
		
	</body>
</html>