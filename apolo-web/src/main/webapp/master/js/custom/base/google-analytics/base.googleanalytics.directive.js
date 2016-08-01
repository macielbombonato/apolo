(function () {
    'use strict';

    angular
        .module('apolo.base')
        .directive('apoloGoogleAnalytics', analytics);

    analytics.$inject = [
        '$state',
        '$rootScope',
        '$translate',
        '$window',
        '$compile',
        'BaseService'
    ];
    function analytics(
        $state,
        $rootScope,
        $translate,
        $window,
        $compile,
        baseService
    ) {

        var analyticsTpl = '';
        analyticsTpl = analyticsTpl + '';
        analyticsTpl = analyticsTpl + '<div class="apoloGoogleAnalytics">';
        analyticsTpl = analyticsTpl + '<script>';
        analyticsTpl = analyticsTpl + '(function(i,s,o,g,r,a,m){i["GoogleAnalyticsObject"]=r;i[r]=i[r]||function(){';
        analyticsTpl = analyticsTpl + '(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),';
        analyticsTpl = analyticsTpl + 'm=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)';
        analyticsTpl = analyticsTpl + '})(window,document,"script","https://www.google-analytics.com/analytics.js","ga");';

        analyticsTpl = analyticsTpl + 'ga("create", "' + $rootScope.googleAnalyticsUserAccount + '", "auto");';

        analyticsTpl = analyticsTpl + '</script>';
        analyticsTpl = analyticsTpl + '</div>';


        function link( $scope, element, attributes ) {

            element.html("");
            element.append(angular.element($compile(analyticsTpl)($scope)));
            if (!$window.apoloGoogleAnalytics) {
                $window.apoloGoogleAnalytics = [];
            }
            $window.apoloGoogleAnalytics.push({});
        }

        return({
            link: link,
            restrict: "A",
            transclude: true,
            template: analyticsTpl
        });

    }
})();
