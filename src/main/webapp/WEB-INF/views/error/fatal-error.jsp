<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html lang="en">
	<jsp:include page='/WEB-INF/views/fragment/_pageheader.jsp'></jsp:include>
	<body>
		<jsp:include page='/WEB-INF/views/fragment/_contentheader.jsp'></jsp:include>
		
		<div class="container ">
			<div class="row ">
				<br /><br /><br />
				<div class="span12">
					<div class="alert alert-danger">
						<h2>
							<s:message code="error.fatal.code" />
						</h2>
						<h3>
							<s:message code="error.fatal.title" />
						</h3>
						<p>
							<s:message code="error.fatal.message" />
						</p>
					</div>
				</div>
			</div>
		</div>
		
		<script src="http://code.jquery.com/jquery-latest.js"></script>
		<script type="text/javascript" src='<%=request.getContextPath()%>/resources/plugin/bootstrap/js/bootstrap.min.js'></script>
		
		<jsp:include page='/WEB-INF/views/fragment/_contentfooter.jsp'></jsp:include>
		<jsp:include page='/WEB-INF/views/fragment/_pagefooter.jsp'></jsp:include>
		
	</body>
</html>