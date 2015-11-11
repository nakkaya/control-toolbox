(ns control-toolbox.math)

(defn scale [x in-min in-max out-min out-max]
  (+ (/ (* (- x in-min) (- out-max out-min)) (- in-max in-min)) out-min))

(defn clamp [x min max]
  (cond
    (> x max) max
    (< x min) min
    :default x))

(defn in-range? [x a b]
  (cond (< x a) false
        (> x b) false
        :else true))

(defn to-radians [x]
  (Math/toRadians x))

(defn to-degrees [x]
  (Math/toDegrees x))

(defn sin [x]
  (Math/sin x))

(defn cos [x]
  (Math/cos x))
