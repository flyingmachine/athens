'use strict'

angular.module('athensApp', @App.modules)
  .config ($routeProvider)->
    $routeProvider
      .when '/',
        templateUrl: 'views/topics/index.html',
        controller: 'TopicsQueryCtrl'
      .when '/topics/:id',
        templateUrl: 'views/topics/show.html',
        controller: 'TopicsShowCtrl'
      .when '/posts',
        templateUrl: 'views/posts/index.html',
        controller: 'PostsQueryCtrl'
      .when '/login',
        templateUrl: 'views/login.html'
      .when '/profile/about',
        templateUrl: 'views/profile/about.html'
        controller: 'ProfileAboutCtrl'
      .when '/profile/password',
        templateUrl: 'views/profile/password.html'
        controller: 'ProfilePasswordCtrl'
      .when '/profile/email',
        templateUrl: 'views/profile/email.html'
        controller: 'ProfileEmailCtrl'
      .when '/watched-topics',
        templateUrl: 'views/watched-topics.html',
        controller: 'WatchedTopicsCtrl'
      .when '/welcome',
        templateUrl: 'views/welcome.html'
      .otherwise
        redirectTo: '/'
