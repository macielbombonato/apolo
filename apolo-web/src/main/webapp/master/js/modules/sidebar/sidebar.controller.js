/**=========================================================
 * Module: sidebar-menu.js
 * Handle sidebar collapsible elements
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('app.sidebar')
        .controller('SidebarController', SidebarController);

    SidebarController.$inject = ['$rootScope', '$scope', '$state', 'SidebarLoader', 'Utils'];
    function SidebarController($rootScope, $scope, $state, SidebarLoader,  Utils) {

        activate();

        ////////////////

        function activate() {
          var collapseList = [];

          // demo: when switch from collapse to hover, close all items
          $rootScope.$watch('app.layout.asideHover', function(oldVal, newVal){
            if ( newVal === false && oldVal === true) {
              closeAllBut(-1);
            }
          });


          // Load menu from json file
          // ----------------------------------- 

          SidebarLoader.getMenu(sidebarReady);
          
          function sidebarReady(items) {
            $scope.menuItems = [];
            if (items != undefined
                && items.length > 0) {
              for(var i = 0; i < items.length; i++) {
                var item = validateMenuItem(items[i]);

                if (item != undefined && item != null) {
                  $scope.menuItems.push(items[i]);
                }
              }
            }
          }

          function validateMenuItem(item) {
            var result = null;
            var subItems = [];

            if (item.permissions != undefined
                && item.permissions != null
                && item.permissions.length > 0) {
              if ($rootScope.principal != undefined
                && $rootScope.principal != null) {
                for (var i = 0; i < item.permissions.length; i++) {
                  if ($rootScope.principal.permissions.indexOf('ADMIN') != -1
                      || $rootScope.principal.permissions.indexOf(item.permissions[i]) != -1) {
                    result = item;
                  }
                }
              }
            } else {
              result = item;
            }

            if (result != null) {
              if (item.submenu != null
                && item.submenu.length > 0) {
                for (var i = 0; i < item.submenu.length; i++) {
                  var submenu = validateMenuItem(item.submenu[i]);

                  if (submenu != undefined && submenu != null) {
                    subItems.push(submenu);
                  }
                }
              }

              result.submenu = subItems;
            }

            return result;
          }

          // Handle sidebar and collapse items
          // ----------------------------------
          
          $scope.getMenuItemPropClasses = function(item) {
            return (item.heading ? 'nav-heading' : '') +
                   (isActive(item) ? ' active' : '') ;
          };

          $scope.addCollapse = function($index, item) {
            collapseList[$index] = $rootScope.app.layout.asideHover ? true : !isActive(item);
          };

          $scope.isCollapse = function($index) {
            return (collapseList[$index]);
          };

          $scope.toggleCollapse = function($index, isParentItem) {

            // collapsed sidebar doesn't toggle drodopwn
            if( Utils.isSidebarCollapsed() || $rootScope.app.layout.asideHover ) return true;

            // make sure the item index exists
            if( angular.isDefined( collapseList[$index] ) ) {
              if ( ! $scope.lastEventFromChild ) {
                collapseList[$index] = !collapseList[$index];
                closeAllBut($index);
              }
            }
            else if ( isParentItem ) {
              closeAllBut(-1);
            }
            
            $scope.lastEventFromChild = isChild($index);

            return true;
          
          };

          // Controller helpers
          // ----------------------------------- 

            // Check item and children active state
            function isActive(item) {

              if(!item) return;

              if( !item.sref || item.sref === '#') {
                var foundActive = false;
                angular.forEach(item.submenu, function(value) {
                  if(isActive(value)) foundActive = true;
                });
                return foundActive;
              }
              else
                return $state.is(item.sref) || $state.includes(item.sref);
            }

            function closeAllBut(index) {
              index += '';
              for(var i in collapseList) {
                if(index < 0 || index.indexOf(i) < 0)
                  collapseList[i] = true;
              }
            }

            function isChild($index) {
              /*jshint -W018*/
              return (typeof $index === 'string') && !($index.indexOf('-') < 0);
            }
        
        } // activate
    }

})();
