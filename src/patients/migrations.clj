(ns patients.migrations
  (:refer-clojure :exclude [alter drop bigint boolean char double float time complement]) 
  (:use [patients.database]
        [patients.helpers]
        [lobos migration core schema]))

(defmigration add-patients-table
  (up [] (create
          (tbl :patients
               (varchar :fio 512 :not-null) (integer :gender :not-null) (check :gender (> :gender 0)) (check :gender (< :gender 3)) (date :birth_date :not-null) (varchar :address 1024) (varchar :polis_oms 100))))
  (down [] (drop (table :patients))))
(defn run-migrations []
  (binding [lobos.migration/*migrations-namespace* 'patients.migrations]
    (migrate)))
(defn rollback-migrations []
  (binding [lobos.migration/*migrations-namespace* 'patients.migrations]
    (rollback)))