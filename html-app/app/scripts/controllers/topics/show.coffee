'use strict'

angular.module('athensApp')
  .controller 'TopicsShowCtrl', ($rootScope, $scope, $routeParams, Topic, Post, User) ->
    Topic.get id: $routeParams.id, (topic)->
      $rootScope.$broadcast "view.topic", topic
      $scope.topic = topic
      $scope.firstPost = $scope.topic.posts.shift()
      $scope.support.secondaryNav.show "topics/watches", $scope.topic
        
    $scope.submitReply = ->
      post = new Post($scope.reply)
      post['topic-id'] = $scope.topic.id
      post.$save((p)->
        $scope.topic.posts.push(p)
      , (res)->
        $scope.errors = res.data.errors
      )
      $scope.reply = {}
