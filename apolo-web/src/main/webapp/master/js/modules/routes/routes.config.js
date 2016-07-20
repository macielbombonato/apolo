/**=========================================================
 * Module: config.js
 * App routes and resources configuration
 =========================================================*/


(function() {
    'use strict';

    angular
        .module('app.routes')
        .config(routesConfig);

    routesConfig.$inject = [
        '$stateProvider',
        '$locationProvider',
        '$urlRouterProvider',
        'RouteHelpersProvider'
    ];
    function routesConfig($stateProvider, $locationProvider, $urlRouterProvider, helper){
        
        // Set the following to true to enable the HTML5 Mode
        // You may have to set <base> tag in index and a routing configuration in your server
        $locationProvider.html5Mode(false);

        // defaults to dashboard
        $urlRouterProvider.otherwise('/home');

        // 
        // Application Routes
        // -----------------------------------   
        $stateProvider

            // Login
            .state('login', {
                url: '/login',
                title: 'Login',
                templateUrl: helper.basepath('auth/login.html'),
                controller: 'AuthController',
                controllerAs: 'auth'
            })

            .state('logout', {
                url: '/logout',
                title: 'Logout',
                templateUrl: helper.basepath('auth/logout.html')
            })

            .state('apolo', {
                url: '',
                abstract: true,
                templateUrl: helper.basepath('app.html'),
                resolve: helper.resolveFor('modernizr', 'icons')
            })
            .state('apolo.home', {
                url: '/home',
                title: 'Apolo',
                templateUrl: helper.basepath('home.html')
            })

            .state('apolo.error_403', {
                url: '/403',
                title: 'Apolo',
                templateUrl: helper.basepath('partials/error/403.html')
            })

            .state('apolo.error_404', {
                url: '/404',
                title: 'Apolo',
                templateUrl: helper.basepath('partials/error/404.html')
            })

            .state('apolo.error_500', {
                url: '/500',
                title: 'Apolo',
                templateUrl: helper.basepath('partials/error/500.html')
            })

            // User
            .state('apolo.user', {
                url: '/user',
                templateUrl: helper.basepath('partials/_base.html'),
                abstract: true
            })
            .state('apolo.user.list', {
                url: '/list?pageNumber=:number',
                title: 'User List',
                templateUrl: helper.basepath('user/list.html')
            })
            .state('apolo.user.view', {
                url: '/:id/view',
                title: 'User View',
                templateUrl: helper.basepath('user/view.html')
            })
            .state('apolo.user.create', {
                url: '/create',
                title: 'User Create',
                templateUrl: helper.basepath('user/create.html')
            })
            .state('apolo.user.edit', {
                url: '/:id/edit',
                title: 'User Edit',
                templateUrl: helper.basepath('user/edit.html')
            })

            .state('apolo.me', {
                url: '/me',
                title: 'Me',
                templateUrl: helper.basepath('user/me.html')
            })

            // Tenant
            .state('apolo.tenant', {
                url: '/tenant',
                templateUrl: helper.basepath('partials/_base.html'),
                abstract: true
            })
            .state('apolo.tenant.list', {
                url: '/list?pageNumber=:number',
                title: 'Tenant List',
                templateUrl: helper.basepath('tenant/list.html')
            })
            .state('apolo.tenant.view', {
                url: '/:id/view',
                title: 'Tenant View',
                templateUrl: helper.basepath('tenant/view.html')
            })
            .state('apolo.tenant.create', {
                url: '/create',
                title: 'Tenant Create',
                templateUrl: helper.basepath('tenant/create.html')
            })
            .state('apolo.tenant.edit', {
                url: '/:id/edit',
                title: 'Tenant Edit',
                templateUrl: helper.basepath('tenant/edit.html')
            })
            .state('apolo.tenant.changeTenant', {
                url: '/:id/changeTenant',
                title: 'Tenant Change',
                templateUrl: helper.basepath('tenant/list.html'),
                controller: "TenantChangeController"
            })

            // Group Permission
            .state('apolo.permission-group', {
                url: '/group',
                templateUrl: helper.basepath('partials/_base.html'),
                abstract: true
            })
            .state('apolo.permission-group.list', {
                url: '/list',
                title: 'Group Permission List',
                templateUrl: helper.basepath('permissionGroup/list.html')
            })
            .state('apolo.permission-group.view', {
                url: '/:id/view',
                title: 'Group Permission View',
                templateUrl: helper.basepath('permissionGroup/view.html')
            })
            .state('apolo.permission-group.create', {
                url: '/create',
                title: 'Group Permission Create',
                templateUrl: helper.basepath('permissionGroup/create.html')
            })
            .state('apolo.permission-group.edit', {
                url: '/:id/edit',
                title: 'Group Permission Edit',
                templateUrl: helper.basepath('permissionGroup/edit.html')
            })

          ;

    } // routesConfig

})();

