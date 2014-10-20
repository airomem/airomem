angular.module('bank', ['ngResource', 'LocalStorageModule'])
        .factory('bankService', ['$resource', 'localStorageService', function ($resource, localStorageService) {
                var bankHandle = $resource('webresources/bank/account/:id',
                        {id: '@id'},
                {
                    register: {method: 'POST', url: 'webresources/bank/account'},
                    show: {method: 'GET'},
                    deposit: {method: 'PUT'},
                    withdraw: {method: 'PUT'}
                });


                function keepVal(account) {
                    var accounts = localStorageService.get('accounts');
                    if (!accounts) {
                        accounts = {};
                    }
                    accounts[account.id] = account;
                    localStorageService.set('accounts', accounts);
                }

                function getVal() {
                    var accounts = localStorageService.get('accounts');
                    if (!accounts) {
                        accounts = {};
                    }
                    return accounts;
                }


                return {
                    register: function () {
                        var account = bankHandle.register();
                        return account;
                    },
                    deposit: function (accountId, value) {
                        var account = bankHandle.deposit({id: accountId}, value);
                        account.$promise.then(function () {
                            keepVal(account);
                        });
                        return account;
                    },
                    withdraw: function (accountId, value) {
                        var account = bankHandle.deposit({id: accountId}, -value);
                        account.$promise.then(function () {
                            keepVal(account);
                        });
                        return account;
                    },
                    show: function (accountId) {
                        var account = bankHandle.show({id: accountId});
                        account.$promise.then(function () {
                            keepVal(account);
                        });
                        return account;
                    },
                    getStored: function () {
                        var accounts = getVal();
                        return accounts;
                    },
                    clearStory: function () {
                        localStorageService.clearAll();
                    }
                };
            }]);


angular.module('bank').controller('bankCtrl', ['$scope', 'bankService','$timeout',
    function ($scope, bankService,$timeout) {
        $scope.accounts = [];
        $scope.selectedId = null;

        $scope.value = '100';

        $scope.selectedAccount = null;

        $scope.storedAccounts = bankService.getStored();

        function restore() {
            $timeout(function () {
                $scope.storedAccounts = bankService.getStored();
            }, 1000);
        }

        $scope.register = function () {
            $scope.accounts.push(bankService.register());
        };

        $scope.status = function () {
            $scope.selectedAccount = bankService.show($scope.selectedId);

        }
        ;

        $scope.select = function (param) {
            $scope.selectedId = param;
            $scope.selectedAccount = bankService.show($scope.selectedId);
            restore();
        };

        $scope.selected = function (param) {
            return param === $scope.selectedId;
        };

        $scope.deposit = function () {
            $scope.selectedAccount = bankService.deposit($scope.selectedId, $scope.value);
            restore();
        };

        $scope.withdraw = function () {
            $scope.selectedAccount = bankService.withdraw($scope.selectedId, $scope.value);
            restore();
        };

        $scope.clear = function () {
            bankService.clearStory();
            restore();
        };

    }]);

