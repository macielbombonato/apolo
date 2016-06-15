apolo.config(
    [
        '$stateProvider',
        '$urlRouterProvider',
        '$translateProvider',
        function($stateProvider, $urlRouterProvider, $translateProvider) {
            // App Translation
            $translateProvider.useStaticFilesLoader({
                prefix : 'resources/i18n/',
                suffix : '.json'
            });

            $translateProvider.preferredLanguage('pt_BR');
            $translateProvider.usePostCompiling(true);
            $translateProvider.useSanitizeValueStrategy('sanitizeParameters');
            $translateProvider.fallbackLanguage([
                'pt-BR'
            ]);

            // App routes
            $stateProvider.state('login', {
                url: '/login',
                templateUrl: 'auth/_login.html',
                controller: 'AuthCtrl',
                onEnter: [
                    '$state',
                    'Auth',
                    'authorization',
                    function($state, Auth, authorization) {
                        if(authorization.isAuthenticated()) {
                            $state.go('home');
                        }
                    }
                ]
            });

            $stateProvider.state('logout', {
                url: '/logout',
                templateUrl: 'auth/_login.html',
                resolve: {
                    postPromise: [
                        'authorization',
                        function(authorization) {
                            return authorization.logout();
                        }
                    ]
                }
            });

            $stateProvider.state('register', {
                url: '/register',
                templateUrl: 'auth/_register.html',
                controller: 'AuthCtrl',
                onEnter: [
                    '$state',
                    'Auth',
                    function($state, Auth) {
                        Auth.currentUser().then(
                            function () {
                                $state.go('home');
                            }
                        );
                    }
                ]
            });

            $stateProvider.state('home', {
                url: '/home',
                templateUrl: 'patients/_home.html',
                controller: 'PatientsCtrl',
                resolve: {
                    postPromise: [
                        'patients',
                        function(patients) {
                            return patients.getAll();
                        }
                    ],
                    authorize: ['authorization',
                        function(authorization) {
                            if (!authorization.authorize()) {
                                $state.go('login');
                            }
                        }
                    ]
                }
            });

            $stateProvider.state('patients', {
                url: '/patients',
                templateUrl: 'patients/_home.html',
                controller: 'PatientsCtrl',
                resolve: {
                    postPromise: [
                        'patients',
                        function(patients) {
                            return patients.getAll();
                        }
                    ],
                    authorize: ['authorization',
                        function(authorization) {
                            if (!authorization.authorize()) {
                                $state.go('login');
                            }
                        }
                    ]
                }
            });

            $stateProvider.state('patients_detail', {
                url: '/patients/:patientId',
                templateUrl: 'patients/_details.html',
                controller: 'PatientsDetailsCtrl',
                resolve: {
                    postPromise: [
                        'patients',
                        '$stateParams',
                        function(patients, $stateParams) {
                            var patient = patients.get($stateParams.patientId);
                            return patient;
                        }
                    ],
                    authorize: ['authorization',
                        function(authorization) {
                            if (!authorization.authorize()) {
                                $state.go('login');
                            }
                        }
                    ]
                }
            });

            $stateProvider.state('patients_create', {
                url: '/patients/create',
                templateUrl: 'patients/_create.html',
                controller: 'PatientsCreateCtrl'
            });

            $urlRouterProvider.otherwise('login');

        }
    ]
);
