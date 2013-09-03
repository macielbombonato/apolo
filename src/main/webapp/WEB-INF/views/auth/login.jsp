<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div class="panel panel-primary" style="margin-left: 25%; margin-right: 25%;">
	<div class="panel-heading">
		<strong>
			<s:message code="user.login.title" />
		</strong>
	</div>
	<div class="panel-body">
		<form id="login_form" action="<c:url value="/j_spring_security_check" />" method="post" role="form" class="form-horizontal" style="margin-left: 5%; margin-right: 5%;">
			<div class="form-group">
				<div class="input-group">
					<span class="input-group-addon">
						<span class="glyphicon glyphicon-user"></span>
					</span>
					<input type="text" 
							id="j_username" 
							name="j_username" 
							class="form-control focus" 
							placeholder='<s:message code="user.name" />' 
						/>
				</div>					
			</div>
			
			<div class="form-group">
				<div class="input-group">
					<span class="input-group-addon">
						<span class="glyphicon glyphicon-lock"></span>
					</span>
					<input type="password" 
							id="j_password" 
							name="j_password" 
							class="form-control" 
							placeholder='<s:message code="user.password" />'
						/>
				</div>					
			</div>
			
			<div class="form-group">
				<button type="submit" id="login_button" class="btn btn-primary">
					<s:message code="user.login" />
				</button>
			</div>
		</form>
	</div>
</div>
