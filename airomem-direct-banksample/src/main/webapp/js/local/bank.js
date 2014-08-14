angular.module('bank',['ngResource'])
        .factory('bankService' ,['$resource', function($resource) {
                  var bankHandle = $resource('webresources/bank/account/:id', 
                   {}, 
                   {register: { method: 'PUT', url:'webresources/bank/account'}} );
            return {
              
                register: function () {
                    var account = bankHandle.register();
                    return account;
                }
            
            };
        }]);
    
    
angular.module('bank').controller('bankCtrl', [ '$scope','bankService', 
        function($scope, bankService) {
        $scope.accounts = [];
        $scope.register = function () {
            $scope.accounts.push( bankService.register());
        };
} ]); 
            
