(ns athens.middleware.routes
  (:require compojure.route
            compojure.handler
            [ring.util.response :as resp]
            [athens.controllers.topics :as topics]
            [athens.controllers.watches :as watches]
            [athens.controllers.watched-topics :as watched-topics]
            [athens.controllers.posts :as posts]
            [athens.controllers.likes :as likes]
            [athens.controllers.users :as users]
            [athens.controllers.session :as session]
            [athens.controllers.js :as js]
            [cemerick.friend :as friend])
  (:use [compojure.core :as compojure.core :only (GET PUT POST DELETE ANY defroutes)]
        athens.config))


(defmacro route
  [method path handler]
  `(~method ~path {params# :params}
            (~handler params#)))

(defmacro authroute
  [method path handler]
  (let [params (quote params)]
    `(~method ~path {:keys [~params] :as req#}
              (~handler ~params (friend/current-authentication req#)))))

(defroutes routes
  (authroute GET "/scripts/load-session.js" js/load-session)

  ;; Serve up angular app
  (apply compojure.core/routes
         (map #(compojure.core/routes
                (compojure.route/files "/" {:root %})
                (compojure.route/resources "/" {:root %}))
              (reverse (config :html-paths))))
  (apply compojure.core/routes
         (map (fn [response-fn]
                (GET "/" [] (response-fn "index.html" {:root "html-app"})))
              [resp/file-response resp/resource-response]))
  
  ;; Topics
  (route GET "/topics" topics/query)
  (authroute GET "/topics/:id" topics/show)
  (authroute POST "/topics" topics/create!)
  (authroute DELETE "/topics/:id" topics/delete!)

  ;; Watches
  (authroute GET "/watches" watches/query)
  (authroute POST "/watches" watches/create!)
  (authroute DELETE "/watches/:id" watches/delete!)

  (authroute GET "/watched-topics" watched-topics/query)
  
  ;; Posts
  (route GET "/posts" posts/query)
  (authroute POST "/posts" posts/create!)
  (authroute PUT  "/posts/:id" posts/update!)
  (authroute POST "/posts/:id" posts/update!)
  (authroute DELETE "/posts/:id" posts/delete!)

  ;; Likes
  (authroute POST "/likes/:post-id" likes/create!)
  (authroute DELETE "/likes/:post-id" likes/delete!)

  ;; Users
  (authroute POST "/users" users/registration-success-response)
  (route GET "/users/:id" users/show)
  (authroute POST "/users/:id" users/update!)
  (authroute POST "/users/:id/password" users/change-password!)

  ;; auth
  (route POST "/login" session/create!)
  (friend/logout
   (ANY "/logout" []
        (ring.util.response/redirect "/")))

  (ANY "/debug" {:keys [x] :as r}
       (str x))
  
  (compojure.route/not-found "Sorry, there's nothing here."))
