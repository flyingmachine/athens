'use strict';
angular.module("athensApp").factory "Watch", ["$resource", ($resource) ->
  $resource '/watches/:id', id: '@id'
]
