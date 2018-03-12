(ns control-toolbox.filters.core)

(defn moving-average-filter [alpha]
  (let [avrg (atom 0)]
    (fn [data]
      (reset! avrg (+ (* alpha data) (* (- 1. alpha) @avrg))))))
