(function () {
    'use strict';

    var app = angular
        .module('apolo.login')
        .controller('UserController', UserController)

    ;

    UserController.$inject = [
        '$state',
        '$rootScope',
        '$scope',
        '$translate',
        '$stateParams',
        '$location',
        'UserService',
        'PermissionGroupService'
    ];
    function UserController(
            $state,
            $rootScope,
            $scope,
            $translate,
            $stateParams,
            $location,
            userService,
            permissionGroupService
    ) {
        var vm = this;

        activate();

        ////////////////

        function activate() {

            vm.message = null;
            vm.messageType = null;

            vm.list = function() {
                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    var pageNumber = $location.search().pageNumber;

                    if (pageNumber == undefined) {
                        $location.$$path = "/user/list?pageNumber=1";
                        pageNumber = 1;
                    }

                    userService.list(
                            $rootScope.principal.tenant.url,
                            $rootScope.principal.token,
                            pageNumber).then(
                        function(response) {
                            vm.users = response;
                            $scope.pagination = vm.users.pagination;
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

                        userService.get(
                                $rootScope.principal.tenant.url,
                                $rootScope.principal.token,
                                $stateParams.id).then(

                            function(userResponse) {
                                if (userResponse != undefined) {
                                    permissionGroupService.list(
                                        $rootScope.principal.tenant.url,
                                        $rootScope.principal.token).then(

                                        function(groupResponse) {
                                            vm.groups = groupResponse.groupList;

                                            vm.user = userResponse;
                                        }
                                    );

                                } else {
                                    vm.message = $translate.instant('message.no_data_found');
                                    vm.messageType = "alert text-center alert-info";
                                }

                            }
                        );
                    } else {
                        $state.go("apolo.user.list");
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

                    permissionGroupService.list(
                        $rootScope.principal.tenant.url,
                        $rootScope.principal.token).then(

                        function(response) {
                            vm.groups = response.groupList;
                            vm.user = {};
                        }
                    );
                } else {
                    $state.go('login');
                }

                $scope.isReadonly = false;
            };

            $scope.uploadFile = function(files) {
                var fd = new FormData();

                fd.append('pictureFile', files[0]);

                vm.user.pictureFile = fd;
            };

            vm.create = function() {

                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    userService.create(
                        $rootScope.principal.tenant.url,
                        $rootScope.principal.token,
                        vm.user).then(

                        function(response) {
                            if (response.user != undefined && response.user.id != undefined) {
                                vm.user = response.user;

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

                        userService.get(
                            $rootScope.principal.tenant.url,
                            $rootScope.principal.token,
                            $stateParams.id).then(

                            function(userResponse) {
                                if (userResponse != undefined) {
                                    permissionGroupService.list(
                                        $rootScope.principal.tenant.url,
                                        $rootScope.principal.token).then(

                                        function(groupResponse) {
                                            vm.groups = groupResponse.groupList;

                                            vm.user = userResponse;
                                        }
                                    );

                                } else {
                                    vm.message = $translate.instant('message.no_data_found');
                                    vm.messageType = "alert text-center alert-info";
                                }

                            }
                        );
                    } else {
                        $state.go("apolo.user.list");
                    }

                } else {
                    $state.go('login');
                }

                $scope.isReadonly = false;
            };

            vm.edit = function() {
                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    userService.edit(
                        $rootScope.principal.tenant.url,
                        $rootScope.principal.token,
                        vm.user).then(

                        function(response) {
                            if (response.user != undefined && response.user.id != undefined) {
                                vm.user = response.user;

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
