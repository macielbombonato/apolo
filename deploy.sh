
cd apolo-web
cd src
cd main
cd webapp
cd master

bower -f update

cd ..
cd ..
cd ..
cd ..
cd ..

~/tools/utils/apache-maven-3.2.3/bin/mvn clean install

cd apolo-api
~/tools/utils/apache-maven-3.2.3/bin/mvn tomcat7:redeploy
cd ..

cd apolo-web
~/tools/utils/apache-maven-3.2.3/bin/mvn tomcat7:redeploy
