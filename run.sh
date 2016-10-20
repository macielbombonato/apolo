#./generate-docker-containers.sh
echo '####################################'
echo '# Parando container Banco de dados #'
echo '####################################'
docker stop $(docker ps -a | grep mysql | cut -c1-15)

echo '######################################'
echo '# Removendo container Banco de dados #'
echo '######################################'
docker rm $(docker ps -a | grep mysql | cut -c1-15)

echo '#######################################'
echo '# Executando container Banco de dados #'
echo '#######################################'
docker run -d \
    -e MYSQL_ROOT_PASSWORD=root \
    --name mysql \
    -v ~/files/apolo/mysql:/var/lib/mysql \
    mysql

echo '###############################'
echo '# Parando container APOLO API #'
echo '###############################'
docker stop $(docker ps -a | grep apolo-api | cut -c1-15)

echo '#################################'
echo '# Removendo container APOLO API #'
echo '#################################'
docker rm $(docker ps -a | grep apolo-api | cut -c1-15)

echo '##################################'
echo '# Executando container APOLO API #'
echo '##################################'
docker run -d \
    -e APOLO_SECRET_KEY="1234567890ABCDEF" \
    -e APOLO_IV_KEY="1234567890ABCDEF" \
    -e APOLO_UPLOADED_FILES="/opt/files/" \
    -e APOLO_VIDEO_CONVERTER_EXECUTABLE_PATH="" \
    -e APOLO_PDF_IMAGE_EXTRACTOR_EXECUTABLE_PATH="" \
    -e APOLO_DEFAULT_TENANT="apolo" \
    -e APOLO_DEFAULT_emailFrom="maciel.bombonato@gmail.com" \
    -e APOLO_DEFAULT_emailUsername="maciel.bombonato@gmail.com" \
    -e APOLO_DEFAULT_emailPassword="pass" \
    -e APOLO_DEFAULT_smtpHost="smtp" \
    -e APOLO_DEFAULT_smtpPort="587" \
    -e APOLO_DEFAULT_useTLS="true" \
    -e APOLO_SEND_AUTH_EMAIL="true" \
    -e APOLO_DATASOURCE_DRIVER_CLASS="com.mysql.jdbc.Driver" \
    -e APOLO_HIBERNATE_DIALECT="org.hibernate.dialect.MySQL5InnoDBDialect" \
    -e APOLO_HIBERNATE_HBM2DDL="update" \
    -e APOLO_DATASOURCE_URL="jdbc:mysql://mysql:3306/apolo?createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=utf-8" \
    -e APOLO_DATASOURCE_USERNAME="root" \
    -e APOLO_DATASOURCE_PASSWORD="root" \
    -e APOLO_HIBERNATE_SHOW_AND_FORMAT_SQL="true" \
    --name apolo-api \
    -v ~/files/apolo/files:/opt/files \
    --link mysql \
    -p 8080:8080 \
    macielbombonato/apolo-api

echo '###############################'
echo '# Parando container APOLO WEB #'
echo '###############################'
docker stop $(docker ps -a | grep apolo-web | cut -c1-15)

echo '#################################'
echo '# Removendo container APOLO WEB #'
echo '#################################'
docker rm $(docker ps -a | grep apolo-web | cut -c1-15)

echo '##################################'
echo '# Executando container APOLO WEB #'
echo '##################################'
docker run -d \
    --name apolo-web \
    -p 80:80 \
    macielbombonato/apolo-web
