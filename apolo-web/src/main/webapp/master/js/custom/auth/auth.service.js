(function() {
    'use strict';

    angular
        .module('apolo.auth')
        .service('AuthService', AuthService);

    AuthService.$inject = ['BaseService'];
    function AuthService(baseService) {
        this.login = login;

        ////////////////

        function login(entity) {
            var result = baseService.post('/login', entity);
            return result;
        }
    }

})();
