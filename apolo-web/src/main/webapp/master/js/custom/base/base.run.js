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

    }

})();

