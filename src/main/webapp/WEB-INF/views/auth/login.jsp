<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div class="row ">
	<div class="col-lg-12">
		<fieldset>
			<legend>
				<s:message code="user.login.title" />
			</legend>

			<form id="login_form" action="<c:url value="/j_spring_security_check" />" method="post" role="form" class="form-horizontal">
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
		</fieldset>
	</div>
</div>
