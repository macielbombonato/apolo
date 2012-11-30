<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

				<form id="login_form" action="<c:url value="/j_spring_security_check" />" method="post">
			
					<div class="row">
						<div class="four centered columns">
							<label for="j_password">
								User
							</label>
							<input id="j_username" name="j_username" type="text" class="required" tabindex="1" />
						</div>
					</div>
			
					<div class="row">
						<div class="four centered columns">
							<label for="j_password">
								Password
							</label>
							<input name="j_password" type="password" class="required" tabindex="2" />
						</div>
					</div>
					
					<div class="row">
						<div class="four centered columns">
							<input type="submit" id="login_button" tabindex="3" value="Login" class="right medium radius button" />
						</div>
					</div>
				</form>



