(ns control-toolbox.holonomic-test
  (:use [control-toolbox.math]
        [control-toolbox.motion.holonomic])
  (:require [clojure.test :refer :all]))


(def spec (holomonic-motion-init {:diameter 1
                                  :angles [0 90 180 270]}))


(deftest scale-test
  (let [[w1 w2 w3 w4] (holomonic-motion-speeds spec [0 1] 0)]
    (is (=  2.0 w1))
    (is (= -2.0 w3))))
