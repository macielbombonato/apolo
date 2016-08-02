(function () {
    'use strict';

    var app = angular
        .module('apolo.home')
        .controller('HomeController', HomeController)

    ;

    HomeController.$inject = [
        '$rootScope',
        '$translate'
    ];
    function HomeController(
            $rootScope,
            $translate
    ) {
        $rootScope.pageTitle = $translate.instant('app.home.title');
        $rootScope.pageDescription = $translate.instant('app.home.description');
    }
})();
