angular.module('home_list', ['base64']).controller('home_list', function($rootScope, $http, $scope, $location, $base64) {
    $scope.list = [];
    $scope.total = 0;
    $scope.page = 1;
    $scope.size = 10;
    $scope.qr = 'easy pay';
    $scope.logged = false;
    $scope.inter = null;
    $scope.providers = [];

    $scope.sessionCheck = function() {
        $http.get('login/session').then(function(response) {
            if (response.data.status == 'active') {
                $scope.logged = true;
                $scope.walletId = response.data.walletId;
                $scope.findProviders();
                $scope.find();
            } else {
                $scope.logged = false;
                $http.get('login/request').then(function(response) {
                    $scope.qr = response.data;
                }, function(response) {
                });

                $scope.inter = setInterval(function() {
                    $http.get('login/check?qr='+$scope.qr).then(function(response) {
                        if (response.data.status  == 'active') {
                            clearInterval($scope.inter);
                            $scope.logged = true;
                            $scope.sessionCheck();
                        }
                    }, function(response) {
                    });
                }, 3000);
            }
        }, function(response) {

        });
    };

    $scope.sessionCheck();
    
    $scope.findProviders = function() {
        $http.get('providers/findAll?page=1&size=10&order=id&dir=asc').then(function(response) {
            $scope.providers = response.data.data;
        }, function(response) {
            $scope.providers = [];
        });
    };

    $scope.find = function() {
        var fun = 'findOne?walletId='+$scope.walletId;
        $scope.list = [];
        $http.get('wallets/'+fun+'&page='+$scope.page+'&size='+$scope.size+'&order=id&dir=asc').then(function(response) {
            response.data.cards.forEach(function(value) {
                var obj = JSON.parse((value.enc));
                obj['status'] = value.status;
                obj['walletId'] = value.walletId;
                obj['id'] = value.id;
                $scope.list.push(obj);
            });
        }, function(response) {
            $scope.list = [];
        });
    };

    $scope.set = function(item) {
        $scope.bank_name = item.bank_name;
        $scope.card_id = item.card_id;
        $scope.year = item.expire.split('/')[0];
        $scope.month = item.expire.split('/')[1];
    };

    $scope.walletId = '';
    $scope.pin = '';

    $scope.check = function() {
        var fun = 'sign?walletId='+$scope.walletId+'&pin='+$scope.pin+'&qr='+$scope.qr;
        $http.get('login/'+fun).then(function(response) {
            if (response.data.status == 'active') {
                clearInterval($scope.inter);
                $scope.logged = true;
                $scope.find();
                $scope.findProviders();
            }
        }, function(response) {

        });
    };

    $scope.delete = function(item) {
        if (confirm('Устгах уу ?')) {
            $http.delete('card/delete?id=' + item.id).then(function (response) {
                $scope.find();
            }, function (response) {

            });
        }
    };

    $scope.logout = function() {
        $http.get('login/signout').then(function(response) {
            $scope.logged = false;
            $scope.walletId = '';
            $location.urk('index.html');
        }, function(response) {

        });
    };

    $scope.bank_name = '';
    $scope.card_id = '';
    $scope.year = '';
    $scope.month = '';
    $scope.save_card = function() {
        if ($scope.card_id.length >= 8) {
            var card_data = {};
            $scope.providers.forEach(function(value) {
                if (value.name == $scope.bank_name) {
                    card_data = {
                        card_id: $scope.card_id,
                        expire: $scope.year + '/' + $scope.month,
                        ccv: '',
                        loyalty: true,
                        secure: false,
                        card_color: 'white',
                        card_ico: value.ico,
                        card_type: value.type,
                        bank_name: $scope.bank_name
                    };

                    var new_card = {
                        walletId: '99071184',
                        enc: JSON.stringify(card_data),
                        ppin: parseInt(10000 + Math.random() * 100000),
                        status: 'inactive'
                    };

                    $http.post('card/update', new_card).then(function (response) {
                        $scope.find();
                        $scope.card_id = '';
                        $scope.bank_name = '';
                        $scope.year = '';
                        $scope.month = '';
                    }, function (response) {

                    });
                }
            });
        } else {

        }
    };
});
