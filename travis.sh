
export SONAR_HOST_URL="https://sonarcloud.io"
export SONAR_ORGANIZATION="macielbombonato-github"
export SONAR_TOKEN="94dab22c83c533a9ccd751e9646708e6d1117a00"

mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar \
	-Dsonar.host.url=$SONAR_HOST_URL \
	-Dsonar.github.oauth=$GITHUB_TOKEN \
	-Dsonar.login=$SONAR_ORGANIZATION \
	-Dsonar.password=$SONAR_TOKEN
