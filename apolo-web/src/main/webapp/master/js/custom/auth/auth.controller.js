(function () {
    'use strict';

    angular
        .module('apolo.auth')
        .controller('AuthController', AuthController);

    AuthController.$inject = [
        '$state',
        '$rootScope',
        '$translate',
        'AuthService'
    ];
    function AuthController($state, $rootScope, $translate, authService) {
        var vm = this;

        activate();

        ////////////////

        function activate() {
            // bind here all data from the form
            vm.account = {};
            // place the message if something goes wrong
            vm.authMsg = '';

            vm.login = function () {
                vm.authMsg = '';

                if (vm.loginForm.$valid) {

                    authService.login({username: vm.account.email, password: vm.account.password}).then(
                        function (response) {
                            if (response != undefined && response.id != null) {
                                $rootScope.principal = response;
                                $rootScope.principal.authenticated = true;

                                $state.go('apolo.home');

                                $rootScope.userBlockVisible = true;
                                $rootScope.$broadcast('toggleUserBlock');
                            } else {
                                vm.authMsg = $translate.instant('auth.error_message');
                            }
                        }
                    );

                } else {
                    // set as dirty if the user click directly to login so we show the validation messages
                    /*jshint -W106*/
                    vm.loginForm.account_email.$dirty = true;
                    vm.loginForm.account_password.$dirty = true;
                }

            };

            vm.logout = function() {
                $rootScope.principal = null;

                $state.go('login');
            }
        }
    }
})();
