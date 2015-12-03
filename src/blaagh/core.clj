(ns blaagh.core
  (:require [blaagh.middleware :as middleware]
            [compojure.core :refer :all]
            [org.httpkit.server :refer [run-server]]
            [ring.middleware.reload :as reload]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.params :refer :all]
            [selmer.parser :refer [render-file]]))

(selmer.parser/cache-off!) ; template caching switch

(defn home-handler [request]
    (str "Blaagh version " (:app-version request) " foo=" (:foo (:params request))))

(defn selmer-handler [request]
    (let [name (:name (:params request))]
        (render-file "templates/selmer.html" {:name name :req request})))

(defn post-something-handler-GET [request]
    (let [anti-forgery-token (:ring.middleware.anti-forgery/anti-forgery-token (:session request))] 
        (render-file "templates/post-something.html" {:anti-forgery-token anti-forgery-token})))

(defn post-something-handler-POST [request]
    (let [name (:name (:params request)) 
          comment (:comment (:params request))]
    (render-file "templates/post-something.html" {:name name :comment comment})))

(defroutes all-routes
    (GET "/" [request] home-handler)
    ; (GET "/selmer" [] (render-file "templates/selmer.html" {:name "Jerry"}))
    (GET "/selmer/:name" [request] selmer-handler)
    (GET "/post-something" [request] post-something-handler-GET)
    (POST "/post-something" [request] post-something-handler-POST)
    ; (GET "/" [] "Show something")
    (POST "/" [] "Create something")
    (PUT "/" [] "Replace something")
    (PATCH "/" [] "Modify Something")
    (DELETE "/" [] "Annihilate something")
    (OPTIONS "/" [] "Appease something")
    (HEAD "/" [] "Preview something"))

(defn -main []
    "This is the idiomatic way of applying the various middleware pieces,
     using the -> macro. It works, and all of the middleware still functions correctly,
     however I still must get a better understanding of how this works. I basically
     just lined the middlewares from inside-to-out based on the original -main implementation."
    (run-server
        (-> all-routes
            (wrap-defaults site-defaults)
            (wrap-params)
            (middleware/wrap-version)
            ; (middleware/wrap-spy)
            (reload/wrap-reload)
            ) {:port 5000}))

(defn -main-ORIGINAL []
    "This is the non-idiomatic way of applying the various middleware pieces.
     It works, but the nesting gets a little crazy."
    (run-server 
        (reload/wrap-reload 
            (middleware/wrap-spy 
                (middleware/wrap-version
                    (wrap-defaults #'all-routes site-defaults)))) {:port 5000}))
    ; (run-server (reload/wrap-reload #'all-routes) {:port 5000}))
