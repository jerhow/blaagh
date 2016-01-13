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
    (try 
        (sql/db-do-commands db-spec 
            "CREATE TABLE foo (name text); "
            "CREATE TABLE bar (name text); "
            "CREATE TABLE baz (name text); ")
        (catch Exception e 
            (println e))))

(defn create-tables-yesql []
    (create-table-foo!)
    (create-table-bar!)
    (create-table-baz!))

(defn populate-tables-yesql []
    (populate-table-foo!))

(defn populate-tables-DEPRECATED []
    (try 
        (sql/db-do-commands db-spec 
            "INSERT INTO foo (name) VALUES
                ('Rick'),
                ('Carol'),
                ('Daryl'),
                ('Michonne'),
                ('Glenn'),
                ('Maggie'),
                ('Carl'),
                ('Sasha'),
                ('Abraham'),
                ('Morgan');")
        (catch Exception e 
            (println e))))
