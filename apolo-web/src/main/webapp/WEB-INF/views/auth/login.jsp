<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html lang="en">
	<jsp:include page='/WEB-INF/views/fragment/_pageheader.jsp'></jsp:include>
	<body class="overflow-hidden">
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
							<s:message code="user.login.title" />
						</div>
					</div>
					<div class="panel-body">
						<c:url value="/login" var="loginUrl"/>
						<form id="login_form" action="${loginUrl}" method="post" role="form" class="form-login">
							<div class="form-group">
								<div class="input-group">
									<span class="input-group-addon">
										<span class="glyphicon glyphicon-user"></span>
									</span>
									<input type="email" 
											id="username" 
											name="username" 
											class="form-control input-sm focus" 
											placeholder='<s:message code="user.name" />' 
										/>
								</div>
							</div>
							<div class="form-group">
								<div class="input-group">
									<span class="input-group-addon">
										<span class="fa fa-key"></span>
									</span>
									<input type="password" 
											id="password" 
											name="password" 
											class="form-control input-sm" 
											placeholder='<s:message code="user.password" />'
										/>
								</div>
							</div>
							<hr/>
							
							<!-- Language selection -->
							<c:set var="langs" value="<%=apolo.data.enums.Language.values()%>"/>
					
							<div class="pull-left">
								<c:forEach items="${langs}" var="lang">
									<a href='<s:url value="/change-locale/${lang.code}"></s:url>'>
										<img src='<c:url value="${lang.icon}" />' 
												width="20" 
												height="20" 
												style="width: 20px; height: 20px;" 
											/>
									</a>
								</c:forEach>
							</div>
							<!-- END Language selection -->
							
							<button type="submit" id="login_button" class="btn btn-success btn-sm pull-right">
								<i class="fa fa-sign-in"></i>
								<s:message code="user.login" />
							</button>
						</form>
					</div>
				</div><!-- /panel -->
			</div><!-- /login-widget -->
		
			<div style="display:none;">
				<jsp:include page='/WEB-INF/views/fragment/_contentfooter.jsp'></jsp:include>
			</div>
			
			<jsp:include page='/WEB-INF/views/fragment/_pagefooter.jsp'></jsp:include>			
		</div>
	</body>
</html>