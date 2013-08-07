(ns athens.controllers.watches
  (:require [athens.db.validations :as validations]
            [datomic.api :as d]
            [athens.db.query :as db]
            [athens.db.maprules :as mr]
            [athens.db.transactions :as ts])
  (:use [liberator.core :only [defresource]]
        athens.controllers.shared
        athens.models.permissions
        athens.db.mapification
        athens.utils))

(defmapifier record
  mr/ent->watch)

(defresource query [params auth]
  :available-media-types ["application/json"]
  :handle-ok (fn [ctx]
               (map (comp record first)
                    (d/q '[:find ?watch
                           :in $ ?userid
                           :where [?watch :watch/user ?userid]
                           [?watch :watch/topic ?topic]
                           [?topic :content/deleted false]]
                         (db/db)
                         (:id auth)))))

(defresource create! [params auth]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :authorized? (logged-in? auth)

  :post! (create-record ts/create-watch params record)
  :handle-created record-in-ctx)

(defresource delete! [params auth]
  :allowed-methods [:delete]
  :available-media-types ["application/json"]
  :authorized? (fn [_]
                 (let [watch-id (str->int (:id params))
                       watch (db/ent watch-id)]
                   (if (and watch
                            (= (:db/id (:watch/user watch)) (:id auth)))
                     {:record {:id watch-id}})))
  :exists? exists-in-ctx?
  :delete! (fn [ctx]
             (db/retract-entity (get-in ctx [:record :id]))))
