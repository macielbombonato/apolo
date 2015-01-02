export MAVEN=~/tools/utils/apache-maven-3.2.3/bin/mvn

funcConfig() {
	echo ============================================== Packaging $1
	mkdir $1
	cd $1
	$MAVEN eclipse:eclipse -Dwtpversion=2.0 clean install
	cd ..
	echo ======== End packaging $1
}

funcConfigTwoLevels() {
	echo ============================================== Packaging $1
	mkdir $2
	mkdir $2/$1
	cd $2/$1
	$MAVEN eclipse:eclipse -Dwtpversion=2.0 clean install
	cd ..
	cd ..
	echo ======== End packaging $1
}


funcConfig apolo-custom-core
funcConfig apolo-core
funcConfig apolo-web

$MAVEN eclipse:eclipse -Dwtpversion=2.0 clean install