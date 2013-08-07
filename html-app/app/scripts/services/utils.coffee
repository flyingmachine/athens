'use strict'
angular.module("athensApp").factory "Utils", ()->
  addWatchCountToTopics: (topics, watches)->
    if topics && watches
      _.each watches, (watch)->
        _.each topics, (topic)->
          if watch['topic-id'] == topic.id && watch['unread-count']
            topic['unread-count'] = watch['unread-count']
            false
