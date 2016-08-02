(function() {
    'use strict';

    angular
        .module('apolo.base')
        .run(apoloRun);

    apoloRun.$inject = [
        '$window',
        '$rootScope',
        'FILE_URL',
        'GOOGLE_ANALYTICS_USER_ACCOUNT',
        'GOOGLE_AD_CLIENT',
        'GOOGLE_AD_SLOT'
    ];
    
    function apoloRun(
        $window,
        $rootScope,
        FILE_URL,
        GOOGLE_ANALYTICS_USER_ACCOUNT,
        GOOGLE_AD_CLIENT,
        GOOGLE_AD_SLOT
    ) {
        $rootScope.fileURL = FILE_URL;
        $rootScope.googleAnalyticsUserAccount = GOOGLE_ANALYTICS_USER_ACCOUNT;
        $rootScope.googleAdClient = GOOGLE_AD_CLIENT;
        $rootScope.googleAdSlot = GOOGLE_AD_SLOT;

        $window.ga('create', $rootScope.googleAnalyticsUserAccount, 'auto');

        $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
            try {
                var url = toState.url;

                if (toParams.pageNumber != undefined) {
                    url = url.replace(':number', toParams.pageNumber);
                }

                if (toParams.id != undefined) {
                    url = url.replace(':id', toParams.id);
                }

                $window.ga('set', 'location', url);

                $window.ga('set', 'title', toState.title);

                if ($rootScope.principal != undefined
                    && $rootScope.principal != null
                    && $rootScope.principal.email != null) {
                    $window.ga("set", "userId", $rootScope.principal.email);
                }

                $window.ga('send', 'pageview');
            }
            catch(err) {
                console.error(err.message);
            }
        });

    }

})();

