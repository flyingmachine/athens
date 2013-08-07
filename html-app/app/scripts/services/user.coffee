'use strict';
angular.module("athensApp").factory "User", ["$resource", ($resource) ->
  $resource '/users/:id/:attribute', id: '@id',
    changePassword:
      method: 'POST'
      params: {attribute: 'password'}
]
