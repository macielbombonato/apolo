<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div class="row">
	<div class="span4 offset4">
		<fieldset>
			<legend>
				<s:message code="user.login.title" />
			</legend>
			
			<c:if test="${error}">
				<div class="alert alert-error">
					<s:message code="user.login.failure" />
				</div>
				
			</c:if>

			<form id="login_form" action="<c:url value="/j_spring_security_check" />" method="post">
				<div>
					<label for="j_password"> 
						<s:message code="user.username" />
					</label> 
					<input id="j_username" name="j_username" type="text" placeholder='<s:message code="user.placeholder.email" />'>
				</div>
				
				<div>
					<label for="j_password"> 
						<s:message code="user.password" />
					</label> 
					<input name="j_password" type="password" />
				</div>
			
				<div>
					<button type="submit" id="login_button" class="btn btn-primary">
						<s:message code="user.login" />
					</button>
				</div>
			</form>
		</fieldset>
	</div>
</div>