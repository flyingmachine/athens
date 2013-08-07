'use strict';
angular.module("athensApp").factory "Like", ["$resource", ($resource) ->
  service = $resource '/likes/:id', id: '@id'
]
