#sudo apt-get install ruby
#sudo apt-get install ruby-compass

cd apolo-web
cd src
cd main
cd webapp
cd master
bower update
npm install
gulp --usesass

cd ..
cd ..
cd ..
cd ..
cd ..

mvn clean install

cd apolo-web
mvn clean install
docker build -t macielbombonato/apolo-web .
cd ..

cd apolo-api
mvn clean install
docker build -t macielbombonato/apolo-api .
cd ..

docker pull mysql
