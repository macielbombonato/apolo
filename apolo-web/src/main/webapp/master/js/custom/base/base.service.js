(function() {
    'use strict';

    angular
        .module('apolo.baseService')
        .service('BaseService', BaseService);

    BaseService.$inject = [
        'BASE_URL',
        '$http',
        '$log',
        '$rootScope'
    ];
    function BaseService(BASE_URL, $http, $log, $rootScope) {
        this.get = get;
        this.getWithKey = getWithKey;
        this.post = post;
        this.postWithKey = postWithKey;
        this.putWithKey = putWithKey;
        this.deleteWithKey = deleteWithKey;

        this.checkPermission = checkPermission;

        ////////////////

        function checkPermission(neededPermissions) {
            var result = false;

            if ($rootScope.principal != undefined
                && $rootScope.principal.permissions != undefined) {
                if ($rootScope.principal.permissions.indexOf('ADMIN') != -1) {
                    result = true;
                } else {
                    if (neededPermissions != undefined && neededPermissions.length > 0) {
                        for (var i = 0; i < neededPermissions.length; i++) {
                            if ($rootScope.principal.permissions.indexOf(neededPermissions[i]) != -1) {
                                result = true;
                            }
                        }
                    }
                }
            }

            return result;
        }

        function get(url) {
            var requestUrl = BASE_URL + url;

            return $http({
                'url': requestUrl,
                'method': 'GET',
                'headers': {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*'
                },
                'cache': false
            }).then(function(response){
                return response.data;
            }).catch(dataServiceError);
        }

        function getWithKey(url, apiToken) {
            var requestUrl = BASE_URL + url;

            return $http({
                'url': requestUrl,
                'method': 'GET',
                'headers': {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*',
                    'key': apiToken
                },
                'cache': false
            }).then(function(response){
                return response.data;
            }).catch(dataServiceError);
        }

        function post(url, entity) {
            var requestUrl = BASE_URL + url;

            return $http({
                'url': requestUrl,
                'method': 'POST',
                'headers': {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*'
                },
                'data': entity,
                'cache': false
            }).then(function(response){
                return response.data;
            }).catch(dataServiceError);
        }

        function postWithKey(url, entity, apiToken) {
            var requestUrl = BASE_URL + url;

            return $http({
                'url': requestUrl,
                'method': 'POST',
                'headers': {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*',
                    'key': apiToken
                },
                'data': entity,
                'cache': false
            }).then(function(response){
                return response.data;
            }).catch(dataServiceError);
        }

        function putWithKey(url, entity, apiToken) {
            var requestUrl = BASE_URL + url;

            return $http({
                'url': requestUrl,
                'method': 'PUT',
                'headers': {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*',
                    'key': apiToken
                },
                'data': entity,
                'cache': false
            }).then(function(response){
                return response.data;
            }).catch(dataServiceError);
        }

        function deleteWithKey(url, apiToken) {
            var requestUrl = BASE_URL + url;

            return $http({
                'url': requestUrl,
                'method': 'DELETE',
                'headers': {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*',
                    'key': apiToken
                },
                'cache': false
            }).then(function(response){
                return response.data;
            }).catch(dataServiceError);
        }

        function dataServiceError(errorResponse) {
            $log.error('XHR Failed for BaseService');
            $log.error(errorResponse);
            return errorResponse;
        }
    }

})();
