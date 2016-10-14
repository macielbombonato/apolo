FROM  tomcat
MAINTAINER Maciel Escudero Bombonato <maciel.bombonato@gmail.com>
RUN rm -fr /usr/local/tomcat/webapps/ROOT
ADD apolo-web/target/apolo-web-5.0.0.war /usr/local/tomcat/webapps/ROOT.war
