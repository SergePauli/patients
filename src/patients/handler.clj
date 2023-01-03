(ns patients.handler
  (:use [compojure.core]        
        [patients.query]
        [metis.core]
        [ring.util.response :only [response]])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [cheshire.core :as converter]
            [ring.middleware.json :as json] 
            [java-time.api :as jt]))

(defn welcome   
  [request]
  {:status 200
   :body "<h1>API Patients started!</h1>
     <h3> Степень готовности: </h3>
     <p> На данный момент готовы CRUD роуты (/api/patients: GET,POST,PUT,DELETE) и простые валидаторы полей</p>"   
   :headers {"Content-Type" "text/html; charset=utf-8"}})


(defn nil-remover [patient] 
  ;удаляем пустые поля 
  (apply dissoc                                                                                            
       patient                                                                                                  
       (for [[k v] patient :when (nil? v)] k)))


(defn date-conversion [patient]
  ;конвертация даты рождения
  (if (nil? (:birth_date patient)) patient
      (assoc patient :birth_date (jt/local-date (:birth_date patient)))))


(defvalidator patient-validator
  ;валидация данных
  [[:fio :gender :birth_date] :presence {:only :creation}]
  [:fio :formatted {:pattern #"[а-яА-ЯёЁa-zA-Z]+( [а-яА-ЯёЁa-zA-Z]+){1,3}" :message "wrong formatting!" :if-not (fn [attrs] (= (:fio attrs) nil))}]  
  [:birth_date :formatted {:pattern #"^[1-2]\d{3}(-|\/)((0[1-9])|(1[0-2]))(-|\/)((0[1-9])|([1-2][0-9])|(3[0-1]))$" :message "wrong SQL-date! Use (YYYY-MM-DD)" :if-not (fn [attrs] (= (:birth_date attrs) nil))}]  
  [:gender :inclusion {:in [1 2] :if-not (fn [attrs] (= (:gender attrs) nil))}] 
  [:address :length {:greater-than 5 :if-not (fn [attrs] (= (:address attrs) nil))}]
  [:polis_oms :length {:less-than 18 :greater-than 11 :if-not (fn [attrs] (= (:polis_oms attrs) nil))} ])


(extend-protocol cheshire.generate/JSONable
  ;правильное отображение sql.Timestamp
  java.sql.Timestamp
  (to-json [dt gen]
    (cheshire.generate/write-string gen (str dt))))


(defn error-response [status errors]
  ;формируем ответ в случае ошибки 
  {:status status :body (converter/generate-string errors) :headers {"Content-Type" "application/json; charset=utf-8"}}
  )

(defn save-patient  
  ; Сохранение изменений
  [patient id]
  ( 
   let [
        context (if id  :update  :creation)
  ; Проверить данные на валидность         
        validate-patient (patient-validator patient context) 
  ; Обработать ошибки
        errors (first validate-patient)        
        ]
  
    (if-not errors
      ; Выполняем запрос к СУБД и возвращаем результат
      (response  (if id (update-patient id (date-conversion (nil-remover patient)))          
        (add-patient (date-conversion (nil-remover patient)))))
      ; Иначе возвращаем ошибку валидации
      (error-response 422 errors) ))
  )


(defroutes app-routes   
  (GET "/" [] welcome)  
  (GET "/api/patients" [] (response 
                           (try (get-patients) 
                                (catch Exception e (error-response 400 (.toString e))))))
  (GET "/api/patients/:id" [id] (try (response (get-patient (Integer/parseInt id))) 
                                     (catch Exception e (error-response 400 (.toString e)))))
  (POST "/api/patients" [fio gender birth_date address polis_oms] 
    (try (save-patient (->Patient fio gender birth_date address polis_oms) nil) 
         (catch Exception e (error-response 422 (.toString e)))))
  (PUT "/api/patients/:id" [id fio gender birth_date address polis_oms] 
    (try (save-patient (->Patient fio gender birth_date address polis_oms) (Integer/parseInt id)) 
         (catch Exception e (error-response 422 (.toString e)))))
  (DELETE "/api/patients/:id" [id] 
    (try (response (delete-patient (Integer/parseInt id))) 
                   (catch Exception e (error-response 400 (.toString e)))))
  (route/not-found "Not found"))

(def app
  (-> (handler/api app-routes)
      (json/wrap-json-params)
      (json/wrap-json-response {:date-format "yyyy-MM-dd"})))
