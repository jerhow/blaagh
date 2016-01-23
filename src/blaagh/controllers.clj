(ns blaagh.controllers
    (:require [selmer.parser :refer [render-file]]
              [blaagh.db.core :as db]
              [blaagh.helpers :as helpers]
              [ring.util.response :refer [redirect]]))

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
            (render-file "templates/post.html" {:post post}))))

(defn process-new-post! [request]
    "Writes a new post to the DB, then redirects somewhere (for the moment, the home page)."
    (db/write-new-post! {
        :slug (:slug (:params request)) 
        :title (:title (:params request)) 
        :content (:content (:params request))})
    (redirect "/"))

(defn new-post [request]
    (let [anti-forgery-token (:ring.middleware.anti-forgery/anti-forgery-token (:session request))
          validation (helpers/validate-new-post request)]
        (cond
            (true? (:result validation))
                (process-new-post! request)
            :else
                (render-file "templates/new-post.html" {
                    :request request
                    :title "NEW POST"
                    :anti-forgery-token anti-forgery-token 
                    :validation validation}))))

(defn admin-show-posts [request]
    ; (let [posts (db/admin-get-posts {:sort "dt"})]
    (let [sort-column "dt" order "DESC"
          query (str "SELECT id, dt, live, slug, title, content FROM posts ORDER BY " sort-column " " order)
          posts (db/raw-fetch-query query)]
        (render-file "templates/admin-show-posts.html" {:request request :posts posts})))
