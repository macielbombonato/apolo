(function () {
    'use strict';

    angular
        .module('apolo.login')
        .controller('LoginController', LoginController);

    LoginController.$inject = [
        '$state',
        '$rootScope',
        '$translate',
        'LoginService'
    ];
    function LoginController($state, $rootScope, $translate, loginService) {
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

                    loginService.login({username: vm.account.email, password: vm.account.password}).then(
                        function (response) {
                            if (response.user != undefined && response.user != null) {
                                console.log(response.user);

                                $rootScope.principal = response.user;
                                $rootScope.principal.authenticated = true;

                                $state.go('apolo.home');

                                $rootScope.userBlockVisible = true;
                                $rootScope.$broadcast('toggleUserBlock');
                            } else {
                                vm.authMsg = $translate.instant('login.error_message');
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
        }
    }
})();
