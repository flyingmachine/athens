(ns athens.config
  (:use environ.core))

(def conf
  (merge-with
   merge
   {:html-paths ["html-app"
                 "../html-app/app"
                 "../html-app/.tmp"]
    :datomic {:db-uri "datomic:free://localhost:4334/athens"
              :test-uri "datomic:mem://athens"}
    :moderator-names ["flyingmachine"]
    :gp-email {:from-address "notifications@athens.com"
               :from-name "Grateful Place Notifications"}}
   {:gp-email {:username (env :gp-email-username)
               :password (env :gp-email-password)}}))

(defn config
  [& keys]
  (get-in conf keys))
