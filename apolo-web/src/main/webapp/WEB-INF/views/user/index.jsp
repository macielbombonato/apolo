<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">
	<jsp:include page='/WEB-INF/views/fragment/_pageheader.jsp'></jsp:include>
	<body class="overflow-hidden">
		<div id="overlay" class="transparent"></div>

		<div id="wrapper" class="preload">
			<jsp:include page='/WEB-INF/views/fragment/_contentheader.jsp'></jsp:include>
			
			<div id="main-container">
			
				<jsp:include page='/WEB-INF/views/fragment/_breadcrumb.jsp'></jsp:include>
				
				<jsp:include page='/WEB-INF/views/fragment/_message.jsp'></jsp:include>
	
				<div class="panel panel-default">
					<div class="panel-heading">
						<center>
							<strong>
								<s:message code="user.index.title" />
							</strong>
						</center>
					</div>
					<div class="panel-body">
						<s:message code="user.index.body" />
					</div>
				</div>
			</div>

			<jsp:include page='/WEB-INF/views/fragment/_contentfooter.jsp'></jsp:include>
			<jsp:include page='/WEB-INF/views/fragment/_pagefooter.jsp'></jsp:include>
		</div>
	</body>
</html>