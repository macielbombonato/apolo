(function () {
    'use strict';

    var app = angular
        .module('apolo.permissionGroup')
        .controller('PermissionGroupController', PermissionGroupController)

    ;

    PermissionGroupController.$inject = [
        '$state',
        '$rootScope',
        '$scope',
        '$translate',
        '$stateParams',
        '$location',
        'PermissionGroupService',
        'StaticService'
    ];
    function PermissionGroupController(
            $state,
            $rootScope,
            $scope,
            $translate,
            $stateParams,
            $location,
            permissionGroupService,
            staticService
    ) {
        var vm = this;

        activate();

        ////////////////

        function activate() {

            vm.message = null;
            vm.messageType = null;

            vm.permissions = [];
            vm.group = {};

            vm.list = function() {
                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    permissionGroupService.list(
                            $rootScope.principal.token).then(
                        function(response) {
                            vm.groups = response;
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

                    if ($stateParams != undefined
                        && $stateParams.id != undefined) {

                        permissionGroupService.get(
                                $rootScope.principal.token,
                                $stateParams.id).then(

                            function(response) {
                                if (response != undefined) {
                                    vm.group = response;
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

                $scope.isReadonly = true;
            };

            vm.createForm = function() {
                vm.message = null;
                vm.messageType = null;

                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    staticService.permissions(
                        $rootScope.principal.token).then(

                        function(response) {
                            vm.permissions = response.list;
                            vm.group = {};
                        }
                    );
                } else {
                    $state.go('login');
                }

                $scope.isReadonly = false;
            };

            vm.create = function() {

                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    permissionGroupService.create(
                        $rootScope.principal.token,
                        vm.group).then(

                        function(response) {
                            if (response != undefined && response.id != undefined) {
                                vm.group = response;

                                vm.message = $translate.instant('message.success_create');
                                vm.messageType = "alert text-center alert-success";

                                $scope.isReadonly = true;
                            } else {
                                vm.message = $translate.instant('message.save_error');
                                vm.messageType = "alert text-center alert-danger";
                            }
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

                        permissionGroupService.get(
                            $rootScope.principal.token,
                            $stateParams.id).then(

                            function(groupResponse) {
                                if (groupResponse != undefined) {
                                    staticService.permissions(
                                        $rootScope.principal.token).then(

                                        function(staticResponse) {
                                            vm.group = groupResponse;

                                            vm.permissions = staticResponse.list;
                                        }
                                    );

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

                    permissionGroupService.edit(
                        $rootScope.principal.token,
                        vm.group).then(

                        function(response) {
                            if (response != undefined && response.id != undefined) {
                                vm.group = response;

                                vm.message = $translate.instant('message.success_edit');
                                vm.messageType = "alert text-center alert-success";

                                $scope.isReadonly = true;
                            } else {
                                vm.message = $translate.instant('message.save_error');
                                vm.messageType = "alert text-center alert-danger";
                            }
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
