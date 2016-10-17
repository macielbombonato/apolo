FROM jboss/wildfly:9.0.2.Final
MAINTAINER Maciel Escudero Bombonato <maciel.bombonato@gmail.com>

EXPOSE 8080

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone.xml", "-b", "0.0.0.0"]
ADD target/apolo-api-5.0.0.war /opt/jboss/wildfly/standalone/deployments/ROOT.war
