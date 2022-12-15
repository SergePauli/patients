(ns patients.database
  (:require [korma.db :as korma]
            [lobos.connectivity :as lobos]))

(def db-connection-info
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"   
   :user "admin"
   :password "Rhyxu1Fu6"
   :subname "//localhost:5432/patients"})

; set up korma
(korma/defdb db db-connection-info)
; set up lobos
(lobos/open-global db-connection-info)