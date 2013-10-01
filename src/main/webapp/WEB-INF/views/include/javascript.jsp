<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
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