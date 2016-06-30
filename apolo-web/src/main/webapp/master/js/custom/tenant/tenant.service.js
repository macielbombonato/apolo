(function() {
    'use strict';

    angular
        .module('apolo.tenant')
        .service('TenantService', TenantService);

    TenantService.$inject = ['BaseService'];
    function TenantService(baseService) {

        this.list = list;
        this.get = get;
        this.create = create;
        this.edit = edit;
        this.remove = remove;
        this.changeTenant = changeTenant;

        ////////////////

        function changeTenant(apiToken, id) {
            var result = null;

            if (baseService.checkPermission(['TENANT_MANAGER'])) {
                result = baseService.getWithKey('/tenant/changeTenant/' + id, apiToken).then(
                    function(response) {
                        var tenant = response;

                        return tenant;
                    }
                );
            } else {
                result = $translate.instant('message.access_denied');
            }

            return result;
        }

        function list(apiToken, pageNumber) {
            var result = null;

            if (baseService.checkPermission(['TENANT_LIST', 'TENANT_VIEW'])) {
                result = baseService.getWithKey('/tenant?pageNumber=' + pageNumber, apiToken).then(
                    function(response) {
                        response = baseService.pagination(pageNumber, response, '#/tenant/list?pageNumber=');

                        return response;
                    }
                );
            } else {
                result = $translate.instant('message.access_denied');
            }

            return result;
        }

        function get(apiToken, id) {
            var result = null;

            if (baseService.checkPermission(['TENANT_LIST', 'TENANT_VIEW'])) {
                result = baseService.getWithKey('/tenant/' + id, apiToken).then(
                    function(response) {
                        var tenant = response;

                        return tenant;
                    }
                );
            } else {
                result = $translate.instant('message.access_denied');
            }

            return result;
        }

        function create(apiToken, entity) {
            var result = null;

            if (baseService.checkPermission(['TENANT_CREATE'])) {
                result = baseService.postWithKey('/tenant', entity, apiToken);
            } else {
                result = $translate.instant('message.access_denied');
            }

            return result;
        }

        function edit(apiToken, entity) {
            var result = null;

            if (baseService.checkPermission(['TENANT_EDIT'])) {
                result = baseService.putWithKey('/tenant', entity, apiToken);
            } else {
                result = $translate.instant('message.access_denied');
            }

            return result;
        }

        function remove(apiToken, id) {
            var result = null;

            if (baseService.checkPermission(['TENANT_REMOVE'])) {
                result = baseService.deleteWithKey('/tenant/' + id, apiToken);
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
