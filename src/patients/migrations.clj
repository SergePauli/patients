(ns patients.migrations
  (:refer-clojure :exclude [alter drop bigint boolean char double float time complement]) 
  (:use [patients.database]
        [patients.helpers]
        [lobos migration core schema]))

(defmigration add-patients-table
  (up [] (create
          (tbl :patients
            (varchar :fio 512) (check :fio (> (length :fio) 1)) (integer :gender) (check :gender (> :gender 0)) (check :gender (< :gender 3)) (date :birth_date) (varchar :address 1024) (varchar :polis_oms 100))))
  (down [] (drop (table :patients))))
(defn run-migrations []
  (binding [lobos.migration/*migrations-namespace* 'patients.migrations]
    (migrate)))
(defn rollback-migrations []
  (binding [lobos.migration/*migrations-namespace* 'patients.migrations]
    (rollback)))