(function() {
    'use strict';

    angular
        .module('apolo.base')

        //.constant('BASE_URL', 'http://localhost:8080')
        //.constant('FILE_URL', 'http://localhost:8080/file')

        .constant('BASE_URL', 'http://dimeric.com.br:8080')
        .constant('FILE_URL', 'http://dimeric.com.br:8080/file')

        .constant('GOOGLE_ANALYTICS_USER_ACCOUNT', 'UA-60177831-4')
        .constant('GOOGLE_AD_CLIENT', 'ca-pub-3481908664466605')
        .constant('GOOGLE_AD_SLOT', '6942381188');


})();
