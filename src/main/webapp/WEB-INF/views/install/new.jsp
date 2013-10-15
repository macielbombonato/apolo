<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">
	<jsp:include page='/WEB-INF/views/fragment/_pageheader.jsp'></jsp:include>
	<body>
		<div class="container ">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<strong>
						<s:message code="install.title" />
					</strong>
				</div>
				<div class="panel-body">
					<p>
						<s:message code="install.msg.instructions" htmlEscape="false"/>
					</p>
				
					<form id="systemForm" action="<s:url value="/install/save"></s:url>" role="form" method="post" enctype="multipart/form-data" target="_self">
						
						<div class="form-group">
							<label for="name" class="control-label">
								<s:message code="user.name" />
							</label>
							<input type="text" id="user.name" name="user.name" class="form-control" value="${install.user.name}" />
						</div>
						
						<div class="form-group">
							<label for="email" class="control-label">
								<s:message code="user.email" />
							</label>
							<input type="text" id="user.email" name="user.email" class="form-control" value="${install.user.email}" />
						</div>
						
						<div class="form-group">
							<label for="password" class="control-label">
								<s:message code="user.password" />
							</label>
							<div class="input-append">
								<input type="password" id="user.password" name="user.password" class="form-control"/>
							</div>
						</div>
						
						<div class="form-group">
							<label for="picturefiles" class="control-label">
								<s:message code="user.picturefiles" />
							</label>
							<input type="file" class="form-control" name="user.picturefiles[0]" />
						</div>
						
						<div class="form-actions">
							<button type="submit" class="btn btn-primary" onclick="submitPage();">
								<i class="glyphicon glyphicon-ok"></i>
								<s:message code="common.save" /> 
							</button>
						</div>
					</form>
				</div>
			</div>
		</div>
		
		<jsp:include page='/WEB-INF/views/fragment/_pagefooter.jsp'></jsp:include>
		
		<script type="text/javascript" src='<c:url value="/resources/app/js/install.js" />'></script>
	</body>
</html>