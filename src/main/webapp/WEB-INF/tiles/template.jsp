<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

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
		<link href='<c:url value="/resources/plugin/charisma/css/jquery-ui-1.8.21.custom.css" />' rel="stylesheet">
		<link href='<c:url value="/resources/plugin/charisma/css/fullcalendar.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/charisma/css/fullcalendar.print.css" />' rel='stylesheet'  media='print'>
		<link href='<c:url value="/resources/plugin/charisma/css/chosen.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/charisma/css/uniform.default.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/charisma/css/colorbox.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/charisma/css/jquery.cleditor.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/charisma/css/jquery.noty.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/charisma/css/noty_theme_default.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/charisma/css/elfinder.min.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/charisma/css/elfinder.theme.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/charisma/css/jquery.iphone.toggle.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/charisma/css/opa-icons.css" />' rel='stylesheet'>
		<link href='<c:url value="/resources/plugin/charisma/css/uploadify.css" />' rel='stylesheet'>
		
		<link rel="stylesheet" href='<c:url value="/resources/app/css/app.css" />' />
		
	</head>
	
	<body>
		<tiles:insertAttribute name="header" defaultValue="" />
		
		<div class="container-fluid">
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
			
			<div class="row-fluid">
				<div class="span12">
					<tiles:insertAttribute name="body" defaultValue="" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<tiles:insertAttribute name="footer" defaultValue="" />
				</div>
			</div>
		</div>

		<div class="container-fluid">
			<div class="row-fluid">
				<div id="loadingDialog" class="modal hide fade span1" tabindex="-1" data-role="dialog" style="text-align: center;">
					<img src='<c:url value="/resources/app/img/ajax-loader.gif" />' class="img-circle">
				</div>
			</div>
		</div>
		
		<script type="text/javascript">
			var App = function() {
				return {
					contextPath : '${pageContext.request.contextPath}',
					locale : '${sessionScope["org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE"]}'
				};
			}();
		</script>
		
		<!-- jQuery -->
		<script src="http://code.jquery.com/jquery-latest.js"></script>
		<script src='<c:url value="/resources/plugin/charisma/js/jquery-1.7.2.min.js" />'></script>
		<!-- jQuery UI -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery-ui-1.8.21.custom.min.js" />'></script>
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
	
		<!-- select or dropdown enhancer -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.chosen.min.js" />'></script>
		<!-- checkbox, radio, and file input styler -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.uniform.min.js" />'></script>
		<!-- plugin for gallery image view -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.colorbox.min.js" />'></script>
		<!-- rich text editor library -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.cleditor.min.js" />'></script>
		<!-- notification plugin -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.noty.js" />'></script>
		<!-- file manager library -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.elfinder.min.js" />'></script>
		<!-- star rating plugin -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.raty.min.js" />'></script>
		<!-- for iOS style toggle switch -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.iphone.toggle.js" />'></script>
		<!-- autogrowing textarea plugin -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.autogrow-textarea.js" />'></script>
		<!-- multiple file upload plugin -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.uploadify-3.1.min.js" />'></script>
		<!-- history.js for cross-browser state change on ajax -->
		<script src='<c:url value="/resources/plugin/charisma/js/jquery.history.js" />'></script>
		<!-- application script for Charisma demo -->
		<script src='<c:url value="/resources/plugin/charisma/js/charisma.js" />'></script>
		
		<script type="text/javascript" src='<c:url value="/resources/app/js/app.js" />'></script>
		<script type="text/javascript" src='<c:url value="/resources/app/js/item-list-util.js" />'></script>
	</body>
</html>
