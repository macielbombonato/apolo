
export SONAR_HOST_URL="https://sonarcloud.io"
export SONAR_ORGANIZATION="macielbombonato-github"
export SONAR_PROJECT_KEY="apolo:apolo"
export SONAR_TOKEN="94dab22c83c533a9ccd751e9646708e6d1117a00"

export SONAR_EXCLUSIONS="*/bower_components/*, */target/*, */node_modules/*"

mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar \
	-Dsonar.host.url=$SONAR_HOST_URL \
	-Dsonar.projectKey=$SONAR_PROJECT_KEY \
	-Dsonar.github.oauth=$GITHUB_TOKEN \
	-Dsonar.login=$SONAR_TOKEN \
	-Dsonar.exclusions=$SONAR_EXCLUSIONS
