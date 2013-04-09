(function(){"use strict";angular.module("gratefulplaceApp",[]).config(["$routeProvider",function(t){return t.when("/",{templateUrl:"views/topics/index.html",controller:"TopicsCtrl"}).when("/topics/:id",{templateUrl:"views/topics/show.html",controller:"TopicCtrl"}).otherwise({redirectTo:"/"})}]).directive("peekable",["$rootScope",function(){return{restrict:"A",link:function(){}}}])}).call(this),function(){"use strict";angular.module("gratefulplaceApp").controller("PeekCtrl",["$scope","$rootScope","$routeParams","Peek",function(t,e){return e.$on("peek.new")(function(e,r){return t.peek=r})}])}.call(this),function(){"use strict";angular.module("gratefulplaceApp").controller("TopicCtrl",["$scope","$routeParams","Topics",function(t,e,r){return r.find(e.id).then(function(e){return console.log(e),t.topic=e})}])}.call(this),function(){"use strict";angular.module("gratefulplaceApp").controller("TopicsCtrl",["$scope","Topics",function(t,e){return e.all().then(function(e){return t.topics=e}),t.firstPost=function(t){return t.posts[0]}}])}.call(this),function(){"use strict";angular.module("gratefulplaceApp").factory("Peek",["$http","$q",function(){return{one:"two"}}])}.call(this),function(){"use strict";angular.module("gratefulplaceApp").factory("Topics",["$http","$q",function(t,e){return{all:function(){var r;return r=e.defer(),t.get("data/topics.json").then(function(t){return r.resolve(t.data)}),r.promise},find:function(t){var r;return r=e.defer(),this.all().then(function(e){return r.resolve(_.find(e,function(e){return parseInt(e.id)===parseInt(t)}))}),r.promise}}}])}.call(this),function(){"use strict";angular.module("gratefulplaceApp").factory("Users",["$http","$q",function(t,e){return{all:function(){var r;return r=e.defer(),t.get("data/users.json").then(function(t){return r.resolve(t.data)}),r.promise},find:function(t){var r;return r=e.defer(),this.all().then(function(e){return r.resolve(_.find(e,function(e){return parseInt(e.id)===parseInt(t)}))}),r.promise}}}])}.call(this);