(function () {
    'use strict';

    angular
        .module('apolo.base')
        .directive('apoloGoogleAdsense', adSense);

    adSense.$inject = [
        '$state',
        '$rootScope',
        '$translate',
        '$window',
        '$compile',
        'BaseService'
    ];
    function adSense(
        $state,
        $rootScope,
        $translate,
        $window,
        $compile,
        baseService
    ) {

        var adSenseTpl = '';
        adSenseTpl = adSenseTpl + '';
        adSenseTpl = adSenseTpl + '<div ng-show="tenant.showAds || tenant == null" >';
        adSenseTpl = adSenseTpl + '<ins class="adsbygoogle responsive" style="display:block" data-ad-format="auto"';

        adSenseTpl = adSenseTpl + 'data-ad-client="' + $rootScope.googleAdClient + '" ';
        adSenseTpl = adSenseTpl + 'data-ad-slot="' + $rootScope.googleAdSlot + '"></ins></ins>';
        adSenseTpl = adSenseTpl + '';
        adSenseTpl = adSenseTpl + '</div>';

        function link( $scope, element, attributes ) {

            element.html("");
            element.append(angular.element($compile(adSenseTpl)($scope)));
            if (!$window.adsbygoogle) {
                $window.adsbygoogle = [];
            }
            $window.adsbygoogle.push({});
        }

        return({
            link: link,
            restrict: "A",
            transclude: true,
            template: adSenseTpl
        });

    }
})();
