(ns blaagh.db.core
    (:require [clojure.java.jdbc :as sql])
    (:require [yesql.core :refer [defqueries]]))

(def db-spec {
    :classname "org.sqlite.JDBC"
    :subprotocol "sqlite"
    :subname "resources/db/blaagh.sqlite3"}) ;; creates DB if not already present

(defqueries "blaagh/db/queries.sql"
   {:connection db-spec})

(defn get-all-names []
    (try
        (let [results (get-names)]
            (cond
                (empty? results) {:status 404}
                :else results))
    (catch Exception e 
        (println e))))

(defn create-tables []
    (create-table-foo!)
    (create-table-bar!)
    (create-table-baz!)
    (create-table-posts!))

(defn populate-tables []
    (populate-table-foo!)
    (populate-table-posts1!)
    (populate-table-posts2!))
