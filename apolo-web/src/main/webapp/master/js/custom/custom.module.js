(function() {
    'use strict';

    angular
        .module('custom', [
            // request the the entire framework
            /* vendor */
            'angularFileUpload',
            /* Apolo */
            'apolo',
            'apolo.baseService',
            /*...*/
            'apolo.static',
            'apolo.auth',
            'apolo.tenant',
            'apolo.user',
            'apolo.permissionGroup',
            'apolo.file'
        ]);
})();