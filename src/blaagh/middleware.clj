(ns blaagh.middleware)

(require 'clojure.pprint)

(defn wrap-version [handler]
    (fn [request]
        (handler (assoc request :app-version "0.1"))))

(defn wrap-spy [handler]
    "Output (to the server console) some pretty-printed representations 
    of the Request and Response maps."
    (fn [request]
        (println "-------------------------------")
        (println "Incoming Request Map:")
        (clojure.pprint/pprint request)
        (let [response (handler request)]
            (println "Outgoing Response Map:")
            (clojure.pprint/pprint response)
            (println "-------------------------------")
            response)))