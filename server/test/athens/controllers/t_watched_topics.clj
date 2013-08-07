(ns athens.controllers.t-watched-topics
  (:require [athens.db.test :as tdb]
            [athens.db.query :as q]
            [athens.db.manage :as db-manage]
            [athens.controllers.watched-topics :as watched-topics])
  (:use midje.sweet
        athens.paths
        athens.controllers.test-helpers))

(setup-db-background)

(fact "query returns all watched topics"
  (response-data :get "/watched-topics" {} (auth))
  => (one-of map?))
