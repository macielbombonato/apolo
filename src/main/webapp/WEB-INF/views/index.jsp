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
				<div class="media">
					
					<img class="pull-left" src='<c:url value="/resources/app/img/favicon.png" />' />
					
					<div class="media-body">
						 <h4 class="media-heading">
						 	<s:message code="view.index.apolo.title" />
						 </h4>
						 
						<p>
							<s:message code="view.index.hello" htmlEscape="false"/>
						</p>
						<p>
							<s:message code="view.index.tecnologies.msg" htmlEscape="false"/>
						</p>
						<ul>
							<li>
								<a href="http://www.springsource.org/" target="_blank">
									Spring
								</a>
							</li>
							<li>
								<a href="http://static.springsource.org/spring/docs/current/spring-framework-reference/html/mvc.html" target="_blank">
									Spring MVC
								</a>
							</li>
							<li>
								<a href="http://www.springsource.org/features/security" target="_blank">
									Spring Security
								</a>
							</li>
							<li>
								<a href="http://pt.wikipedia.org/wiki/Java_Persistence_API" target="_blank">
									JPA
								</a>
								 (
								<a href="http://www.hibernate.org/" target="_blank">
									Hibernate
								</a>
								 )
							</li>
							<li>
								<a href="http://maven.apache.org/" target="_blank">
									Maven
								</a>
							</li>
							<li>
								<a href="http://twitter.github.com/bootstrap/" target="_blank">
									Bootstrap
								</a>
							</li>
						</ul>
						<p>
							<s:message code="view.index.tecnologies.explain" htmlEscape="false"/>
						</p>
						<p>
							<s:message code="view.index.tecnologies.ps" htmlEscape="false"/>
						</p>
					</div>
				</div>	
			</div>
		</div>
		
		<jsp:include page='/WEB-INF/views/fragment/_contentfooter.jsp'></jsp:include>
		<jsp:include page='/WEB-INF/views/fragment/_pagefooter.jsp'></jsp:include>
	</body>
</html>