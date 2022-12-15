(ns patients.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [patients.handler :refer :all]))

(deftest test-app
  ;; (testing "main route"
  ;;   (let [response (app (mock/request :get "/index"))]
  ;;     (is (= (:status response) 200))
  ;;     (is (.contains (:body response) "TODO: Make a front-end part here"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
