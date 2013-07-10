<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		
		<title>
			<s:message code="app.title" />
		</title>
		
		<link rel="icon" type="image/png" href='<c:url value="/resources/app/img/favicon.png" />'>
		
		<!-- The styles -->
		<style type="text/css">
			body {
				padding-bottom: 40px;
			}
			.sidebar-nav {
				padding: 9px 0;
			}
		</style>
		<link href='<c:url value="/resources/plugin/bootstrap/css/bootstrap.css" />' rel="stylesheet" />
		<link href='<c:url value="/resources/plugin/bootstrap/css/bootstrap-responsive.css" />' rel="stylesheet" />
		<link href='<c:url value="/resources/plugin/bootstrap/css/bootstrap-switch.css" />' rel="stylesheet" />
		
		<link href='<c:url value="/resources/plugin/jquery/themes/base/jquery-ui.css" />' rel="stylesheet" />
		
		<link href='<c:url value="/resources/plugin/charisma/css/fullcalendar.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/charisma/css/fullcalendar.print.css" />' rel='stylesheet'  media='print'>
		<link href='<c:url value="/resources/plugin/charisma/css/uniform.default.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/charisma/css/colorbox.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/charisma/css/jquery.noty.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/charisma/css/noty_theme_default.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/charisma/css/jquery.iphone.toggle.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/charisma/css/opa-icons.css" />' rel='stylesheet'>
		
		<link href='<c:url value="/resources/plugin/jquery-chosen/chosen.css" />' rel='stylesheet'>
		
		<link href='<c:url value="/resources/plugin/tagit/css/jquery.tagit.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/tagit/css/tagit.ui-zendesk.css" />' rel='stylesheet'>
		
		<link href='<c:url value="/resources/plugin/cleditor/jquery.cleditor.css" />' rel='stylesheet'>
		
		<link href='<c:url value="/resources/app/css/app.css" />' rel='stylesheet'>
		
	</head>
	
	<body>
		<tiles:insertAttribute name="header" defaultValue="" />
		
		<div class="container-fluid">
			<c:if test="${error || warn || msg}">
				<div class="row-fluid">
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
			
			<div class="row-fluid">
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
		<script src='<c:url value="/resources/plugin/jquery/jquery-1.7.2.js" />'></script>
		
		<script src='<c:url value="/resources/plugin/jquery/ui/jquery-ui.js" />'></script>
		
		<script src='<c:url value="/resources/plugin/bootstrap/js/bootstrap.js" />'></script>
		
		<script src='<c:url value="/resources/plugin/bootstrap/js/bootstrap-switch.js" />'></script>
		
		<!-- select or dropdown enhancer -->
		<script src='<c:url value="/resources/plugin/jquery-chosen/chosen.jquery.js" />'></script>
		
		<!-- tag-it script -->
		<script src='<c:url value="/resources/plugin/tagit/js/tag-it.js" />'></script>
		
		<!-- cleditor script -->
		<script src='<c:url value="/resources/plugin/cleditor/jquery.cleditor.js" />'></script>

		<!-- ellipsis script -->
		<script src='<c:url value="/resources/plugin/jquery-ellipsis/jquery.ellipsis.js" />'></script>

		<!-- transition / effect library -->
		<script src='<c:url value="/resources/plugin/charisma/js/bootstrap-transition.js" />'></script>
		<!-- alert enhancer library -->
		<script src='<c:url value="/resources/plugin/charisma/js/bootstrap-alert.js" />'></script>
		<!-- modal / dialog library -->
		<script src='<c:url value="/resources/plugin/charisma/js/bootstrap-modal.js" />'></script>
		<!-- custom dropdown library -->
		<script src='<c:url value="/resources/plugin/charisma/js/bootstrap-dropdown.js" />'></script>
		<!-- scrolspy library -->
		<script src='<c:url value="/resources/plugin/charisma/js/bootstrap-scrollspy.js" />'></script>
		<!-- library for creating tabs -->
		<script src='<c:url value="/resources/plugin/charisma/js/bootstrap-tab.js" />'></script>
		<!-- library for advanced tooltip -->
		<script src='<c:url value="/resources/plugin/charisma/js/bootstrap-tooltip.js" />'></script>
		<!-- popover effect library -->
		<script src='<c:url value="/resources/plugin/charisma/js/bootstrap-popover.js" />'></script>
		<!-- button enhancer library -->
		<script src='<c:url value="/resources/plugin/charisma/js/bootstrap-button.js" />'></script>
		<!-- accordion library (optional, not used in demo) -->
		<script src='<c:url value="/resources/plugin/charisma/js/bootstrap-collapse.js" />'></script>
		<!-- carousel slideshow library (optional, not used in demo) -->
		<script src='<c:url value="/resources/plugin/charisma/js/bootstrap-carousel.js" />'></script>
		<!-- autocomplete library -->
		<script src='<c:url value="/resources/plugin/charisma/js/bootstrap-typeahead.js" />'></script>
		<!-- tour library -->
		<script src='<c:url value="/resources/plugin/charisma/js/bootstrap-tour.js" />'></script>
		<!-- library for cookie management -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.cookie.js" />'></script>
		<!-- calander plugin -->
		<script src='<c:url value="/resources/plugin/charisma/js/fullcalendar.min.js" />'></script>
		<!-- data table plugin -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.dataTables.min.js" />'></script>
	
		<!-- chart libraries start -->
		<script src='<c:url value="/resources/plugin/charisma/js/excanvas.js" />'></script>
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.flot.min.js" />'></script>
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.flot.pie.min.js" />'></script>
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.flot.stack.js" />'></script>
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.flot.resize.min.js" />'></script>
		<!-- chart libraries end -->
	

		<!-- checkbox, radio, and file input styler -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.uniform.min.js" />'></script>
		<!-- plugin for gallery image view -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.colorbox.min.js" />'></script>
		<!-- notification plugin -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.noty.js" />'></script>
		<!-- star rating plugin -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.raty.min.js" />'></script>
		<!-- for iOS style toggle switch -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.iphone.toggle.js" />'></script>
		<!-- autogrowing textarea plugin -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.autogrow-textarea.js" />'></script>
		
		<!-- application scripts -->
		<script type="text/javascript" src='<c:url value="/resources/app/js/app.js" />'></script>
		
		<!-- Scripts to load only if the user are in the form screen -->
		<security:authorize access="!isAuthenticated()">
			<script type="text/javascript" src='<c:url value="/resources/app/js/login.js" />'></script>
		</security:authorize>
	</body>
</html>
