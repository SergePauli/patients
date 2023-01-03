(ns patients.query
  (:require [patients.database]            
            [korma.core :refer :all]
            [java-time.api :as jt]))

; Определяем структуру записи пациента
(defrecord Patient [fio gender birth_date address polis_oms])

(defentity patients)

(defn get-patients []
  (select patients))

(defn add-patient [patient]
  (insert patients
          (values patient)))

(defn delete-patient [id]
  (delete patients
          (where {:id [= id]})))

(defn update-patient [id patient]
  (update patients
          (set-fields (assoc patient :updated_on (jt/local-date-time)))
          (where {:id [= id]})))

(defn get-patient [id]
  (first
    (select patients
          (where {:id [= id]}))))