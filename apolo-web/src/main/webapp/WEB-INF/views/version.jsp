<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html lang="en">
	<jsp:include page='/WEB-INF/views/fragment/_pageheader.jsp'></jsp:include>
	<body class="overflow-hidden">
		<div id="overlay" class="transparent"></div>
	
		<div id="wrapper" class="preload">
			<jsp:include page='/WEB-INF/views/fragment/_contentheader.jsp'></jsp:include>
				
			<div id="main-container">
				
				<jsp:include page='/WEB-INF/views/fragment/_breadcrumb.jsp'></jsp:include>
					
				<jsp:include page='/WEB-INF/views/fragment/_message.jsp'></jsp:include>
				
				<div class="container ">
					<div class="row ">
						<div class="col-md-12">
							<h3 style="text-align: center;">
								<s:message code="view.build.package" />
							</h3>
						</div>
					</div>
					<div class="row ">
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.version" />
								</div>
								<div class="panel-body">
									<p><s:message code="version" /></p>								
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.generated.war" />
								</div>
								<div class="panel-body">
									<p><s:message code="generated.war" /></p>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.java.version" />
								</div>
								<div class="panel-body">
									<p><s:message code="java.version" /></p>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.java.vendor" />
								</div>
								<div class="panel-body">
									<p><s:message code="java.vendor" /></p>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.java.vendor.url" />
								</div>
								<div class="panel-body">
									<p><s:message code="java.vendor.url" /></p>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.java.home" />
								</div>
								<div class="panel-body">
									<p><s:message code="java.home" /></p>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.java.vm.specification.version" />
								</div>
								<div class="panel-body">
									<p><s:message code="java.vm.specification.version" /></p>
								</div>
							</div>
						</div>
					
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.java.vm.specification.vendor" />
								</div>
								<div class="panel-body">
									<p><s:message code="java.vm.specification.vendor" /></p>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.java.vm.specification.name" />
								</div>
								<div class="panel-body">
									<p><s:message code="java.vm.specification.name" /></p>
								</div>
							</div>
						</div>
										
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.java.vm.version" />
								</div>
								<div class="panel-body">
									<p><s:message code="java.vm.version" /></p>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.java.vm.vendor" />
								</div>
								<div class="panel-body">
									<p><s:message code="java.vm.vendor" /></p>
								</div>
							</div>
						</div>

						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.java.vm.name" />
								</div>
								<div class="panel-body">
									<p><s:message code="java.vm.name" /></p>
								</div>
							</div>
						</div>
										
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.java.specification.version" />
								</div>
								<div class="panel-body">
									<p><s:message code="java.specification.version" /></p>
								</div>
							</div>
						</div>
								
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.java.specification.vendor" />
								</div>
								<div class="panel-body">
									<p><s:message code="java.specification.vendor" /></p>
								</div>
							</div>
						</div>
										
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.java.specification.name" />
								</div>
								<div class="panel-body">
									<p><s:message code="java.specification.name" /></p>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.java.class.version" />
								</div>
								<div class="panel-body">
									<p><s:message code="java.class.version" /></p>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.java.class.path" />
								</div>
								<div class="panel-body">
									<p><s:message code="java.class.path" /></p>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.java.ext.dirs" />
								</div>
								<div class="panel-body">
									<p><s:message code="java.ext.dirs" /></p>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.os.name" />
								</div>
								<div class="panel-body">
									<p><s:message code="os.name" /></p>
								</div>
							</div>
						</div>
										
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.os.arch" />
								</div>
								<div class="panel-body">
									<p><s:message code="os.arch" /></p>
								</div>
							</div>
						</div>
										
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.os.version" />
								</div>
								<div class="panel-body">
									<p><s:message code="os.version" /></p>
								</div>
							</div>
						</div>

						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.file.separator" />
								</div>
								<div class="panel-body">
									<p><s:message code="file.separator" /></p>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.path.separator" />
								</div>
								<div class="panel-body">
									<p><s:message code="path.separator" /></p>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.line.separator" />
								</div>
								<div class="panel-body">
									<p><s:message code="line.separator" /></p>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.build.user.name" />
								</div>
								<div class="panel-body">
									<p><s:message code="build.user.name" /></p>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.build.user.home" />
								</div>
								<div class="panel-body">
									<p><s:message code="build.user.home" /></p>
								</div>
							</div>
						</div>
										
						<div class="col-md-6">
							<div class="panel panel-default">
								<div class="panel-heading">
									<s:message code="view.build.user.dir" />
								</div>
								<div class="panel-body">
									<p><s:message code="build.user.dir" /></p>
								</div>
							</div>
						</div>
										
					</div>
				</div>
			</div>
			
			<jsp:include page='/WEB-INF/views/fragment/_contentfooter.jsp'></jsp:include>
			<jsp:include page='/WEB-INF/views/fragment/_pagefooter.jsp'></jsp:include>			
		</div>
	</body>
</html>