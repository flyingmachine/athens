'use strict';
angular.module("athensApp").factory "Post", ["$resource", ($resource) ->
  $resource '/posts/:id', id: '@id'
]
