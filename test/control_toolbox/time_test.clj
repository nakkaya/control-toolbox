(ns control-toolbox.time-test
  (:use [control-toolbox.time])
  (:require [clojure.test :refer :all]))

(defn ping [] true)

(deftest fn-throttler-test
  (let [throttled-ping (fn-throttler ping 1 :second :blocking)
        begin (System/currentTimeMillis)]
    (throttled-ping)
    (throttled-ping)
    (throttled-ping)
    (is (> (- (System/currentTimeMillis) begin) 2000))
    (is (throttled-ping)))


  (let [throttled-ping (fn-throttler ping 1 :second :non-blocking)
        begin (System/currentTimeMillis)]
    (throttled-ping)
    (throttled-ping)
    (throttled-ping)
    (is (nil? (throttled-ping)))
    (is (< (- (System/currentTimeMillis) begin) 1000))))
