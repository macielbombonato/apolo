(function() {
    'use strict';

    angular
        .module('apolo.base')
        .run(apoloRun);

    apoloRun.$inject = [
        '$rootScope',
        'FILE_URL'
    ];
    
    function apoloRun(
        $rootScope,
        FILE_URL
    ) {

        $rootScope.fileURL = FILE_URL;

    }

})();

