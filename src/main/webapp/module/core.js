var allControlller = ['ngRoute','ngAnimate', 'ngSanitize', 'ui.bootstrap', 'index'];
var customController = [
    'home_list', 'corporate_list'
];

allControlller = allControlller.concat(customController);
customController.forEach(function(value) {
    $('head').append("<script type='text/javascript' src='module/"+value.split('_')[0]+"/"+value.replace('_','.')+".js'></script>");
});


angular
    .module('core', allControlller)
    .config(
        function($routeProvider, $httpProvider, $locationProvider, $compileProvider) {
            $locationProvider.html5Mode({
                enabled: false,
                requireBase: false
            });

            customController.forEach(function(value) {
                $routeProvider.when('/'+value, {
                    templateUrl : 'module/'+value.split('_')[0]+'/'+value.replace('_','.')+'.html',
                    controller : value,
                    controllerAs : 'controller'
                });
            });

            $routeProvider.when('/', {
                templateUrl : 'module/index/index.html',
                controller : 'index',
                controllerAs : 'controller'
            });

            $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
            $httpProvider.interceptors.push('myHttpInterceptor');
        }).run(function($rootScope, $http, $location) {
            $rootScope.url = function(url) {
                $location.path(url);
            };
        })
        .factory('myHttpInterceptor', function ($q, $window, $location) {
            var reqNumber = 0;
            var resNumber = 0;
            return {
                request: function(config) {
                    //console.log('req');
                    reqNumber++;
                    angular.element('.loader').show();
                    return config;
                },
                response : function(response) {
                    if ($location.path().length > 5)
                        $('li#'+$location.path().substring(1, $location.path().length)).addClass('active');

                    resNumber++;
                    if (reqNumber == resNumber) {
                        angular.element('.loader').fadeOut(400);
                    }

                    return response || $q.when(response);
                },
                responseError: function(reason) {
                    resNumber++;
                    if (reqNumber == resNumber) {
                        angular.element('.loader').fadeOut(400);
                    }

                    switch (reason.status) {
                        case 400: case 500: case 404:
                            //alert('Серверт алдаа гарлаа !');
                            break;
                        case 401:
                            window.location.href = window.location.origin + '/auth-logout';
                            break;
                        case 403:
                            alert('Хандах эрхгүй байна !');
                            break;
                        default : break;
                    }

                    return $q.reject(reason);
                }
            };
        })
        .directive('ngEnter', function () {
            return function (scope, element, attrs) {
                element.bind("keydown keypress", function (event) {
                    if(event.which === 13) {
                        scope.$apply(function (){
                            scope.$eval(attrs.ngEnter);
                        });

                        event.preventDefault();
                    }
                });
            };
        })
        .constant();