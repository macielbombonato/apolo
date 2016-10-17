#sudo apt-get install ruby
#sudo apt-get install ruby-compass

cd apolo-web
cd src
cd main
cd webapp
cd master
npm run

cd ..
cd ..
cd ..
cd ..
cd ..

mvn clean install

cd apolo-web
docker build -t macielbombonato/apolo-web .
cd ..

cd apolo-api
docker build -t macielbombonato/apolo-api .
cd ..

docker pull mysql
