(ns blaagh.controllers
    (:require [selmer.parser :refer [render-file]]
              [blaagh.db.core :as db]))

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

(defn names-handler [request]
    (let [names (db/get-all-names)]
        (render-file "templates/names.html" {:names names})))

(defn show-post-handler [request]
    (let [slug (:slug (:params request)) 
          db-row (db/get-post-by-slug {:slug slug})
          post (first db-row)]
          (if (nil? post)
            (render-file "templates/404.html" {:slug slug})
            (render-file "templates/post.html" {:post post}))
        ))
(defn new-post [request]
    (let [anti-forgery-token (:ring.middleware.anti-forgery/anti-forgery-token (:session request))] 
        (render-file "templates/new-post.html" {:request request 
            :title "New Post" :anti-forgery-token anti-forgery-token})))

(defn new-post! [request]
    (let [anti-forgery-token (:ring.middleware.anti-forgery/anti-forgery-token (:session request))] 
        (render-file "templates/new-post.html" {:request request 
            :title "NEW POST" :anti-forgery-token anti-forgery-token})))
