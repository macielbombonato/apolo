(function () {
    'use strict';

    var app = angular
        .module('apolo.user')
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
        'PermissionGroupService',
        'FileUploader',
        'BASE_URL'
    ];
    function UserController(
            $state,
            $rootScope,
            $scope,
            $translate,
            $stateParams,
            $location,
            userService,
            permissionGroupService,
            FileUploader,
            BASE_URL
    ) {
        var vm = this;

        vm.user = {};

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
                            $rootScope.tenant.url,
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
                                $rootScope.tenant.url,
                                $rootScope.principal.token,
                                $stateParams.id).then(

                            function(userResponse) {
                                if (userResponse != undefined) {
                                    permissionGroupService.list(
                                        $rootScope.principal.token).then(

                                        function(groupResponse) {
                                            vm.groups = groupResponse.list;

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
                        $rootScope.principal.token).then(

                        function(response) {
                            vm.groups = response.list;
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
                        $rootScope.tenant.url,
                        $rootScope.principal.token,
                        vm.user).then(

                        function(response) {
                            if (response != undefined && response.id != undefined) {
                                vm.user = response;

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
                            $rootScope.tenant.url,
                            $rootScope.principal.token,
                            $stateParams.id).then(

                            function(userResponse) {
                                if (userResponse != undefined) {
                                    permissionGroupService.list(
                                        $rootScope.principal.token).then(

                                        function(groupResponse) {
                                            vm.groups = groupResponse.list;

                                            vm.user = userResponse;

                                            vm.createUploader();
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

                    if (vm.user.id == $rootScope.principal.id) {
                        $rootScope.tenant = $rootScope.principal.tenant;
                        $rootScope.app.layout.theme = $rootScope.tenant.theme;

                        vm.user.token = $rootScope.principal.token;
                        vm.user.authenticated = $rootScope.principal.authenticated;

                        $rootScope.principal = vm.user;
                    }

                    userService.edit(
                        $rootScope.tenant.url,
                        $rootScope.principal.token,
                        vm.user).then(

                        function(response) {
                            if (response != undefined && response.id != undefined) {
                                vm.user = response;

                                vm.createUploader();

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

            vm.me = function() {
                if ($rootScope.principal != undefined
                    && $rootScope.principal != null) {

                    permissionGroupService.list(
                        $rootScope.principal.token).then(

                        function(groupResponse) {
                            vm.groups = groupResponse.list;

                            vm.user = $rootScope.principal;

                            vm.createUploader();
                        }
                    );

                } else {
                    $state.go('login');
                }

                $scope.isReadonly = true;
            };

            vm.createUploader = function() {

                var uploader = vm.uploader = new FileUploader({
                    url: BASE_URL + '/' + $rootScope.tenant.url + '/' + 'user' + '/' + vm.user.id + '/picture',
                    'headers': {
                        'Access-Control-Allow-Origin': '*',
                        'key': $rootScope.principal.token
                    }
                });

                // FILTERS

                vm.uploader.filters.push({
                    name: 'customFilter',
                    fn: function(/*item, options*/) {
                        return this.queue.length < 10;
                    }
                });

                // CALLBACKS

                vm.uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
                };
                vm.uploader.onAfterAddingFile = function(fileItem) {
                    vm.uploader.uploadAll();
                };
                vm.uploader.onAfterAddingAll = function(addedFileItems) {
                    vm.uploader.uploadAll();
                };
                vm.uploader.onBeforeUploadItem = function(item) {
                };
                vm.uploader.onProgressItem = function(fileItem, progress) {
                };
                vm.uploader.onProgressAll = function(progress) {
                };
                vm.uploader.onSuccessItem = function(fileItem, response, status, headers) {
                    $rootScope.principal.avatarFileName = $rootScope.principal.avatarFileName + '?=' + new Date();
                };
                vm.uploader.onErrorItem = function(fileItem, response, status, headers) {
                };
                vm.uploader.onCancelItem = function(fileItem, response, status, headers) {
                };
                vm.uploader.onCompleteItem = function(fileItem, response, status, headers) {
                };
                vm.uploader.onCompleteAll = function() {
                };
            }

            vm.uploader = null;

        }
    }
})();
