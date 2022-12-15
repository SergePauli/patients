(ns patients.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]            
            [ring.middleware.json :as json]))

(defroutes app-routes   
  (GET "/api/patients" [] "TODO:  return all list items")
  (GET "/api/patients/:id" [id] "TODO:  return a single list item")
  (POST "/api/patients" [] "TODO:  create a list item")
  (PUT "/api/patients/:id" [id] "TODO:  update a list item")
  (DELETE "/api/patients/:id" [id] "TODO:  delete a list item") 
  (route/resources "/index")
  (route/not-found "Not found"))

(def app
  (-> (handler/api app-routes)
      (json/wrap-json-params)
      (json/wrap-json-response)))


