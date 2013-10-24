<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ taglib prefix="apolo" uri="/WEB-INF/taglib/apolo.tld" %>

<div style="height:60px; position:relative; ">
	&nbsp;
</div>

<div class="container ">
	<div class="row ">
		<div class="footer">
			<div class="footer_contents"> 
				<p class="muted">
					<small>
						<apolo:sysadmin />
						<br />
						<s:message code="footer.message" htmlEscape="false"/>
					</small>
				</p>
			</div>
		</div>
	</div>
</div>