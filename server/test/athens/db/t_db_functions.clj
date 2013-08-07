(ns athens.db.t-db-functions
  (:require [athens.db.test :as tdb]
            [athens.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c]
            [athens.db.query :as q])
  (:use midje.sweet
        athens.controllers.test-helpers))

(setup-db-background)

(defn watch
  []
  (q/one [:watch/topic]))

(fact "increment-register"
  (q/t [[:increment-watch-count (-> (watch) :watch/topic :db/id) 1]])
  (:watch/unread-count (watch))
  => 1)
