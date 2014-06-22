var chat = angular.module('chat', ['ngAnimate', 'ngResource']).config(function($locationProvider) {
    $locationProvider.html5Mode(true).hashPrefix('!');
});

chat.controller('chatBoard', ['$scope', '$http', '$sce', function($scope, $http, $sce) {
        $scope.messages = [];

        $scope.nick = '';
        $scope.content = "";

        $scope.send = function() {
            $http.put('webresources/chat', {nick: $scope.nick, content: $scope.content}).success(function(data) {
                update();
            });
        };


        function update() {
            $http.get('webresources/chat').success(function(data) {
                $scope.messages = data;

                var ul = jQuery("ul.message");
                ul.empty();

                for (var i = 0; i < $scope.messages.length; i++) {
                    try {
                        var msg = $scope.messages[i];
                        /* this part is rewritten fron angluar ot jquery in order to introduce security problem
                        *  to temonstrate CSP                          
                         */
                        /*$scope.messages[i].insecureContent =  $sce.trustAsHtml($scope.messages[i].content);
                         $scope.messages[i].insecureJsContent =  $sce.trustAsJs($scope.messages[i].content);*/
                        var li = jQuery("<li></li>");
                        li.append(jQuery("<span class='author'>" + msg.authorView.nickName + "</span>"));
                        li.append(jQuery("<span class='time'>" + msg.time + "</span>"));
                        li.append(jQuery("<span class='content'>" + msg.content + "</span>"));
                        ul.append(li);
                    } catch (e) {
                        ul.append("<li class='blocked'>"+e+"</li>");
                        console.log(e);
                    }
                }
                setTimeout(function() {
                    jQuery("ul.message").scrollTo("li:last-child");
                }, 500);

            });


        }
        update();
    }]);