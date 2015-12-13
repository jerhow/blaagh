(ns blaagh.db.core
    (:require [clojure.java.jdbc :as sql]))

(def db-conn {
    :classname "org.sqlite.JDBC"
    :subprotocol "sqlite"
    :subname "resources/db/blaagh.sqlite3"})

(defn get-names []
    (let [results (sql/query db-conn ["select name from foo"])]
        (cond
            (empty? results) {:status 404}
            :else results)))
