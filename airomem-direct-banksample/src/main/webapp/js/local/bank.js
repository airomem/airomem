angular.module('bank', ['ngResource'])
        .factory('bankService', ['$resource', function($resource) {
                var bankHandle = $resource('webresources/bank/account/:id',
                        {},
                        {
                            register: {method: 'PUT', url: 'webresources/bank/account'},
                            deposit: {method: 'POST'},
                            withdraw: {method: 'POST'},
                        });
                return {
                    register: function() {
                        var account = bankHandle.register();
                        return account;
                    }

                };
            }]);


angular.module('bank').controller('bankCtrl', ['$scope', 'bankService',
    function($scope, bankService) {
        $scope.accounts = [];
        $scope.selectedId =  null;
        $scope.register = function() {
            $scope.accounts.push(bankService.register());
        };
        $scope.select = function(param) {
            $scope.selectedId = param;
        };
        
        $scope.selected = function(param) {
            return param === $scope.selectedId;
        };
        
        $scope.deposit = function() {
            bankService.deposit({id:$scope.selectedId});
        };
        
    }]);

