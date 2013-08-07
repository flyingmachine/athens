'use strict'

angular.module('athensApp')
  .controller 'ProfilePasswordCtrl', ($scope, User, CurrentSession) ->
    $scope.password = {}
    $scope.editing = false
    passwordFields = ['current-password', 'new-password', 'new-password-confirmation']
    
    User.get id: CurrentSession.get().id, (user)->
      $scope.user = user

    $scope.support.secondaryNav.show "profile/about-nav", {tab: "password"}

    validate = ->
      $scope.errors = {}
      _.each passwordFields, (field)->
        if !$scope.user[field]
          $scope.errors[field] = ["You must fill out this field"]
      if !_.isEmpty $scope.errors
        return false
      return true

    $scope.updatePassword = ->
      if !validate()
        return
      
      $scope.user.$changePassword((u)->
        $scope.successMessage = "Your password was updated successfully"
        $scope.user['current-password'] = ""
        $scope.user['new-password'] = ""
        $scope.user['new-password-confirmation'] = ""
      , (res)->
        $scope.errors = res.data.errors
      )
