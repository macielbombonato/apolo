(function() {
    'use strict';

    angular
        .module('apolo.auth')
        .service('AuthService', AuthService);

    AuthService.$inject = [
        '$http',
        'BASE_URL'
    ];
    function AuthService($http, BASE_URL) {
        this.login = login;

        ////////////////

        function login(entity) {
            var result = null;

            var requestUrl = BASE_URL + '/login';

            result = $http({
                'url': requestUrl,
                'method': 'POST',
                'headers': {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*'
                },
                'data': entity,
                'cache': false
            }).then(function(response){
                return response.data;
            }).catch(function(error){
                /*
                 The controller will send to the user a default error message in this case.
                 */
                return null;
            });

            return result;
        }
    }

})();
