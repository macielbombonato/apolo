FROM  tomcat
MAINTAINER Maciel Escudero Bombonato <maciel.bombonato@gmail.com>
ADD apolo-web/target/apolo-web-5.0.0.war /usr/local/tomcat/webapps/ROOT.war
