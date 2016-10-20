#sudo apt-get install ruby
#sudo apt-get install ruby-compass

echo '######################################'
echo '# Gerando artefatos da aplicacao WEB #'
echo '######################################'
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

echo '#############################'
echo '# Compilando todo o sistema #'
echo '#############################'
mvn clean install

echo '############################'
echo '# Compilando aplicacao WEB #'
echo '############################'
cd apolo-web
mvn clean install
echo '#########################################'
echo '# Gerando imagem docker da aplicacao WEB#'
echo '#########################################'
docker build -t macielbombonato/apolo-web . --rm
cd ..

echo '##################'
echo '# Compilando API #'
echo '##################'
cd apolo-api
mvn clean install
echo '################################'
echo '# Gerando imagem docker da API #'
echo '################################'
docker build -t macielbombonato/apolo-api . --rm
cd ..