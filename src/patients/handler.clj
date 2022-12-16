(ns patients.handler
  (:use [compojure.core]        
        [patients.query]
        [ring.util.response :only [response]])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as json]
            
            [clj-time.format :as f]))
(defroutes default-routes     
  (route/not-found "Not found")
  )
(defn welcome  
  [request]
  {:status 200
     :body "<h1>API Patients started!</h1>
     <p>Welcome to my first Clojure app, I now update automatically</p>"
     
   :headers {}})

(defroutes app-routes   
  (GET "/" [] welcome)
  (GET "/api/patients" [] (response (get-patients)))
  (GET "/api/patients/:id" [id] (response (get-patient (Integer/parseInt id))))
  (POST "/api/patients" [fio gender birth_date address polis_oms] (response (add-patient fio (Integer/parseInt gender)  birth_date address polis_oms)))
  (PUT "/api/patients/:id" [id fio gender birth_date address polis_oms] (response (update-patient (Integer/parseInt id) fio (Integer/parseInt gender) birth_date address polis_oms)))
  (DELETE "/api/patients/:id" [id] (response (delete-patient (Integer/parseInt id)))))

(def app
  (-> (handler/api app-routes)
      (json/wrap-json-params)
      (json/wrap-json-response)))
