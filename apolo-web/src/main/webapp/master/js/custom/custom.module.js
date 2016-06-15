(function() {
    'use strict';

    angular
        .module('custom', [
            // request the the entire framework
            'apolo',
            'apolo.baseService',
            /*...*/
            'apolo.login',
            'apolo.user',
            'apolo.permissionGroup'
        ]);
})();