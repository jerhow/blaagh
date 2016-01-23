(ns blaagh.helpers
    (:require [clojure.string :refer [blank?]]))

(defn field-found? [request field-key]
    (contains? (:params request) field-key))

(defn validate-new-post 
    "This function should be the harness for any form validations we decide to do. 
     For the time being, I only need required-field validations."
    [request]
    (cond
        (blank? (:slug (:params request)))
            (hash-map :result false, :field "Slug",
                :msg (str "Required field Slug is blank"))
        (blank? (:title (:params request)))
            (hash-map :result false, :field "Title",
                :msg (str "Required field: Title is blank"))
        (blank? (:content (:params request)))
            (hash-map :result false, :field "Content",
                :msg (str "Required field: Content is blank"))
        :else
            (hash-map :result true, :msg "All required fields have values")))
