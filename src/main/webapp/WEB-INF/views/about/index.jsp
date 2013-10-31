<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html lang="en">
	<jsp:include page='/WEB-INF/views/fragment/_pageheader.jsp'></jsp:include>
	<body>
		<jsp:include page='/WEB-INF/views/fragment/_contentheader.jsp'></jsp:include>
		
		<div class="container ">
			<div class="row ">
				<ul class="list-group" style="text-align: justify">
					<li class="list-group-item">
					    <h4 class="list-group-item-heading">
					    	Portal
					    </h4>
					    <p class="list-group-item-text">
					    	O Apolo é uma proposta de arquitetura para o desenvolvimento de sistemas web de forma que o startup de desenvolvimento de uma aplicação seja considerávelmente reduzido.<br/>
					    	A ideia foi desenvolver algo que é necessário no inicio da construção de uma aplicação (estrutura de autenticação e controle de permissões de acesso) e tentar incluir
					    	nesta estrutura o maior número possível de exemplos úteis (diminuir ao máximo o número de que devem ser excluídas ou alteradas pelo desenvolvedor que utilizar o Apolo
					    	como alicerce para seu sistema). Com isso, o sistema oferece controle de usuários, controle de campos customizados para usuários, controle de grupos de permissão de acesso, trilha de auditoria 
					    	(apenas administradores utilizam), caminho de pão (breadcrumb), paginação de tabelas e suporte a internacionalização.<br />
					    	O desenvolvedor encontrará componentes de apoio para o desenvolvimento de telas, upload de arquivos, dentre outras funcionalidades.<br/>
					    	Como a ideia é facilitar e agilizar, procuramos utilizar apenas componentes e conceitos com uso bem difundido no mercado, como por exemplo, Spring (MVC e Secutiry), JPA (com hibernate), 
					    	camada visual contruida com twitter bootstrap e suporte aos bancos de dados MySQL e PostgreeSQL.<br/>
					    </p>
					</li>
					<li class="list-group-item">
					    <h4 class="list-group-item-heading">
					    	Como utilizar
					    </h4>
					    <p class="list-group-item-text">
					    	...
					    </p>
					</li>
					<li class="list-group-item">
					    <h4 class="list-group-item-heading">
					    	Como extender e desenvolver seu sistema
					    </h4>
					    <p class="list-group-item-text">
					    	...
					    </p>
					</li>
					<li class="list-group-item">
					    <h4 class="list-group-item-heading">
					    	Localização do código fonte e licença de uso
					    </h4>
					    <p class="list-group-item-text">
					    	...
					    </p>
					</li>
					<li class="list-group-item">
					    <h4 class="list-group-item-heading">
					    	Idealizador do projeto
					    </h4>
					    <p class="list-group-item-text">
					    	...
					    </p>
					</li>
				</ul>
				INTERNACIONALIZAR
			</div>
		</div>
		
		<jsp:include page='/WEB-INF/views/fragment/_contentfooter.jsp'></jsp:include>
		<jsp:include page='/WEB-INF/views/fragment/_pagefooter.jsp'></jsp:include>
	</body>
</html>