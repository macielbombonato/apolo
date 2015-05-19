# Apolo
---------
## O Projeto

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

	export APOLO_UPLOADED_FILES="/your/file/path/"
	export APOLO_VIDEO_CONVERTER_EXECUTABLE_PATH=""
	export APOLO_PDF_IMAGE_EXTRACTOR_EXECUTABLE_PATH=""
	export APOLO_DEFAULT_TENANT="apolo"

	export APOLO_DEFAULT_emailFrom="email@from"
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


## Licença

Apolo é um software de código aberto, você pode redistribuí-lo e/ou modificá-lo conforme a licença Apache versão 2.0. Veja o arquivo LICENSE-Apache.txt
