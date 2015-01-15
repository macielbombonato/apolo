export MAVEN=~/tools/utils/apache-maven-3.2.3/bin/mvn

funcConfigProject() {
	echo ============================================== Packaging $1
	mkdir $1
	cd $1
	$MAVEN eclipse:clean eclipse:eclipse clean install
	cd ..
	echo ======== End packaging $1
}

funcConfigWebProject() {
	echo ============================================== Packaging $1
	mkdir $1
	cd $1
	$MAVEN eclipse:eclipse -Dwtpversion=2.0 clean install
	cd ..
	echo ======== End packaging $1
}

funcConfigProject    apolo-core-custom
funcConfigProject    apolo-core
funcConfigWebProject apolo-web

$MAVEN eclipse:clean eclipse:eclipse clean install