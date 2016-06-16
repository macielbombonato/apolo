(function() {
    'use strict';

    angular
        .module('custom', [
            // request the the entire framework
            'apolo',
            'apolo.baseService',
            /*...*/
            'apolo.auth',
            'apolo.user',
            'apolo.permissionGroup'
        ]);
})();