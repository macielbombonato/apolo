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
                templateUrl: helper.basepath('login/login.html'),
                controller: 'LoginController',
                controllerAs: 'login'
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

            // User
            .state('apolo.user', {
                url: '/user',
                templateUrl: helper.basepath('user/_base.html'),
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

            // Tenant
            .state('apolo.tenant', {
                url: '/tenant',
                abstract: true
            })
            .state('apolo.tenant.list', {
                url: '/list',
                title: 'Tenant List',
                templateUrl: 'app/views/home.html'
            })
            .state('apolo.tenant.create', {
                url: '/create',
                title: 'Tenant Create',
                templateUrl: 'app/views/home.html'
            })

            // Group Permission
            .state('apolo.group-permission', {
                url: '/group',
                abstract: true
            })
            .state('app.group-permission.list', {
                url: '/list',
                title: 'Group Permission List',
                templateUrl: 'app/views/home.html'
            })
            .state('apolo.group-permission.create', {
                url: '/create',
                title: 'Group Permission Create',
                templateUrl: 'app/views/home.html'
            })


          // 
          // CUSTOM RESOLVES
          //   Add your own resolves properties
          //   following this object extend
          //   method
          // ----------------------------------- 
          // .state('app.someroute', {
          //   url: '/some_url',
          //   templateUrl: 'path_to_template.html',
          //   controller: 'someController',
          //   resolve: angular.extend(
          //     helper.resolveFor(), {
          //     // YOUR RESOLVES GO HERE
          //     }
          //   )
          // })
          ;

    } // routesConfig

})();

