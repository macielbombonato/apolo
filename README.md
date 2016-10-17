# APOLO
---------
## O Projeto Apolo

Este projeto é uma iniciativa open source para que os desenvolvedores ganhem tempo no startup de seus sistemas, tendo uma base consistente e de fácil expansão.

Basicamente, imagine que em todo projeto que iniciamos, temos que definir a arquitetura, que componentes iremos utilizar, e contruir um utilitário ou outro para isso ou aquilo.

O Apolo tem alguns recursos desses já construidos, por exemplo:

- Sistema de autenticação (CRUD de usuários);
- Sistema de segurança (grupos de permissões de acesso);
- Startup que utiliza variáveis de ambiente;
- Sistema de propagandas google adsense;
- Sistema de controle de acessos com google analytics;
- Controle de upload de arquivos;
- Interface administrativa responsiva;
- Sistema de auditoria de dados (trilha de auditoria);
- e assim por diante;

A ideia é ajudar e agilizar a vida dos programadores que utilizarem este projeto, onde, fazendo um fork deste, já é possível iniciar o seu projeto focando apenas nas regras de negócio específicas do sistema.


## Versões do sistema

Normalmente tento deixar a última versão do Apolo [neste endereço](http://dimeric.com.br).

Como se trata de um servidor meu para testes de aplicativos que estou atuando em regime de free lancer ou algo parecido, é bem possível de vez em quando encontrar outro sistema nesse endereço e não o Apolo.

Na versão 5.0 aconteceu uma grande mudança arquitetural no sistema onde a aplicação de servidor é inteiramente uma API e a aplicação de cliente é AngularJS.

Irei documentar todas essas alterações assim que deixar ele mais redondo e com poucas coisas a fazer.

Caso você que está lendo, ache a ideia do Apolo interessante mas encontre problemas de código ou até mesmo conceituais, me ajude a deixar este sistema que serve (a principio) apenas para estudo, uma plataforma mais forte e coesa, envie sugestões para meu [e-mail](mailto:maciel.bombonato@gmail.com).

OBS.: Como se trata de um projeto pessoal, os pontos que preciso desenvolver dele são basicamente ideias que tendo registrar sempre que possível na lista de incidentes no [Bitbucket](https://bitbucket.org/macielbombonato/apolo/issues), mas muita coisa que é feita não está lá e isso pode ser visto nos comentários de commit que quando são feitos por conta de uma issue, possuem referência a mesma.

## Configurações de ambiente

### Necessário instalar em sua máquina

	- docker
	- java ([Clique Aqui](https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-get-on-ubuntu-16-04));
	- maven (apt-get install maven);
	- ruby (apt-get install ruby);
	- ruby-compass (apt-get install ruby-compass);
	- node (apt-get install nodejs nodejs-legacy);
	- npm (apt-get install npm);
	- bower (npm install --save bower -g);
	- gulp (npm install --save gulp -g)

### Documentação antiga

A partir da versão 3.1.0 o Apolo passou a utilizar variáveis de ambiente para configurações, assim, fica mais fácil de configurar o servidor de aplicação e de colocar a aplicação no ar.

Abaixo estão as variáveis de ambiente que devem ser configuradas em seu servidor.

	export APOLO_PRODUCTION_DEPLOY_URL="http://endereco_do_seu_servidor:8080/manager/text"

	export APOLO_PRODUCTION_DEPLOY_USERNAME="USERNAME-CONFIGURADO-NO-TOMCAT"
	export APOLO_PRODUCTION_DEPLOY_PASSWORD="SENHA-CONFIGURADA-NO-ARQUIVO-USERS-DO-TOMCAT-NO-SERVER"

	export APOLO_SECRET_KEY="1234567890ABCDEF" #max 16 chars
	export APOLO_IV_KEY="1234567890ABCDEF" #max 16 chars

	export APOLO_UPLOADED_FILES="/your/file/path/"
	export APOLO_VIDEO_CONVERTER_EXECUTABLE_PATH=""
	export APOLO_PDF_IMAGE_EXTRACTOR_EXECUTABLE_PATH=""
	export APOLO_DEFAULT_TENANT="apolo"

	export APOLO_DEFAULT_emailFrom="email@from"
	export APOLO_DEFAULT_emailUsername="email@username"
	export APOLO_DEFAULT_emailPassword="pass"
	export APOLO_DEFAULT_smtpHost="smtp"
	export APOLO_DEFAULT_smtpPort="123"
	export APOLO_DEFAULT_useTLS="true"
	export APOLO_SEND_AUTH_EMAIL="true"

	export APOLO_DEFAULT_GOOGLE_ADCLIENT="ca-pub-XXXXXXXXXXX"
	export APOLO_DEFAULT_GOOGLE_ADSLOT_ONE="XXXXXXXXXX"
	export APOLO_DEFAULT_GOOGLE_ADSLOT_TWO="XXXXXXXXXX"
	export APOLO_DEFAULT_GOOGLE_ADSLOT_THREE="XXXXXXXXXX"
	export APOLO_DEFAULT_GOOGLE_ANALYTICS_USER_ACCOUNT="UA-XXXXXXXX-X"

	export APOLO_DATASOURCE_DRIVER_CLASS="com.mysql.jdbc.Driver"
	export APOLO_HIBERNATE_DIALECT="org.hibernate.dialect.MySQL5InnoDBDialect"
	export APOLO_HIBERNATE_HBM2DDL="update"
	export APOLO_DATASOURCE_URL="jdbc:mysql://localhost:3306/apolo?createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=utf-8"
	export APOLO_DATASOURCE_USERNAME="your_user"
	export APOLO_DATASOURCE_PASSWORD="pass"
	export APOLO_HIBERNATE_SHOW_AND_FORMAT_SQL="true"


## Primeiros passos

Após a configuração das variáveis de ambiente vá até a pasta principal do projeto e execute o comando:

	$ mvn clean install

Isso irá compilar e gerar um primeiro pacote do sistema.

Após configurar o projeto em sua IDE preferência, por exemplo, Eclipse ou IntelliJIDEA e configurar o application server, por exemplo o Tomcat, é hora de executar a aplicação pela primeira vez.

O problema aqui é que o banco está vazio, mas, o sistema oferece uma página de instalação que só pode ser acessada uma vez, para isso, acesse em seu servidor:

	http://localhost:8080/install

Preencha o formulário para criar o usuário administrador geral do sistema. Após isso, é só customizar o sistema para suas necessidades.


## Deploy em PRODUÇÃO

Bom, quando fechar um release e quiser fazer o deploy em produção, é possível fazer isso diretamente de sua máquina, mas para isso, algumas coisas precisam ser feitas no servidor.

Vá até a instalação do tomcat e na pasta de configuração dele abra o arquivo tomcat-users.xml. Verificar se os usuários e roles estão corretamente configurados, conforme exemplo:

	<role rolename="manager-gui"/>
	<role rolename="manager-script"/>
	<user username="admin" password="SENHA-CONFIGURADA-NO-ARQUIVO-USERS-DO-TOMCAT-NO-SERVER" roles="manager-gui,manager-script"/>

Estando tudo certo neste arquivo, verifique se na pasta webapps vc possui o projeto manager, caso sim, a estrutura está OK e pronta para o deploy remoto.

Voltando a máquina local, é necessário conferir se a variável de ambiente **APOLO_PRODUCTION_DEPLOY_URL** está apontando para o servidor corretamente e se as variáveis **APOLO_PRODUCTION_DEPLOY_USERNAME** e **APOLO_PRODUCTION_DEPLOY_PASSWORD** estão com os valores corretos no arquivo de configuração do Tomcat de produção.

Abaixo segue um exemplo de URL para deploy remoto:

	http://endereco_do_seu_servidor:8080/manager/text

Tudo certinho? execute o seguinte comando no diretório da aplicação api ou web:

	$ mvn clean install tomcat7:redeploy

No final do processo, o maven irá fazer o deploy automaticamente.

Quando o deploy for feito na API a URL de acesso no servidor terminará com /api e a camada web é instalada no root do servidor.

## Licença

Apolo API é um software de código aberto, você pode redistribuí-lo e/ou modificá-lo conforme a licença Apache versão 2.0. Veja o arquivo LICENSE-Apache.txt.

**A versão WEB** utiliza o template **[Angle](https://wrapbootstrap.com/theme/angle-bootstrap-admin-template-WB04HF123)**, então, antes de utilizá-la, você precisa comprar sua cópia através do link informado anteriormente.

## API

A partir da versão 4.0 o Apolo passou a ter uma API básica de suas funcionalidades e com isso, oferece uma estrutura para que aplicações baseadas nele possam implementar APIs facilmente.

O primeiro passo é cadastrar sua aplicação dentro do Apolo no menu Aplicativos e isso irá gerar uma chave, esta deve ser enviada no "header" de todas as chamadas conforme o exemplo abaixo:

	key: f4a8c9c3fc9019b4d063f0cca8864e33

Abaixo as chamadas que o Apolo possui:

### Login

	POST /api/{tenant-url}/user/login
	Content-Type: application/json

**Exemplo de json para postar**

	{
		"username":"Nome do Usuário",
		"password":"senha do usuário"
	}

Este serviço irá retornar o registro do usuário autenticado. É necessário recuperar o token deste retorno, ele será utilizado em todas as outras chamadas preenchendo o campo "key" do cabeçalho.

### Listar usuários

	GET /api/{tenant-url}/user/list
	GET /api/{tenant-url}/user/list/{pageNumber}

**Somente para administradores do sistema**

	GET /api/{tenant-url}/user/list-all
	GET /api/{tenant-url}/user/list-all/{pageNumber}

### Buscar usuário

	GET /api/{tenant-url}/user/{id}

Caso a chave de acesso da API tenha permissão de administrador do sistema, o termo (tenant-url) não será considerado na busca, ou seja, o usuário será procurado apenas pelo ID, caso a chave de API não possua essa permissão, serão considerados ID e tenant URL para a busca.

### Criar usuário

	POST /api/{tenant-url}/user
	Content-Type: application/json

**Exemplo de json para postar**

	{
		"name":"Nome do Usuário",
		"email":"email@usuario.com",
		"groupIds":[1, 2] --> grupos de permissão de acesso
	}

### Editar usuário

	PUT /api/{tenant-url}/user
	Content-Type: application/json

**Exemplo de json para postar**

	{
		"id":10,
		"name":"Nome do Usuário",
		"email":"email@usuario.com",
		"groupIds":[1, 2] --> grupos de permissão de acesso
	}

Somente os campos indicados no json acima poderão ser editados, os demais campos do usuário não podem ser alterados pela API.
Este recurso é também a única maneira de alterar o Tenant de um usuário, portanto, atente-se bem ao tenant que está sendo indicado na URL, caso tenha permissão para isso, este será o novo tenant do usuário informado.

### Excluir usuário

	DELETE /api/{tenant-url}/user/{id}
