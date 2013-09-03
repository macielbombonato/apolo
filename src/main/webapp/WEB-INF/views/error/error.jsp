<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<div class="row ">
	<div class="span12">
		<div class="alert alert-danger">
			<h2>
				${code}
			</h2>
			<h3>
				${title}
			</h3>
			<p>
				${message}
			</p>
		</div>
	</div>
</div>
