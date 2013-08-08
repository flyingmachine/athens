'use strict'

angular.module('athensApp').controller 'PostsQueryCtrl', ($scope, $location, Post) ->
  Post.query {q: $location.search().q || ""}, (data)->
    $scope.posts = data