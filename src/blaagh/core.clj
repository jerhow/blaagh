(ns blaagh.core
  (:require [blaagh.middleware :as middleware]
            [blaagh.controllers :as controllers]
            [compojure.core :refer :all]
            [org.httpkit.server :refer [run-server]]
            [ring.middleware.reload :as reload]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.params :refer :all]
            [selmer.parser :refer [render-file]]))

(selmer.parser/cache-off!) ; template caching switch

(defroutes all-routes
    (GET "/" [request] controllers/home-handler)
    ; (GET "/selmer" [] (render-file "templates/selmer.html" {:name "Jerry"}))
    (GET "/selmer/:name" [request] controllers/selmer-handler)
    (GET "/post-something" [request] controllers/post-something-handler-GET)
    (POST "/post-something" [request] controllers/post-something-handler-POST)
    (GET "/names" [request] controllers/names-handler)
    ; (GET "/" [] "Show something")
    ; (POST "/" [] "Create something")
    ; (PUT "/" [] "Replace something")
    ; (PATCH "/" [] "Modify Something")
    ; (DELETE "/" [] "Annihilate something")
    ; (OPTIONS "/" [] "Appease something")
    ; (HEAD "/" [] "Preview something")
    (GET "/admin/post/new" [request] controllers/new-post)
    (POST "/admin/post/new" [request] controllers/new-post)
    (GET "/admin/posts" [request] controllers/admin-show-posts)
    (GET "/:slug" [request] controllers/show-post-handler)
    ; (POST "/post/new" [request] controllers/write-new-post-handler)
)

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
