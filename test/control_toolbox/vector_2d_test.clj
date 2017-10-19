(ns control-toolbox.vector-2d-test
  (:use [control-toolbox.math.vector-2d])
  (:require [clojure.test :refer :all]
            [control-toolbox.core :refer :all]))

(deftest arithmetic-test
  (let [u [1.0 2.0]
	v [2.0 2.0]] 
    (is (= [3.0 4.0] (add u v)))
    (is (= [1.0 0.0] (sub v u)))
    (is (= [5.0 5.0] (mul [1.0 1.0] 5.0)))
    (is (= true (equal? [5.0 5.0] [5.0 5.0])))))

(deftest operations-test
  (let [u [1.0 1.0]]
    (is (= {:r 13.0 :t 0.39479}) (to-polar 12 5))
    (is (= [-1.0 -1.0] (negative u)))
    (is (= 1.4142135623730951 (magnitude u)))
    (is (= [-1.3072872417272239 -1.5136049906158564] (to-cartesian 2 4)))
    (is (= 2.0 (dist [0.0 0.0] [2.0 0.0])))
    (is (= 0.0 (dist-manhattan [1.0 1.0] [1.0 1.0])))
    (is (= [0.7071067811865475 0.7071067811865475] (normalize u)))
    (is (= 2.0 (dot-product u u)))
    (is (= "1.00"
           (format "%.2f" (-> (project [1.0 1.0] [10.0 10.0]) first))))
    (is (= "1.00"
           (format "%.2f" (-> (project [1.0 1.0] [10.0 10.0]) second))))
    (is (= [6.123233995736766E-17 1.0] (rotate [1.0 0.0] (Math/toRadians 90))))
    (is (= [6.123233995736766E-17 -1.0] (rotate [1.0 0.0] (Math/toRadians -90))))    
    (is (= [1.0 1.0] 
           (bisect-angle [0.0 5.0] [5.0 0.0])))
    (is (= true (in-view-cone? [0.0 0.0] [1.0 1.0] (Math/toRadians 90) [5.0 5.0])))
    (is (= false (in-view-cone? [1.0 3.0] [1.0 1.0] (Math/toRadians 71) [3.0 2.0])))
    (is (= true (in-view-cone? [1.0 3.0] [1.0 1.0] (Math/toRadians 74) [3.0 2.0])))
    ;;(is (= true (parallel? [3 -2] [8 -12])))
    (is (= true (parallel? [1.0 2.0] [2.0 4.0])))    
    (is (= false (perpendicular? [1.0 5.0] [5.0 1.0])))
    (is (= true (perpendicular? [0.0 5.0] [5.0 0.0])))
    (is (= [5.0 0.0]
           (closest-point-on-line [1.0 0.0] [10.0 0] [5.0 0.0])))
    (is (= [1.0 0.0] (closest-point-on-circle [2.0 0.0] [0.0 0.0] 1.0)))
    (is (= false
           (line-circle-collision [1.0 0.0] [10.0 0.0] 
            			  [5.0 5.0] 1.0)))
    (is (= true
           (line-circle-collision [1.0 0.0] [10.0 0.0] [5.0 5.0] 10.0)))
    (is (= true (point-in-circle? [1.0 1.0] [0.0 0.0] 10.0)))
    (is (= false (point-in-circle? [11.0 11.0] [0.0 0.0] 10.0)))
    (is (= 180.0 (Math/toDegrees (bearing [0.0 0.0] [0.0 2.0]))))
    (is (= -135.0 (Math/toDegrees 
                   (bearing [0.0 0.0] [1.0 1.0]))))
    (is (= [1 45] (octant [2 1])))
    (is (= [5 225] (octant [-3 -2])))
    (is (= [4 360] (quadrant [2 -3])))
    (is (= [2 180] (quadrant [-2 3])))
    
    (is (= [-10.0 0.0] (distance-behind-line [0.0 0.0] [10.0 0.0] 10)))
    (is (= true (circle-circle-collision [0.0 0.0] 10 [10.0 0.0] 10)))
    (is (= false (circle-circle-collision [0.0 0.0] 10 [21.0 0.0] 10)))
    (is (= [0.0 0.0] (line-intersection [-10.0 0.0] [10.0 0.0]
                                        [0.0 -10.0] [0.0 10.0])))
    (is (= true (point-in-rectangle
                 [-10.0 10.0] [10.0 10.0]
                 [-10.0 -10.0] [10.0 -10.0]
                 [0.0 0.0])))
    (is (= true (rectangle-circle-collision
                 [[-10.0 10.0] [10.0 10.0]
                  [-10.0 -10.0] [10.0 -10.0]]
                 [0.0 0.0] 10.0)))
    (is (= false (rectangle-circle-collision
                  [[-10.0 10.0] [10.0 10.0]
                   [-10.0 -10.0] [10.0 -10.0]]
                  [20.0 20.0] 10.0)))
    (is (= 90.0 (Math/toDegrees (angle-between-points [1.0 0.0] [0.0 1.0]))))
    (is (= 45.0 (Math/toDegrees (angle-between-points [1.0 0.0] [1.0 1.0]))))))
