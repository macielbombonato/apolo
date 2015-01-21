<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
	var App = function() {
		return {
			contextPath : '${pageContext.request.contextPath}',
			locale : '${sessionScope["org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE"]}',
			readOnly : ${readOnly != null ? readOnly : false }
		};
	}();
</script>

<script src='<c:url value="/resources/plugin/endless/js/jquery-1.10.2.min.js" />'></script>
<script src='<c:url value="/resources/plugin/endless/bootstrap/js/bootstrap.min.js" />'></script>
<script src='<c:url value="/resources/plugin/endless/js/chosen.jquery.min.js" />'></script>
<script src='<c:url value="/resources/plugin/endless/js/jquery.maskedinput.min.js" />'></script>
<script src='<c:url value="/resources/plugin/endless/js/bootstrap-datepicker.min.js" />'></script>
<script src='<c:url value="/resources/plugin/endless/js/bootstrap-timepicker.min.js" />'></script>
<script src='<c:url value="/resources/plugin/endless/js/bootstrap-slider.min.js" />'></script>
<script src='<c:url value="/resources/plugin/endless/js/jquery.tagsinput.min.js" />'></script>
<script src='<c:url value="/resources/plugin/endless/js/wysihtml5-0.3.0.min.js" />'></script>
<script src='<c:url value="/resources/plugin/endless/js/uncompressed/holder.js" />'></script>
<script src='<c:url value="/resources/plugin/endless/js/uncompressed/bootstrap-wysihtml5.js" />'></script>
<script src='<c:url value="/resources/plugin/endless/js/dropzone.min.js" />'></script>
<script src='<c:url value="/resources/plugin/endless/js/modernizr.min.js" />'></script>
<script src='<c:url value="/resources/plugin/endless/js/pace.min.js" />'></script>
<script src='<c:url value="/resources/plugin/endless/js/jquery.popupoverlay.min.js" />'></script>
<script src='<c:url value="/resources/plugin/endless/js/jquery.slimscroll.min.js" />'></script>
<script src='<c:url value="/resources/plugin/endless/js/jquery.cookie.min.js" />'></script>
<script src='<c:url value="/resources/plugin/endless/js/endless/endless_form.js" />'></script>
<script src='<c:url value="/resources/plugin/endless/js/endless/endless.js" />'></script>

<!-- mediaelement script -->
<script src='<c:url value="/resources/plugin/mediaelement/mediaelement-and-player.min.js" />'></script>

<!-- application scripts -->
<script type="text/javascript" src='<c:url value="/resources/app/js/app.js" />'></script>