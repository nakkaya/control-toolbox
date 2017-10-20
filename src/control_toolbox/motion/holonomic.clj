(ns control-toolbox.motion.holonomic
  (:require [clojure.core.matrix :as m])
  (:use [control-toolbox.math]
        [control-toolbox.math.vector-2d]))

(defn- velocity-coupling-matrix [& n]
  {:pre [(> (count n) 2)]}
  (map #(vector (- (sin (to-radians %))) (cos (to-radians %)) 1) n))

(defn holomonic-motion-init [specs]
  (let [{:keys [angles diameter]} specs]
    (merge {:vcm (apply velocity-coupling-matrix angles)
            :ang-vel-fn #(/ (* 2 %) diameter)}
           specs)))

(defn holomonic-motion-ratios [motion x y r]
  (m/eseq (m/mmul (m/matrix (:vcm motion))
                  (m/matrix [[x] [y] [r]]))))

(defn holomonic-motion-speeds [motion [x y] r]
  (let [{:keys [ang-vel-fn]} motion]
    (holomonic-motion-ratios
     motion (ang-vel-fn x) (ang-vel-fn y) (ang-vel-fn r))))
