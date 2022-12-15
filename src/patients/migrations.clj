(ns patients.migrations
  (:refer-clojure :exclude
        [alter drop bigint boolean char double float time complement])
  (:use [patients.database]
        [lobos migration core schema]))

(defmigration add-patients-table
  (up [] (create (table :patients
                        (integer :id :primary-key :auto-inc)
                        (varchar :fio 512)
                        (integer :gender 1)
                        (date :birth_date)
                        (varchar :address 1024)
                        (varchar :polis_OMS 100))))
  (down [] (drop (table :patients))))

(defn run-migrations []
  (binding [lobos.migration/*migrations-namespace* 'patients.migrations]
    (migrate)))