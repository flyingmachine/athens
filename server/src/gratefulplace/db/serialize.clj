(ns gratefulplace.db.serialize
  (:require [gratefulplace.db.query :as db]))

(declare topic post user serialize)

(defn attr
  [name retriever]
  {name (eval retriever)})


(defn has
  [name arity directives]
  {name (apply assoc {:arity arity} directives)})

(defn has-one
  [name & directives]
  (has name :one directives))

(defn has-many
  [name & directives]
  (has name :many directives))

(defn ref-count
  [ref-attr]
  #(ffirst (db/q [:find '(count ?c) :where ['?c ref-attr (:db/id %)]])))

(defmacro defserializer
  [name & fields]
  `(def ~name
     (let [seed# {:attributes {} :relationships {}}]
       (reduce (fn [result# [fun# & args#]]
                 (let [tomerge# (apply (resolve fun#) args#)
                       destination# (if (= "attr" (str fun#)) :attributes :relationships)]
                   (update-in result# [destination#] merge tomerge#)))
               seed#
               (quote [~@fields])))))


(defserializer topic
  (attr :id :db/id)
  (attr :title :topic/title)
  (attr :post-count (ref-count :post/topic))
  (attr :author-id :content/author)
  (has-one :first-post :serializer post :retriever :topic/first-post)
  (has-one :author :serializer user :retriever :content/author))

(defserializer post
  (attr :id :db/id)
  (attr :content :post/content)
  (attr :topic-id :post/topic)
  (attr :author-id :content/author)
  (has-one :author :serializer user :retriever :content/author)
  (has-one :topic :serializer topic :retriever :post/topic))

(defserializer user
  (attr :id :db/id)
  (attr :username :user/username)
  (attr :email :user/email)
  (has-many :topics :serializer topic :retriever #(db/all :topic/title [:content/author (:db/id %)]))
  (has-many :posts :serializer post :retriever #(db/all :post/content [:content/author (:db/id %)])))

(defn apply-options-to-attributes
  [attributes options]
  (apply dissoc attributes (:exclude options)))

(defn serialize-attributes
  [entity attributes options]
  (reduce (fn [acc [attr-name retrieval-fn]]
            (conj acc [attr-name (retrieval-fn entity)]))
          {}
          (apply-options-to-attributes
           attributes
           options)))

(defn apply-options-to-relationships
  [relationships options]
  (let [include (:include options)]
    (cond
     (keyword? include) (select-keys relationships [include])
     (empty? include) {}
     (map? include) (reduce merge {} (map (fn [[k o]]
                                            {k (merge (k relationships) {:options o})})
                                          include))
     :else (select-keys relationships include))))



(defn serialize-relationship
  [entity directives]
  (let [serialize-retrieved #(serialize
                              %
                              (:serializer directives)
                              (:options directives))
        retrieved ((:retriever directives) entity)]
    (println "DIRECTIVES" directives)
    (println "RETRIEVED" retrieved)
    (cond
     (= :one (:arity directives)) (serialize-retrieved retrieved)
     (= :many (:arity directives)) (map serialize-retrieved retrieved))))

(defn serialize-relationships
  [entity relationships options]
  (reduce (fn [acc [attr-name serialization-directives]]
            {attr-name (serialize-relationship entity serialization-directives)})
          {}
          (apply-options-to-relationships
           relationships
           options)))

(defn serialize
  ;; Given a map of transformations, apply them such that a map is
  ;; returned where the keys of the return and the transformations are
  ;; the same, and the return values are derived by applying the
  ;; values of the transformation map to the supplied entity
  ([entity serializer]
     (serialize serializer entity {}))
  ([entity serializer options]
     (merge
      (serialize-attributes entity (:attributes serializer) options)
      (serialize-relationships entity (:relationships serializer) options))))


(serialize
 {:topic/title "test" :db/id 1 :topic/first-post {:db/id 1 :post/content "content"}} 
 topic
 {:exclude [:id]
  :include {:first-post {:include :topic}}})