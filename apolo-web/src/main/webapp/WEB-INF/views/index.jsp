<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html lang="en">
	<jsp:include page='/WEB-INF/views/fragment/_pageheader.jsp'></jsp:include>
	<body class="overflow-hidden">
		<div id="overlay" class="transparent"></div>
	
		<div id="wrapper" class="preload">
			<jsp:include page='/WEB-INF/views/fragment/_contentheader.jsp'></jsp:include>
				
			<div id="main-container">
				
				<jsp:include page='/WEB-INF/views/fragment/_breadcrumb.jsp'></jsp:include>
					
				<jsp:include page='/WEB-INF/views/fragment/_message.jsp'></jsp:include>		<div class="container ">
					
				<div class="row ">
					<div class="media">
						<div class="col-md-4">
							<img class="img-responsive" src='<c:url value="/resources/app/img/logo.png" />' />
						</div>
						<div class="media-body col-md-8" style="text-align: justify">
							<h4 class="media-heading">
								<s:message code="view.index.title" />
							</h4>
								 
							<p>
								<s:message code="view.index.hello" htmlEscape="false"/>
							</p>
						</div>
					</div>	
				</div>
			</div>
			</div>
			
			<jsp:include page='/WEB-INF/views/fragment/_contentfooter.jsp'></jsp:include>
			<jsp:include page='/WEB-INF/views/fragment/_pagefooter.jsp'></jsp:include>			
		</div>
	</body>
</html>