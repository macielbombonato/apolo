<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html lang="en">
	<jsp:include page='/WEB-INF/views/fragment/_pageheader.jsp'></jsp:include>
	<body>
		<jsp:include page='/WEB-INF/views/fragment/_contentheader.jsp'></jsp:include>
		
		<div class="container ">
			<div class="row ">
				<ul class="list-group" style="text-align: justify">
					<li class="list-group-item">
					    <h4 class="list-group-item-heading">
					    	<s:message code="view.about.portal" htmlEscape="false"/>
					    </h4>
					    <p class="list-group-item-text">
							<s:message code="view.about.portal.text" htmlEscape="false"/>
					    </p>
					</li>
					<li class="list-group-item">
					    <h4 class="list-group-item-heading">
					    	<s:message code="view.about.use" htmlEscape="false"/>
					    </h4>
					    <p class="list-group-item-text">
							<s:message code="view.about.use.text" htmlEscape="false"/>
					    </p>
					</li>
					<li class="list-group-item">
					    <h4 class="list-group-item-heading">
					    	<s:message code="view.about.extends" htmlEscape="false"/>
					    </h4>
					    <p class="list-group-item-text">
							<s:message code="view.about.extends.text" htmlEscape="false"/>
					    </p>
					</li>
					<li class="list-group-item">
					    <h4 class="list-group-item-heading">
					    	<s:message code="view.about.fonts" htmlEscape="false"/>
					    </h4>
					    <p class="list-group-item-text">
							<s:message code="view.about.fonts.text" htmlEscape="false"/>
					    </p>
					</li>
					<li class="list-group-item">
					    <h4 class="list-group-item-heading">
					    	<s:message code="view.about.features" htmlEscape="false"/>
					    </h4>
					    <p class="list-group-item-text">
							<s:message code="view.about.features.text" htmlEscape="false"/>
					    </p>
					</li>
					<li class="list-group-item">
					    <h4 class="list-group-item-heading">
					    	<s:message code="view.about.authors" htmlEscape="false"/>
					    </h4>
					    <p class="list-group-item-text">
					    	<s:message code="view.about.authors.text" htmlEscape="false"/>
					    </p>
					</li>
					<li class="list-group-item">
					    <h4 class="list-group-item-heading">
					    	<s:message code="view.about.contact" htmlEscape="false"/>
					    </h4>
					    <p class="list-group-item-text">
					    	<s:message code="view.about.contact.text" htmlEscape="false"/>
					    </p>
					</li>
				</ul>
			</div>
		</div>
		
		<jsp:include page='/WEB-INF/views/fragment/_contentfooter.jsp'></jsp:include>
		<jsp:include page='/WEB-INF/views/fragment/_pagefooter.jsp'></jsp:include>
	</body>
</html>