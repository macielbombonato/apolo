(function() {
    'use strict';

    angular
        .module('apolo.baseService')
        .constant('BASE_URL', 'http://localhost:8080/api')
        .constant('FILE_URL', 'http://localhost:8080/api/file');

    //angular
    //    .module('apolo.baseService')
    //    .constant('BASE_URL', 'http://dimeric.com.br/api')
    //    .constant('FILE_URL', 'http://dimeric.com.br/api/file');
})();