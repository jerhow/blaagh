(ns blaagh.core
  (:require [compojure.core :refer :all]
            [org.httpkit.server :refer [run-server]]
            [ring.middleware.reload :as reload]
            [ring.middleware.defaults :refer :all]))

(require 'clojure.pprint)

(defn home-handler
    [request]
    (str "Blaagh version " (:app-version request)))

(defn wrap-version [handler]
    (fn [request]
        (handler (assoc request :app-version "0.1"))))

(defn wrap-spy [handler]
    ""
    (fn [request]
        (println "-------------------------------")
        (println "Incoming Request Map:")
        (clojure.pprint/pprint request)
        (let [response (handler request)]
            (println "Outgoing Response Map:")
            (clojure.pprint/pprint response)
            (println "-------------------------------")
            response)))

(defroutes all-routes
    (GET "/" [request] home-handler)
    ; (GET "/" [] "Show something")
    (POST "/" [] "Create something")
    (PUT "/" [] "Replace something")
    (PATCH "/" [] "Modify Something")
    (DELETE "/" [] "Annihilate something")
    (OPTIONS "/" [] "Appease something")
    (HEAD "/" [] "Preview something"))

(defn -main []
    (run-server 
        (reload/wrap-reload 
            (wrap-spy 
                (wrap-version #'all-routes))) {:port 5000}))
                    ; (wrap-defaults #'all-routes site-defaults)))) {:port 5000}))
    ; (run-server (reload/wrap-reload #'all-routes) {:port 5000}))
