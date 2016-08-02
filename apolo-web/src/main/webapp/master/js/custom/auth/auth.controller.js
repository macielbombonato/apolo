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
    function AuthController(
        $state,
        $rootScope,
        $translate,
        authService
    ) {

        var vm = this;

        activate();

        ////////////////

        function activate() {
            // bind here all data from the form
            vm.account = {};
            // place the message if something goes wrong
            vm.message = '';

            vm.login = function () {
                vm.message = '';

                if (vm.loginForm.$valid) {

                    $('.container-auth').addClass('whirl line back-and-forth grow');

                    authService.login({username: vm.account.email, password: vm.account.password}).then(
                        function (response) {
                            if (response != undefined && response.id != null) {
                                $rootScope.principal = response;
                                $rootScope.principal.authenticated = true;

                                if ($rootScope.principal.tenant != undefined
                                    && $rootScope.principal.tenant != null) {
                                    $rootScope.tenant = $rootScope.principal.tenant;
                                }

                                if ($rootScope.tenant.theme != undefined
                                    && $rootScope.tenant.theme != null) {
                                    $rootScope.app.layout.theme = $rootScope.tenant.theme;
                                }

                                $state.go('apolo.home');

                                $rootScope.userBlockVisible = true;
                                $rootScope.$broadcast('toggleUserBlock');

                                $('.container-auth').removeClass('whirl line back-and-forth grow');
                            } else {
                                vm.message = $translate.instant('auth.error_message');

                                $('.container-auth').removeClass('whirl line back-and-forth grow');
                            }
                        }
                    );

                } else {
                    // set as dirty if the user click directly to login so we show the validation messages
                    /*jshint -W106*/
                    vm.loginForm.account_email.$dirty = true;
                    vm.loginForm.account_password.$dirty = true;

                    $('.container-auth').removeClass('whirl line back-and-forth grow');
                }

            };

            vm.logout = function() {
                $rootScope.principal = null;

                $state.go('login');
            }
        }
    }
})();
