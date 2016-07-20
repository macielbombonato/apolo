(function() {
    'use strict';

    angular
        .module('apolo.static')
        .service('StaticService', StaticService);

    StaticService.$inject = ['BaseService'];
    function StaticService(baseService) {

        this.permissions = permissions;

        ////////////////

        function permissions(apiToken) {
            var result = null;

            result = baseService.getWithKey('/static/permission', apiToken).then(
                function(response) {
                    var permissions = response;

                    return permissions;
                }
            );

            return result;
        }
    }

})();
