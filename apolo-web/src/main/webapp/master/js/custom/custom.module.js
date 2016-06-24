(function() {
    'use strict';

    angular
        .module('custom', [
            // request the the entire framework
            'apolo',
            'apolo.baseService',
            /*...*/
            'apolo.static',
            'apolo.auth',
            'apolo.tenant',
            'apolo.user',
            'apolo.permissionGroup'
        ]);
})();