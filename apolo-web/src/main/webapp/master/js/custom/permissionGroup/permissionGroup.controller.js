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

                $rootScope.pageTitle = $translate.instant('permissionGroup.list.title');
                $rootScope.pageDescription = $translate.instant('permissionGroup.list.description');

                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    $('.container-list').addClass('whirl line back-and-forth grow');

                    permissionGroupService.list(
                            $rootScope.principal.token).then(
                        function(response) {
                            vm.groups = response;

                            $('.container-list').removeClass('whirl line back-and-forth grow');
                        }
                    );
                } else {
                    $state.go('login');
                }

                $scope.isReadonly = true;
            };

            vm.view = function() {

                $rootScope.pageTitle = $translate.instant('permissionGroup.view.title');
                $rootScope.pageDescription = $translate.instant('permissionGroup.view.description');

                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    $('.container-form').addClass('whirl line back-and-forth grow');

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

                                $('.container-form').removeClass('whirl line back-and-forth grow');
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

                $rootScope.pageTitle = $translate.instant('permissionGroup.create.title');
                $rootScope.pageDescription = $translate.instant('permissionGroup.create.description');

                vm.message = null;
                vm.messageType = null;

                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    $('.permission-list').addClass('whirl line back-and-forth grow');

                    staticService.permissions(
                        $rootScope.principal.token).then(

                        function(response) {
                            vm.permissions = response.list;

                            $('.permission-list').removeClass('whirl line back-and-forth grow');
                        }
                    );

                    vm.group = {};
                } else {
                    $state.go('login');
                }

                $scope.isReadonly = false;
            };

            vm.create = function() {

                $rootScope.pageTitle = $translate.instant('permissionGroup.create.title');
                $rootScope.pageDescription = $translate.instant('permissionGroup.create.description');

                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    $('.container-form').addClass('whirl line back-and-forth grow');

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

                            $('.container-form').removeClass('whirl line back-and-forth grow');
                        }
                    );
                } else {
                    $state.go('login');
                }

                $scope.isReadonly = false;
            };

            vm.editForm = function() {

                $rootScope.pageTitle = $translate.instant('permissionGroup.edit.title');
                $rootScope.pageDescription = $translate.instant('permissionGroup.edit.description');

                vm.message = null;
                vm.messageType = null;

                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    $('.container-form').addClass('whirl line back-and-forth grow');

                    if ($stateParams != undefined
                        && $stateParams.id != undefined) {

                        permissionGroupService.get(
                            $rootScope.principal.token,
                            $stateParams.id).then(

                            function(groupResponse) {
                                if (groupResponse != undefined) {

                                    $('.permission-list').addClass('whirl line back-and-forth grow');

                                    staticService.permissions(
                                        $rootScope.principal.token).then(

                                        function(staticResponse) {
                                            vm.permissions = staticResponse.list;

                                            $('.permission-list').removeClass('whirl line back-and-forth grow');
                                        }
                                    );

                                    vm.group = groupResponse;

                                } else {
                                    vm.message = $translate.instant('message.no_data_found');
                                    vm.messageType = "alert text-center alert-info";
                                }

                                $('.container-form').removeClass('whirl line back-and-forth grow');
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

                $rootScope.pageTitle = $translate.instant('permissionGroup.edit.title');
                $rootScope.pageDescription = $translate.instant('permissionGroup.edit.description');

                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    $('.container-form').addClass('whirl line back-and-forth grow');

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
