(function() {
    'use strict';

    angular
        .module('apolo.base')
        .run(apoloRun);

    apoloRun.$inject = [
        '$rootScope',
        'FILE_URL',
        'GOOGLE_ANALYTICS_USER_ACCOUNT',
        'GOOGLE_AD_CLIENT',
        'GOOGLE_AD_SLOT'
    ];
    
    function apoloRun(
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

        $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
            try {
                ga('set', 'title', toState.title);
                ga('set', 'location', toState.url);

                if ($rootScope.principal != undefined
                    && $rootScope.principal != null
                    && $rootScope.principal.email != null) {
                    ga("set", "userId", $rootScope.principal.email);
                }

                ga('send', 'pageview');
            }
            catch(err) {
                console.error(err.message);
            }
        });

    }

})();

