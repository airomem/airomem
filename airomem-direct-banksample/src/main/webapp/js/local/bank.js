angular.module('bank', ['ngResource'])
        .factory('bankService', ['$resource', function($resource) {
                var bankHandle = $resource('webresources/bank/account/:id',
                        {id: '@id'},
                        {
                            register: {method: 'PUT', url: 'webresources/bank/account'},
                            show: {method: 'GET'},
                            deposit: {method: 'POST'},
                            withdraw: {method: 'POST'},
                        });
                return {
                    register: function() {
                        var account = bankHandle.register();
                        return account;
                    },
                    
                    deposit: function(accountId, value) {
                        var account = bankHandle.deposit(   {id: accountId}, value);
                        return account;
                    },
                       
                    show: function(accountId) {
                        var account = bankHandle.show( {id: accountId});
                        return account;
                    }

                };
            }]);


angular.module('bank').controller('bankCtrl', ['$scope', 'bankService',
    function($scope, bankService) {
        $scope.accounts = [];
        $scope.selectedId =  null;
        
        $scope.value = '100';
        
        $scope.selectedAccount = null;
        
        $scope.register = function() {
            $scope.accounts.push(bankService.register());
        };
        $scope.select = function(param) {
            $scope.selectedId = param;
            $scope.selectedAccount = bankService.show($scope.selectedId);
        };
        
        $scope.selected = function(param) {
            return param === $scope.selectedId;
        };
        
        $scope.deposit = function() {
             $scope.selectedAccount = bankService.deposit($scope.selectedId, $scope.value);
        };
        
    }]);

