(ns blaagh.db.core
    (:require [clojure.java.jdbc :as sql]))

(def db-conn {
    :classname "org.sqlite.JDBC"
    :subprotocol "sqlite"
    :subname "resources/db/blaagh.sqlite3"}) ;; creates DB if not already present

(defn get-names []
    (let [results (sql/query db-conn ["select name from foo"])]
        (cond
            (empty? results) {:status 404}
            :else results)))

(defn create-tables []
    (try 
        (sql/db-do-commands db-conn 
            "CREATE TABLE foo (name text); "
            "CREATE TABLE bar (name text); "
            "CREATE TABLE baz (name text); ")
        (catch Exception e 
            (println e))))
