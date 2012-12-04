<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<form id="userForm" action="<s:url value="/user/save"></s:url>" method="post">
	<fieldset>
		<legend>
			<s:message code="user.new.title" />
		</legend>
		
		<div class="row">
			<div>
				<label for="name">
					<s:message code="user.username" />
				</label>
				<input type="text" id="name" name="name" />
			</div>
		</div>
		<div class="row">
			<div>
				<label for="email">
					<s:message code="user.email" />
				</label>
				<input type="text" id="email" name="email" />
			</div>
		</div>
		<div class="row">
			<div>
				<label for="email">
					<s:message code="user.password" />
				</label>
				<input type="password" id="password" name="password" />
			</div>
		</div>
		<div class="row">
			<div>
				<button type="submit" class="right medium radius button">
					<s:message code="common.save" /> 
				</button>
			</div>
		</div>
	</fieldset>
</form>
