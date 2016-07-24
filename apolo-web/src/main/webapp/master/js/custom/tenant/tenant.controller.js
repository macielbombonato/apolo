(function () {
    'use strict';

    var app = angular
        .module('apolo.tenant')
        .controller('TenantController', TenantController)
    ;

    TenantController.$inject = [
        '$state',
        '$rootScope',
        '$scope',
        '$translate',
        '$stateParams',
        '$location',
        'TenantService'
    ];
    function TenantController(
            $state,
            $rootScope,
            $scope,
            $translate,
            $stateParams,
            $location,
            tenantService
    ) {
        var vm = this;

        activate();

        ////////////////

        function activate() {

            vm.message = null;
            vm.messageType = null;

            vm.tenant = {};

            vm.list = function() {
                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    $('.container-list').addClass('whirl line back-and-forth grow');

                    var pageNumber = $location.search().pageNumber;

                    if (pageNumber == undefined) {
                        $location.$$path = "/tenant/list?pageNumber=1";
                        pageNumber = 1;
                    }

                    tenantService.list(
                        $rootScope.principal.token,
                        pageNumber).then(
                        function(response) {
                            vm.tenants = response;
                            $scope.pagination = vm.tenants.pagination;

                            $('.container-list').removeClass('whirl line back-and-forth grow');
                        }
                    );
                } else {
                    $state.go('login');
                }

                $scope.isReadonly = true;
            };

            vm.view = function() {
                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    $('.container-form').addClass('whirl line back-and-forth grow');

                    if ($stateParams != undefined
                        && $stateParams.id != undefined) {

                        tenantService.get(
                                $rootScope.principal.token,
                                $stateParams.id).then(

                            function(response) {
                                if (response != undefined) {
                                    vm.tenant = response;
                                } else {
                                    vm.message = $translate.instant('message.no_data_found');
                                    vm.messageType = "alert text-center alert-info";
                                }

                                $('.container-form').removeClass('whirl line back-and-forth grow');
                            }
                        );
                    } else {
                        $state.go("apolo.tenant.list");
                    }
                } else {
                    $state.go('login');
                }

                $scope.isReadonly = true;
            };

            vm.createForm = function() {
                vm.message = null;
                vm.messageType = null;

                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    vm.tenant = {};

                } else {
                    $state.go('login');
                }

                $scope.isReadonly = false;
            };

            vm.create = function() {

                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    $('.container-form').addClass('whirl line back-and-forth grow');

                    tenantService.create(
                        $rootScope.principal.token,
                        vm.tenant).then(

                        function(response) {
                            if (response != undefined && response.id != undefined) {
                                vm.tenant = response;

                                vm.message = $translate.instant('message.success_create');
                                vm.messageType = "alert text-center alert-success";

                                $scope.isReadonly = true;
                            } else {
                                vm.message = $translate.instant('message.save_error');
                                vm.messageType = "alert text-center alert-danger";
                            }

                            $('.container-form').removeClass('whirl line back-and-forth grow');
                        }
                    );
                } else {
                    $state.go('login');
                }

                $scope.isReadonly = false;
            };

            vm.editForm = function() {
                vm.message = null;
                vm.messageType = null;

                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    if ($stateParams != undefined
                        && $stateParams.id != undefined) {

                        tenantService.get(
                            $rootScope.principal.token,
                            $stateParams.id).then(

                            function(entityResponse) {
                                if (entityResponse != undefined) {

                                    vm.tenant = entityResponse;

                                } else {
                                    vm.message = $translate.instant('message.no_data_found');
                                    vm.messageType = "alert text-center alert-info";
                                }

                            }
                        );
                    } else {
                        $state.go("apolo.permission-group.list");
                    }

                } else {
                    $state.go('login');
                }

                $scope.isReadonly = false;
            };

            vm.edit = function() {
                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    $('.container-form').addClass('whirl line back-and-forth grow');

                    tenantService.edit(
                        $rootScope.principal.token,
                        vm.tenant).then(

                        function(response) {
                            if (response != undefined && response.id != undefined) {
                                vm.tenant = response;

                                vm.message = $translate.instant('message.success_edit');
                                vm.messageType = "alert text-center alert-success";

                                $scope.isReadonly = true;
                            } else {
                                vm.message = $translate.instant('message.save_error');
                                vm.messageType = "alert text-center alert-danger";
                            }

                            $('.container-form').removeClass('whirl line back-and-forth grow');
                        }
                    );
                } else {
                    $state.go('login');
                }

                $scope.isReadonly = false;
            };

        }
    }
})();
