(ns control-toolbox.pid-test
  (:require [clojure.test :refer :all]
            [control-toolbox.control.pid :refer :all]))

(deftest pid-setpoint-range
  (testing "Test Set Point Range"
    (let [spec {:kp 2
                :ki 0
                :kd 0
                :set-point 0
                :bounds [10 -10 1 -1]}
          p (pid spec)]
      (is (= 0.0 (do (swap! p assoc :set-point 0)
                     (.getSetpoint (:controller @p)))))
      (is (= 1.0 (do (swap! p assoc :set-point 50)
                     (.getSetpoint (:controller @p)))))
      (is (= (- 1.0) (do (swap! p assoc :set-point -50)
                         (.getSetpoint (:controller @p))))))))

(deftest pid-in-out-range
  (testing "Test In Out Range"
    (let [spec {:kp 1
                :ki 0
                :kd 0
                :set-point 0
                :bounds [10 -10 1 -1]}
          p (pid spec)]
      (is (= 0.0 (pid p 0)))
      (is (= -0.5 (pid p 5)))
      (is (= 0.5 (pid p -5)))
      (is (= -1.0 (pid p 100)))
      (is (= 1.0 (pid p -100)))
      (is (= -1.0 (pid p 10)))
      (is (= 1.0 (pid p -10))))))

(deftest pid-range-oneway
  (testing "Test One-Way Range"
    (let [spec {:kp -1
                :ki 0
                :kd 0
                :set-point 0
                :bounds [10 0 1 0]}
          p (pid spec)]
      (is (zero? (pid p 0)))
      (is (= 1.0 (pid p 10)))
      (is (= 0.5 (pid p 5))))))
