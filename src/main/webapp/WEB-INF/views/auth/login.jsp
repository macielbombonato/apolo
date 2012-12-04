<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div class="row">
	<div class="five centered columns">
		<form id="login_form"
			action="<c:url value="/j_spring_security_check" />" method="post">
			
			<fieldset>
				<legend>
					<s:message code="user.login.title" />
				</legend>
				
				<div class="row">
		
					<div>
						<div class="twelve columns">
							<label for="j_password"> 
								<s:message code="user.username" />
							</label> 
							<input id="j_username" name="j_username" type="text" class="twelve" />
						</div>
					</div>
				
					<div>
						<div class="twelve columns">
							<label for="j_password"> 
								<s:message code="user.password" />
							</label> 
							<input name="j_password" type="password" class="twelve" />
						</div>
					</div>
				
					<div>
						<div class="twelve columns">
							<input type="submit" id="login_button" class="btn" value='<s:message code="user.login" />' />
						</div>
					</div>
				
				</div>
			</fieldset>
		</form>
	</div>
</div>