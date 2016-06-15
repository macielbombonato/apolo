(function() {
    'use strict';

    angular
        .module('apolo.login')
        .service('LoginService', LoginService);

    LoginService.$inject = ['BaseService'];
    function LoginService(baseService) {
        this.login = login;

        ////////////////

        function login(entity) {
            var result = baseService.post('/login', entity);
            return result;
        }
    }

})();
