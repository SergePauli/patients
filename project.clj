(defproject patients "0.1.0-SNAPSHOT"
  :description "Тестовое задание Clojure/ClojureScript"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-json "0.5.1"]
                 [korma "0.3.0-RC5"]
                 [lobos "1.0.0-beta1"] 
                 [clj-time "0.15.2"]
                 [org.postgresql/postgresql "LATEST"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler patients.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
