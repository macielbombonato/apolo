(function () {
    'use strict';

    angular
        .module('apolo.base')
        .directive('apoloPermission', permission);

    permission.$inject = [
        '$state',
        '$rootScope',
        '$translate',
        'BaseService'
    ];
    function permission(
        $state,
        $rootScope,
        $translate,
        baseService
    ) {
        function link( $scope, element, attributes ) {

            var permissions = [];

            if (attributes.apoloPermission != undefined
                && attributes.apoloPermission.length > 0) {
                permissions = attributes.apoloPermission.split(',');

                for (var i = 0; i < permissions.length; i++) {
                    permissions[i] = permissions[i].trim();
                }
            }

            if ( ! baseService.checkPermission(permissions) ) {
                element.remove();
            }
        }

        return({
            link: link,
            restrict: "A"
        });

    }
})();
