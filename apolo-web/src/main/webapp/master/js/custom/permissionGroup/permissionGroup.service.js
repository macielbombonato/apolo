(function() {
    'use strict';

    angular
        .module('apolo.permissionGroup')
        .service('PermissionGroupService', PermissionGroupService);

    PermissionGroupService.$inject = [
        '$translate',
        'BaseService'
    ];
    function PermissionGroupService(
        $translate,
        baseService
    ) {

        this.list = list;
        this.listAll = listAll;
        this.get = get;
        this.create = create;
        this.edit = edit;
        this.remove = remove;

        ////////////////

        function list(apiToken) {
            var result = null;

            if (baseService.checkPermission(['PERMISSION_GROUP_LIST', 'PERMISSION_GROUP_VIEW'])) {
                result = baseService.getWithKey('/permission-group', apiToken).then(
                    function(response) {
                        var groups = response;

                        return groups;
                    }
                );
            } else {
                result = $translate.instant('message.access_denied');
            }

            return result;
        }

        function listAll() {
            var result = null;

            result = baseService.get('/permission-group/list').then(
                function(response) {
                    var groups = response;

                    return groups;
                }
            );

            return result;
        }

        function get(apiToken, id) {
            var result = null;

            if (baseService.checkPermission(['PERMISSION_GROUP_LIST', 'PERMISSION_GROUP_VIEW'])) {
                result = baseService.getWithKey('/permission-group/' + id, apiToken).then(
                    function(response) {
                        var group = response;

                        return group;
                    }
                );
            } else {
                result = $translate.instant('message.access_denied');
            }

            return result;
        }

        function create(apiToken, entity) {
            var result = null;

            if (baseService.checkPermission(['PERMISSION_GROUP_CREATE'])) {
                result = baseService.postWithKey('/permission-group', entity, apiToken);
            } else {
                result = $translate.instant('message.access_denied');
            }

            return result;
        }

        function edit(apiToken, entity) {
            var result = null;

            if (baseService.checkPermission(['PERMISSION_GROUP_EDIT'])) {
                result = baseService.putWithKey('/permission-group', entity, apiToken);
            } else {
                result = $translate.instant('message.access_denied');
            }

            return result;
        }

        function remove(apiToken, id) {
            var result = null;

            if (baseService.checkPermission(['PERMISSION_GROUP_REMOVE'])) {
                result = baseService.deleteWithKey('/permission-group/' + id, apiToken);
            } else {
                result = $translate.instant('message.access_denied');
            }

            return result;
        }
    }

})();
