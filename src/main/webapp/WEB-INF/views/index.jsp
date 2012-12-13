<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<div class="row-fluid">
	<div class="span12">
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
				<a href="http://tiles.apache.org/" target="_blank">
					Tiles
				</a>
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