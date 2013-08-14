<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>


<div class="row ">
	<div class="span12">
		<div class="alert alert-error">
			<h2>
				<s:message code="error.fatal.code" />
			</h2>
			<h3>
				<s:message code="error.fatal.title" />
			</h3>
			<p>
				<s:message code="error.fatal.message" />
			</p>
		</div>
	</div>
</div>
