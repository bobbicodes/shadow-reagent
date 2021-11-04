(ns shadow-reagent.app-test
  (:require [cljs.test :refer [deftest is run-tests]]
            [shadow-reagent.app :as app]))

(deftest square-root-test
  (is (= (app/square-root 4) 2)))

(comment
  (run-tests)
  )