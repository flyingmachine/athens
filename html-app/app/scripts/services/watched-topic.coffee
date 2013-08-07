'use strict';
angular.module("athensApp").factory "WatchedTopic", ["$resource", ($resource) ->
  $resource '/watched-topics/:id', id: '@id'
]
