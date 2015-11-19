(ns control-toolbox.motion.holonomic
  (:refer-clojure :exclude [+ - * =])
  (:require [clojure.core.matrix :as m])
  (:use (clojure.algo.generic [arithmetic :only [+ - *]]
                              [comparison :only [=]]))
  (:use [control-toolbox.math.vector-2d]))

(defn- velocity-coupling-matrix [& n]
  {:pre [(> (count n) 2)]}
  (map #(vector (- (Math/sin (Math/toRadians %))) (Math/cos (Math/toRadians %)) 1) n))

(defn holomonic-motion-init [specs]
  (let [{:keys [angles diameter]} specs]
    (merge {:vcm (apply velocity-coupling-matrix angles)
            :ang-vel-fn #(/ (* 2 %) diameter)}
           specs)))

(defn holomonic-motion-ratios [motion x y r]
  (m/eseq (m/mmul (m/matrix (:vcm motion)) (m/matrix [[x] [y] [r]]))))

(defn holomonic-motion-speeds [motion {x :x y :y} r]
  (let [{:keys [ang-vel-fn]} motion]
    (holomonic-motion-ratios
     motion (ang-vel-fn x) (ang-vel-fn y) (ang-vel-fn r))))
