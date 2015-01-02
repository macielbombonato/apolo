<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html lang="en">
	<jsp:include page='/WEB-INF/views/fragment/_pageheader.jsp'></jsp:include>
	
	<body class="overflow-hidden"
			<security:authorize  ifAnyGranted="ROLE_AFTER_AUTH_USER"> 
				onload="window.location.href = '<s:url value="/"></s:url>' "
			</security:authorize> 
		>
		<div id="overlay" class="transparent"></div>

		<div class="login-wrapper">
			<jsp:include page='/WEB-INF/views/fragment/_message.jsp'></jsp:include>
			
			<div class="login-widget">
				<div class="panel panel-default">
					<div class="panel-heading clearfix">
						<div class="col-sm-4 pull-left">
							<img class="img-responsive" src='<c:url value="/resources/app/img/logo.png" />' width="80" style="width: 80px;" />
						</div>
						<div class="col-sm-8 pull-left"> 
							<s:message code="user.token.title" />
						</div>
					</div>
					<div class="panel-body">
						<c:url value="/auth/validate-token" var="loginUrl"/>
						<form id="login_form" action="${loginUrl}" method="post" role="form" class="form-login">
							<div class="form-group">
								<div class="input-group">
									<span class="input-group-addon">
										<span class="fa fa-lock fa-lg"></span>
									</span>
									<input type="password" 
											id="token" 
											name="token" 
											class="form-control input-sm focus" 
											placeholder='<s:message code="user.type.token" />' 
										/>
								</div>					
							</div>
							
							<hr/>
							
							<button type="submit" id="login_button" class="btn btn-success btn-sm pull-right">
								<s:message code="user.token.validate" />
							</button>
						</form>
					</div>
				</div>
			</div>
		
			<jsp:include page='/WEB-INF/views/fragment/_pagefooter.jsp'></jsp:include>
		</div>
	</body>
</html>