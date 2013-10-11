<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<jsp:include page='/WEB-INF/views/menu/menu.jsp'></jsp:include>

<!-- Subhead ================================================== -->
<header id="header">
  <div class="container">
    <p class="lead">&nbsp;</p>
  </div>
</header>

<jsp:include page='/WEB-INF/views/fragment/_breadcrumb.jsp'></jsp:include>

<div class="container ">
	<jsp:include page='/WEB-INF/views/fragment/_message.jsp'></jsp:include>
</div>
		
