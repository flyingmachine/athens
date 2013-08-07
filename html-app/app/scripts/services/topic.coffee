'use strict';
angular.module("athensApp").factory "Topic", ["$resource", ($resource) ->
  service = $resource '/topics/:id', id: '@id'
]
