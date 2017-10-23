#!/bin/sh
# Exit on failure
set -e

export SONAR_HOST_URL="https://sonarcloud.io"
export SONAR_TOKEN="5db4d3b663e7a6857fede685ec7dc4adcee636ac"

mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar \
	-Dsonar.host.url=$SONAR_HOST_URL \
	-Dsonar.analysis.mode=preview \
	-Dsonar.github.oauth=$GITHUB_TOKEN \
	-Dsonar.github.repository=$TRAVIS_REPO_SLUG \
	-Dsonar.github.pullRequest=$TRAVIS_PULL_REQUEST \
	-Dsonar.login=macielbombonato@github \
	-Dsonar.password=$SONAR_TOKEN
