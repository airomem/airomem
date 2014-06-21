var chat = angular.module('chat', ['ngAnimate', 'ngResource']).config(function($locationProvider) {
    $locationProvider.html5Mode(true).hashPrefix('!');
});

chat.controller('chatBoard',['$scope','$http',function ($scope,$http) {
    $scope.messages = [];
    
    function update () {
        $http.get('webresources/chat').success(function(data){
            $scope.messages = data;
        });
        

    }
    update();
}]);