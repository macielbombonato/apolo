<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div id="loginScreen" class="row-fluid">
	<div class="span4">
		<fieldset>
			<legend>
				<s:message code="user.login.title" />
			</legend>

			<form id="login_form" action="<c:url value="/j_spring_security_check" />" method="post" class="form-horizontal">
				<div class="control-group">
					<input type="text" id="j_username" name="j_username" class="input-block-level" style="margin-bottom: 15px;" size="30" placeholder='<s:message code="user.username" />' />
					
					<input type="password" id="j_password" name="j_password" class="input-block-level" style="margin-bottom: 15px;" size="30" placeholder='<s:message code="user.password" />'/>
					
					<button type="submit" id="login_button" class="btn btn-primary">
						<s:message code="user.login" />
					</button>
				</div>
			</form>
		</fieldset>
	</div>
</div>
