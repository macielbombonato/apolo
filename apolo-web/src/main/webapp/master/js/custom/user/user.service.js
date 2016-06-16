(function() {
    'use strict';

    angular
        .module('apolo.user')
        .service('UserService', UserService);

    UserService.$inject = [
        '$translate',
        'BaseService'
    ];
    function UserService($translate, baseService) {

        this.list = list;
        this.get = get;
        this.create = create;
        this.edit = edit;
        this.remove = remove;

        ////////////////

        function list(tenantUrl, apiToken, pageNumber) {
            var result = null;

            if (baseService.checkPermission(['USER_LIST'])) {
                if (pageNumber != null && pageNumber != undefined) {
                    result = baseService.getWithKey('/'+ tenantUrl +'/user?pageNumber=' + pageNumber, apiToken).then(
                        function(response) {
                            return response;
                        }
                    );
                } else {
                    result = baseService.getWithKey('/'+ tenantUrl +'/user', apiToken).then(
                        function(response) {
                            return response;
                        }
                    );
                }
            } else {
                result = $translate.instant('message.access_denied');
            }

            return result;
        }

        function get(tenantUrl, apiToken, id) {
            var result = null;

            if (baseService.checkPermission(['USER_LIST'])) {
                result = baseService.getWithKey('/'+ tenantUrl +'/user/' + id, apiToken).then(
                    function(response) {
                        return response.user;
                    }
                );
            } else {
                result = $translate.instant('message.access_denied');
            }

            return result;
        }

        function create(tenantUrl, apiToken, entity) {
            var result = null;

            if (baseService.checkPermission(['USER_CREATE'])) {
                result = baseService.postWithKey('/'+ tenantUrl +'/user', entity, apiToken);
            } else {
                result = $translate.instant('message.access_denied');
            }

            return result;
        }

        function edit(tenantUrl, apiToken, entity) {
            var result = null;

            if (baseService.checkPermission(['USER_EDIT'])) {
                result = baseService.putWithKey('/'+ tenantUrl +'/user', entity, apiToken);
            } else {
                result = $translate.instant('message.access_denied');
            }

            return result;
        }

        function remove(tenantUrl, apiToken, id) {
            var result = null;

            if (baseService.checkPermission(['USER_REMOVE'])) {
                result = baseService.deleteWithKey('/'+ tenantUrl +'/user/' + id, apiToken);
            } else {
                result = $translate.instant('message.access_denied');
            }

            return result;
        }


        function dataServiceError(errorResponse) {
            $log.error('XHR Failed for ShowService');
            $log.error(errorResponse);
            return errorResponse;
        }
    }

})();