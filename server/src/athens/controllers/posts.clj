(ns athens.controllers.posts
  (:require [datomic.api :as d]
            [athens.db.validations :as validations]
            [athens.db.query :as db]
            [athens.db.transactions :as ts]
            [athens.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c])
  (:use [flyingmachine.webutils.validation :only (if-valid)]
        [liberator.core :only [defresource]]
        athens.controllers.shared
        athens.models.permissions
        athens.db.mapification
        athens.utils))

(defmapifier record
  mr/ent->post
  {:include author-inclusion-options
   ;; :exclude [:content :created-at :topic-id]
   })

(defresource update! [params auth]
  :allowed-methods [:put :post]
  :available-media-types ["application/json"]

  :malformed? (validator params (:update validations/post))
  :handle-malformed errors-in-ctx

  :authorized? (can-update-record? (record (id)) auth)
  :exists? record-in-ctx
  :put! (ts/update-post params)
  :post! (ts/update-post params)
  :new? false
  :respond-with-entity? true
  :handle-ok (fn [_] (record (id))))

(defresource create! [params auth]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :authorized? (logged-in? auth)

  :malformed? (validator params (:create validations/post))
  :handle-malformed errors-in-ctx

  :post! (create-content ts/create-post params auth record)
  :handle-created record-in-ctx)

(defresource delete! [params auth]
  :allowed-methods [:delete]
  :available-media-types ["application/json"]
  :authorized? (can-delete-record? (record (id)) auth)
  :exists? exists-in-ctx?
  :delete! delete-record-in-ctx)
