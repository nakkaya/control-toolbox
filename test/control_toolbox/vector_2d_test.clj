(ns control-toolbox.vector-2d-test
  (:refer-clojure :exclude [+ - * =])
  (:use (clojure.algo.generic [arithmetic :only [+ - *]]
                              [comparison :only [=]]))
  (:use [control-toolbox.math.vector-2d])
  (:require [clojure.test :refer :all]
            [control-toolbox.core :refer :all]))

(deftest arithmetic-test
  (let [u (vector-2d 1 2)
	v (vector-2d 2 2)] 
    (is (= (vector-2d 3 4) (+ u v)))
    (is (= (vector-2d 1 0) (- v u)))
    (is (= (vector-2d 5 5) (* (vector-2d 1 1) 5)))
    (is (= (vector-2d 5 5) (vector-2d 5 5)))))

(deftest operations-test
  (let [u (vector-2d 1 1)] 
    (is (= (vector-2d -1 -1) (negative u)))
    (is (= 1.4142135623730951 (magnitude u)))
    (is (= 2.0 (dist (vector-2d 0 0) (vector-2d 2 0))))
    (is (= (vector-2d 0.7071067811865475 0.7071067811865475) (normalize u)))
    (is (= 2.0 (dot-product u u)))
    (is (= "1.00"
           (format "%.2f" (-> (project (vector-2d 1 1) (vector-2d 10 10)) :x))))
    (is (= "1.00"
           (format "%.2f" (-> (project (vector-2d 1 1) (vector-2d 10 10)) :y))))
    (is (= (vector-2d 6.123233995736766E-17 1.0)
           (rotate (vector-2d 1 0) 90)))
    (is (= (vector-2d 5.0 0.0)
           (closest-point-on-line (vector-2d 1 0) (vector-2d 10 0) 
        			  (vector-2d 5 5))))
    (is (= false
           (line-circle-collision (vector-2d 1 0) (vector-2d 10 0) 
        			  (vector-2d 5 5) 1)))
    (is (= true
           (line-circle-collision (vector-2d 1 0) (vector-2d 10 0) 
        			  (vector-2d 5 5) 10)))
    (is (= true (parallel? (vector-2d 2 -3) (vector-2d 8 -12))))
    (is (= true (parallel? (vector-2d 1 2) (vector-2d 2 4))))
    (is (= false (perpendicular? (vector-2d 1 5) (vector-2d 5 1))))
    (is (= true (perpendicular? (vector-2d 0 5) (vector-2d 5 0))))
    (is (= (vector-2d 1.0 1.0) 
           (bisect-angle (vector-2d 0 5) (vector-2d 5 0))))
    (is (= true 
           (in-view-cone? (vector-2d 0 0) 
        		  (vector-2d 1 1) 90 (vector-2d 5 5))))
    (is (= false (in-view-cone? (vector-2d 1 3) 
        			(vector-2d 1 1) 71 (vector-2d 3 2))))
    (is (= true (in-view-cone? (vector-2d 1 3) 
        		       (vector-2d 1 1) 74 (vector-2d 3 2))))
    (is (= true (point-in-circle? (vector-2d 1 1) (vector-2d 0 0) 10)))
    (is (= false (point-in-circle? (vector-2d 11 11) (vector-2d 0 0) 10)))
    (is (= 180.0 (Math/toDegrees (bearing (vector-2d 0 0) (vector-2d 0 2)))))
    (is (= -135.0 (Math/toDegrees 
        	 (bearing (vector-2d 0 0) (vector-2d 1 1)))))
    (is (= (vector-2d 1 0) (closest-point-on-circle (vector-2d 2 0) (vector-2d 0 0) 1)))
    (is (= (vector-2d -10 0) (distance-behind-line (vector-2d 0 0) (vector-2d 10 0) 10)))
    (is (= true (circle-circle-collision (vector-2d 0 0) 10 (vector-2d 10 0) 10)))
    (is (= false (circle-circle-collision (vector-2d 0 0) 10 (vector-2d 21 0) 10)))
    (is (= (vector-2d 0 0) (line-intersection (vector-2d -10 0) (vector-2d 10 0)
                                              (vector-2d 0 -10) (vector-2d 0 10))))
    (is (= true (point-in-rectangle
                 (vector-2d -10 10) (vector-2d 10 10)
                 (vector-2d -10 -10) (vector-2d 10 -10)
                 (vector-2d 0 0))))
    (is (= true (rectangle-circle-collision
                 [(vector-2d -10 10) (vector-2d 10 10)
                  (vector-2d -10 -10) (vector-2d 10 -10)]
                 (vector-2d 0 0) 10)))
    (is (= false (rectangle-circle-collision
                  [(vector-2d -10 10) (vector-2d 10 10)
                   (vector-2d -10 -10) (vector-2d 10 -10)]
                  (vector-2d 20 20) 10)))))