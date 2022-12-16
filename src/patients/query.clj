(ns patients.query
  (:require [patients.database]
            [clj-time.coerce :as t]
            [korma.core :refer :all]))

(defentity patients)

(defn get-patients []
  (select patients))

(defn add-patient [fio gender birth_date address polis_oms]
  (insert patients
          (values {:fio fio :gender gender :birth_date (t/to-sql-date birth_date) :address address :polis_oms polis_oms})))

(defn delete-patient [id]
  (delete patients
          (where {:id [= id]})))

(defn update-patient [id fio gender birth_date address polis_oms]
  (update patients
          (set-fields {:fio fio :gender gender :birth_date (t/to-sql-date birth_date) :address address :polis_OMS polis_oms})
          (where {:id [= id]})))

(defn get-patient [id]
  (first
    (select patients
          (where {:id [= id]}))))