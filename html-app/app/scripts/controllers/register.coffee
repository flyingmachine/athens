'use strict'

angular.module('athensApp')
  .controller 'RegisterCtrl', ($scope, $location, CurrentSession, User) ->
    $scope.errors = {}
    
    $scope.registration =
      username: ""
      password: ""
      email: ""
      'receive-watch-notifications': true

    registrationSuccess = (response)->
      response.newRegistration = true
      CurrentSession.set(response)
      $location.path "/"

    registrationFailure = (response)->
      $scope.errors = response.data.errors
      
    $scope.submitRegistration = ->
      user = new User($scope.registration)
      user.$save([], registrationSuccess, registrationFailure)
      
