(ns control-toolbox.math-test
  (:use [control-toolbox.math])
  (:require [clojure.test :refer :all]))

(deftest scale-test
  (is (= 0 (scale 0 0 1500 0 500)))
  (is (= 500 (scale 1500 0 1500 0 500))))

(deftest clamp-test
  (is (= 6 (clamp 5 6 9)))
  (is (= 2 (clamp 1 2 6)))
  (is (= 8 (clamp 9 6 8))))

(deftest in-range-test
  (is (= false (in-range? 5 4 4)))
  (is (= true (in-range? 3 2 6))))

(deftest to-radians-test
  (is (= 0.0 (to-radians 0)))
  (is (= "1.571" (format "%.3f" (to-radians 90)))))

(deftest to-degrees-test
  (is (= 0.0 (to-degrees 0)))
  (is (= "114.6" (format "%.1f" (to-degrees 2)))))
