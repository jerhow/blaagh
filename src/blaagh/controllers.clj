(ns blaagh.controllers
    (:require [selmer.parser :refer [render-file]]
              [blaagh.db.core :as db]
              [blaagh.helpers :as helpers]
              [ring.util.response :refer [redirect]]
              [clojure.string :refer [blank?]]))

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

(defn process-new-post! 
    "Writes a new post to the DB, then redirects to /admin/posts"
    [request]
    (db/write-new-post! {
        :live (:live (:params request)) 
        :slug (:slug (:params request)) 
        :title (:title (:params request)) 
        :content (:content (:params request))})
    (redirect "/admin/posts"))

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
    (let [sort-column (if (blank? (:sort (:params request))) "dt" (:sort (:params request)))
          order (if (blank? (:order (:params request))) "DESC" (:order (:params request)))
          query (str "SELECT id, dt, live, slug, title, 
                          CASE WHEN length(content) > 50 THEN
                            substr(content, 0, 50) || '...'
                          ELSE
                            content
                          END AS content
                      FROM posts ORDER BY " sort-column " " order)
          posts (db/raw-fetch-query query)]
        (render-file "templates/admin-show-posts.html" {:request request :posts posts})))

(defn edit-post-handler [request]
    (let [id (:id (:params request)) 
          db-row (db/get-post-by-id {:id id})
          post (first db-row)
          anti-forgery-token (:ring.middleware.anti-forgery/anti-forgery-token (:session request))]
          (if (nil? post)
            (render-file "templates/404.html" {:id id})
            (render-file "templates/new-post.html" {:edit-mode true 
                :post post :request request :anti-forgery-token anti-forgery-token}))))

(defn edit-post-update! 
    "Updates an existing post in the DB, then redirects to /admin/posts"
    [request]
    (db/update-post! {
        :id (:post_id (:params request))
        :live (:live (:params request)) 
        :slug (:slug (:params request)) 
        :title (:title (:params request)) 
        :content (:content (:params request))})
    (redirect "/admin/posts"))
