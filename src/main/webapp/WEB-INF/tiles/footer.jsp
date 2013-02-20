<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<div class="row-fluid">
	<div class="span12">
		<hr>
	</div>
</div>
<div class="row-fluid">
	<div class="span8">
		<p class="muted">
			<small>
				<s:message code="footer.message" htmlEscape="false"/>
			</small>
		</p>
	</div>
	
	<div class="span4">
		<!-- theme selector starts -->
		<div class="btn-group pull-right theme-container dropup" >
			<a class="btn dropdown-toggle-up" data-toggle="dropdown" href="#">
				<i class="icon-tint"></i><span class="hidden-phone"> Change Theme / Skin</span>
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu" id="themes">
				<li><a data-value="classic" href="#"><i class="icon-blank"></i> Classic</a></li>
				<li><a data-value="cerulean" href="#"><i class="icon-blank"></i> Cerulean</a></li>
				<li><a data-value="cyborg" href="#"><i class="icon-blank"></i> Cyborg</a></li>
				<li><a data-value="redy" href="#"><i class="icon-blank"></i> Redy</a></li>
				<li><a data-value="journal" href="#"><i class="icon-blank"></i> Journal</a></li>
				<li><a data-value="simplex" href="#"><i class="icon-blank"></i> Simplex</a></li>
				<li><a data-value="slate" href="#"><i class="icon-blank"></i> Slate</a></li>
				<li><a data-value="spacelab" href="#"><i class="icon-blank"></i> Spacelab</a></li>
				<li><a data-value="united" href="#"><i class="icon-blank"></i> United</a></li>
			</ul>
		</div>
		<!-- theme selector ends -->
	</div>
</div>