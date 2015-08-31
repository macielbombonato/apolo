# HERMES
---------
## O Projeto Apolo

Este projeto é uma iniciativa open source para que os desenvolvedores ganhem tempo no startup de seus sistemas, tendo uma base consistente e de fácil expansão.

Basicamente, imagene que em todo projeto que iniciamos, temos que definir a arquitetura, que componentes iremos utilizar, e contruir um utilitário ou outro para isso ou aquilo.

O Apolo tem alguns recursos desses já construidos, por exemplo:

- Sistema de autenticação (CRUD de usuários);
- Sistema de segurança (grupos de permissões de acesso);
- Startup que utiliza variáveis de ambiente;
- Sistema de propagandas google adsense;
- Sistema de controle de acessos com google analytics;
- Controle de upload de arquivos;
- Interface administrativa responsiva;
- Sistema de auditoria de dados (trilha de auditoria);
- Dentre outros que já existem e virão em breve;

Basicamente, a ideia é ajudar e agilizar a vida dos programadores que utilizarem este projeto, onde, fazendo um fork deste, já é possível iniciar o seu projeto.


## Faça sua cópia

O Apolo é versionado com git, portanto, para fazer sua cópia do código e controlá-la, será necessário ter o git em sua
máquina.

Há algum tempo eu fiz um "tutorialzinho" de alguns comandos git e para que eles servem:
[http://macielbombonato.blogspot.com.br/2012/02/utilizando-git-versionador-local.html](http://macielbombonato.blogspot.com.br/2012/02/utilizando-git-versionador-local.html),
Este tutorial deve ajudar um pouco para realizar operações via linha de comando.

## Crie seu Fork deste projeto

Caso queira montar um projeto a partir deste e queira deixá-lo versionado aqui no bitbucket, use a opção "Fork" que encontra-se no topo da tela. O processo é rápido e o passo a passo é bem simples.

Espero que este projeto lhe seja útil e tendo sugestões ou encontrando problemas, por favor, entre em contato ou abra uma issue aqui no bitbucket.

## Configurações de ambiente

A partir da versão 3.1.0 o Apolo passou a utilizar variáveis de ambiente para configurações, assim, fica mais fácil de configurar o servidor de aplicação e de colocar a aplicação no ar.

Abaixo estão as variáveis de ambiente que devem ser configuradas em seu servidor.

	export HERMES_PRODUCTION_DEPLOY_URL="http://endereco_do_seu_servidor:8080/manager/text"

	export HERMES_SECRET_KEY="1234567890ABCDEF" #max 16 chars
	export HERMES_IV_KEY="1234567890ABCDEF" #max 16 chars

	export HERMES_UPLOADED_FILES="/your/file/path/"
	export HERMES_VIDEO_CONVERTER_EXECUTABLE_PATH=""
	export HERMES_PDF_IMAGE_EXTRACTOR_EXECUTABLE_PATH=""
	export HERMES_DEFAULT_TENANT="apolo"

	export HERMES_DEFAULT_emailFrom="email@from"
	export HERMES_DEFAULT_emailUsername="email@username"
	export HERMES_DEFAULT_emailPassword="pass"
	export HERMES_DEFAULT_smtpHost="smtp"
	export HERMES_DEFAULT_smtpPort="123"
	export HERMES_DEFAULT_useTLS="true"
	export HERMES_SEND_AUTH_EMAIL="true"

	export HERMES_DEFAULT_GOOGLE_ADCLIENT="ca-pub-XXXXXXXXXXX"
	export HERMES_DEFAULT_GOOGLE_ADSLOT_ONE="XXXXXXXXXX"
	export HERMES_DEFAULT_GOOGLE_ADSLOT_TWO="XXXXXXXXXX"
	export HERMES_DEFAULT_GOOGLE_ADSLOT_THREE="XXXXXXXXXX"
	export HERMES_DEFAULT_GOOGLE_ANALYTICS_USER_ACCOUNT="UA-XXXXXXXX-X"

	export HERMES_DATASOURCE_DRIVER_CLASS="com.mysql.jdbc.Driver"
	export HERMES_HIBERNATE_DIALECT="org.hibernate.dialect.MySQL5InnoDBDialect"
	export HERMES_HIBERNATE_HBM2DDL="update"
	export HERMES_DATASOURCE_URL="jdbc:mysql://localhost:3306/hermes?createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=utf-8"
	export HERMES_DATASOURCE_USERNAME="your_user"
	export HERMES_DATASOURCE_PASSWORD="pass"
	export HERMES_HIBERNATE_SHOW_AND_FORMAT_SQL="true"

	export HERMES_FACEBOOK_APP_ID="YORAPPID"
	export HERMES_FACEBOOK_APP_SECRET="YOURAPPSECRET"

## Primeiros passos

Após a configuração das variáveis de ambiente vá até a pasta principal do projeto e execute o comando:

	$ mvn clean install

Isso irá compilar e gerar um primeiro pacote do sistema.

Após configurar o projeto em sua IDE preferência, por exemplo, Eclipse ou IntelliJIDEA e configurar o application server, por exemplo o Tomcat, é hora de executar a aplicação pela primeira vez.

O problema aqui é que o banco está vazio, mas, o sistema oferece uma página de instalação que só pode ser acessada uma vez, para isso, acesse em seu servidor:

	http://localhost:8080/install

Preencha o formulário para criar o usuário administrador geral do sistema. Após isso, é só customizar o sistema para suas necessidades.


## Deploy em PRODUÇÃO

Bom, quando fechar um release e quiser fazer o deploy em produção, é possível fazer isso diretamente de sua máquina, mas para isso, algumas coisas precisam ser feitas no servidor e na sua máquina.

Primeiro você precisa abrir o arquivo settings.xml do maven que fica no seu repositório local de usuário (ambiente UNIX).

	$ cd ~/.m2
	$ vi settings.xml

Agora é necessário incluir as seguintes linhas no arquivo e dentro da estrutura servers:

    <server>
      <id>TomcatServer</id>
      <username>admin</username>
      <password>SENHA-CONFIGURADA-NO-ARQUIVO-USERS-DO-TOMCAT-NO-SERVER</password>
    </server>

Agora é necessário conferir se a estrutura no servidor está correta, caso não, devemos acertá-la.

Vá até a instalação do tomcat e na pasta de configuração dele abra o arquivo tomcat-users.xml. Verificar se os usuários e roles estão corretamente configurados, conforme exemplo:

	<role rolename="manager-gui"/>
	<role rolename="manager-script"/>
	<user username="admin" password="SENHA-CONFIGURADA-NO-ARQUIVO-USERS-DO-TOMCAT-NO-SERVER" roles="manager-gui,manager-script"/>

Estando tudo certo neste arquivo, verifique se na pasta webapps vc possui o projeto manager, caso sim, a estrutura está OK e pronta para o deploy remoto.

Voltando a máquina local, é necessário conferir se a variável de ambiente HERMES_PRODUCTION_DEPLOY_URL está apontando para o servidor corretamente, abaixo segue um exemplo:

	http://endereco_do_seu_servidor:8080/manager/text

Tudo certinho? execute o seguinte comando no diretório da aplicação:

	$ mvn clean install tomcat7:redeploy

No final do processo, o maven irá fazer o deploy automaticamente.

## Licença

Apolo é um software de código aberto, você pode redistribuí-lo e/ou modificá-lo conforme a licença Apache versão 2.0. Veja o arquivo LICENSE-Apache.txt
