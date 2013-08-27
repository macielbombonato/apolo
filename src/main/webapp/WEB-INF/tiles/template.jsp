<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html lang="en">
	<head>
	    <meta charset="utf-8">
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
		
		<title>
			<s:message code="app.title" />
		</title>
		
		<link rel="icon" type="image/png" href='<c:url value="/resources/app/img/favicon.png" />'>
		
		<link href='<c:url value="/resources/plugin/bootstrap/css/bootstrap.css" />' rel="stylesheet" media="screen"/>
		<link href='<c:url value="/resources/plugin/bootstrap/css/bootstrap-theme.css" />' rel="stylesheet" />
		
		<link href='<c:url value="/resources/plugin/jquery/themes/base/jquery-ui.css" />' rel="stylesheet" />
		
		<link href='<c:url value="/resources/plugin/jquery-chosen/chosen.css" />' rel='stylesheet'>
		
		<link href='<c:url value="/resources/plugin/tagit/css/jquery.tagit.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/tagit/css/tagit.ui-zendesk.css" />' rel='stylesheet'>
		
		<link href='<c:url value="/resources/plugin/cleditor/jquery.cleditor.css" />' rel='stylesheet'>
		
		<link href='<c:url value="/resources/plugin/mediaelement/mediaelementplayer.min.css" />' rel='stylesheet'>
		
		<link href='<c:url value="/resources/app/css/app.css" />' rel='stylesheet'>
		
	</head>
	
	<body>
		<tiles:insertAttribute name="header" defaultValue="" />
		
		<div class="container ">
			<c:if test="${error || warn || msg}">
				<div class="row ">
					<div class="span12">
						<c:choose>
							<c:when test="${error}">
								<div class="alert alert-error">
									${message}
								</div>		
							</c:when>
							<c:when test="${warn}">
								<div class="alert alert-warn">
									${message}
								</div>		
							</c:when>
							<c:when test="${msg}">
								<div class="alert alert-info">
									${message}
								</div>		
							</c:when>
						</c:choose>
					</div>
				</div>
			</c:if>
			
			<div class="row ">
				<div class="span12">
					<tiles:insertAttribute name="body" defaultValue="" />
				</div>
			</div>
		</div>
		
		<tiles:insertAttribute name="footer" defaultValue="" />

		<jsp:include page='_loadingDialog.jsp'></jsp:include>
		
		<jsp:include page='_editDataDialog.jsp'></jsp:include>
		
		<jsp:include page='_removeConfirmationDialog.jsp'></jsp:include>
		
		<script type="text/javascript">
			var App = function() {
				return {
					contextPath : '${pageContext.request.contextPath}',
					locale : '${sessionScope["org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE"]}',
					readOnly : ${readOnly != null ? readOnly : false }
				};
			}();
		</script>
		
		<!-- jQuery -->
		<script src='<c:url value="/resources/plugin/jquery/jquery-1.9.1.js" />'></script>
		
		<script src='<c:url value="/resources/plugin/jquery/ui/jquery-ui.js" />'></script>
		
		<script src='<c:url value="/resources/plugin/bootstrap/js/bootstrap.js" />'></script>
		
		<!-- select or dropdown enhancer -->
		<script src='<c:url value="/resources/plugin/jquery-chosen/chosen.jquery.js" />'></script>
		
		<!-- tag-it script -->
		<script src='<c:url value="/resources/plugin/tagit/js/tag-it.js" />'></script>
		
		<!-- cleditor script -->
		<script src='<c:url value="/resources/plugin/cleditor/jquery.cleditor.js" />'></script>

		<!-- ellipsis script -->
		<script src='<c:url value="/resources/plugin/jquery-ellipsis/jquery.ellipsis.js" />'></script>

		<!-- mediaelement script -->
		<script src='<c:url value="/resources/plugin/mediaelement/mediaelement-and-player.min.js" />'></script>
		
		<!-- application scripts -->
		<script type="text/javascript" src='<c:url value="/resources/app/js/app.js" />'></script>
	</body>
</html>
