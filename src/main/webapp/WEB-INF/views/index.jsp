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
				Spring;
			</li>
			<li>
				Spring MVC;
			</li>
			<li>
				Spring Security;
			</li>
			<li>
				JPA (Hibernate);
			</li>
			<li>
				Tiles;
			</li>
			<li>
				Maven;
			</li>
		</ul>
		<p>
			<s:message code="view.index.tecnologies.explain" htmlEscape="false"/>
		</p>
	</div>
</div>