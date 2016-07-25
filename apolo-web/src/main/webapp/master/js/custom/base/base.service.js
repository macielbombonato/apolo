(function() {
    'use strict';

    angular
        .module('apolo.base')
        .service('BaseService', BaseService);

    BaseService.$inject = [
        'BASE_URL',
        '$http',
        '$state',
        '$rootScope',
        '$translate'
    ];
    function BaseService(BASE_URL, $http, $state, $rootScope, $translate) {
        this.get = get;
        this.getWithKey = getWithKey;
        this.post = post;
        this.postWithKey = postWithKey;
        this.putWithKey = putWithKey;
        this.deleteWithKey = deleteWithKey;

        this.checkPermission = checkPermission;
        this.pagination = pagination;

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

        function pagination(pageNumber, entity, url) {
            if (pageNumber != undefined && pageNumber != null) {
                entity.currentIndex = pageNumber;
            } else {
                entity.currentIndex = 1;
            }

            entity.firstUrl = url + 1;

            var currentIndex = parseInt(entity.currentIndex);

            if (currentIndex > 1) {
                entity.prevUrl = url + (currentIndex - 1);
            }

            if (entity.currentIndex < entity.totalPages) {
                entity.nextUrl = url + (currentIndex + 1);
            }

            entity.lastUrl = url + entity.totalPages;

            entity.pages = [];
            for (var i = 1; i <= entity.totalPages; i ++) {
                var page = {};
                page.index = i;
                page.url = url + i;

                entity.pages.push(page);
            }

            entity.pagination = {};
            entity.pagination.currentIndex = entity.currentIndex;
            entity.pagination.firstUrl = entity.firstUrl;
            entity.pagination.prevUrl = entity.prevUrl;
            entity.pagination.nextUrl = entity.nextUrl;
            entity.pagination.lastUrl = entity.lastUrl;
            entity.pagination.pages = entity.pages;
            entity.pagination.totalElements = entity.totalElements;
            entity.pagination.totalPages = entity.totalPages;

            return entity;
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
            console.error(errorResponse);

            if (errorResponse != undefined) {
                var route = 'apolo.error_' + errorResponse.status;

                if (errorResponse.status == 401) {
                    $state.go('login');
                } else {
                    $state.go(route);
                }
            }

            return errorResponse;
        }
    }

})();
