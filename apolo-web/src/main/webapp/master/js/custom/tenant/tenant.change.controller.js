(function () {
    'use strict';

    var app = angular
        .module('apolo.tenant')
        .controller('TenantChangeController', TenantChangeController)
    ;

    TenantChangeController.$inject = [
        '$state',
        '$rootScope',
        '$scope',
        '$translate',
        '$stateParams',
        '$location',
        'TenantService'
    ];
    function TenantChangeController(
            $state,
            $rootScope,
            $scope,
            $translate,
            $stateParams,
            $location,
            tenantService
    ) {
        var vm = this;

        if ($rootScope.principal != undefined
            && $rootScope.principal != null) {

            if ($stateParams != undefined
                && $stateParams.id != undefined) {

                tenantService.changeTenant(
                    $rootScope.principal.token,
                    $stateParams.id).then(

                    function(response) {
                        if (response != undefined) {
                            vm.tenant = response;

                            if (response != undefined
                                && response != null) {
                                $rootScope.tenant = response;
                            }

                            if ($rootScope.tenant.theme != undefined
                                && $rootScope.tenant.theme != null) {
                                $rootScope.app.layout.theme = $rootScope.tenant.theme;
                            }
                        } else {
                            vm.message = $translate.instant('message.no_data_found');
                            vm.messageType = "alert text-center alert-info";
                        }

                    }
                );
            } else {
                $state.go("apolo.tenant.list");
            }
        } else {
            $state.go('login');
        }

        $scope.isReadonly = true;
    }
})();
