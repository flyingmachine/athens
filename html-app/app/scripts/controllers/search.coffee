'use strict'

angular.module('athensApp').controller 'SearchCtrl', ($scope, $location)->
  $scope.search = ->
    if $scope.q
      $location.path("/posts").search(q: $scope.q)
  $scope.q = ""