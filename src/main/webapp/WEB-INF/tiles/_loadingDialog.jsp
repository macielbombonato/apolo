<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div class="container-fluid">
	<div class="row-fluid">
		<div id="loadingDialog" class="modal hide fade span1" tabindex="-1" data-role="dialog" style="text-align: center;">
			<img src='<c:url value="/resources/app/img/ajax-loader.gif" />' class="img-circle">
		</div>
	</div>
</div>