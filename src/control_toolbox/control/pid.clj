(ns control-toolbox.control.pid
  (:import (edu.dhbw.mannheim.tigers.sumatra.util PIDController))
  (:use control-toolbox.math))

(defn- power-band [out-max out-min]
  (cond (zero? out-min) [1 0]
        :default [1 -1]))

(defn- update-pid [s]
  (let [{:keys [controller set-point bounds]} s
        [in-max in-min out-max out-min] bounds
        [range-max range-min] (power-band out-max out-min)]
    (.setSetpoint    controller (scale set-point in-min in-max range-min range-max))
    (.setInputRange  controller range-min range-max)
    (.setOutputRange controller range-min range-max)))

(defn new-pid [kp ki kd continuous]
  (PIDController. kp ki kd continuous))

(defn pid
  ([s]
   (let [{:keys [kp kd ki bounds continuous]} s
         [in-max in-min out-max out-min] bounds
         pid        (new-pid kp ki kd
                             (if (nil? continuous) false continuous))
         controller (atom (assoc s :controller pid))
         watch-fn   (fn [_ _ _ new-value] (update-pid new-value))]
     (update-pid @controller)
     (add-watch controller nil watch-fn)
     controller))
  ([s v]
   (let [{:keys [controller bounds]} @s
         [in-max in-min out-max out-min] bounds
         [range-max range-min] (power-band out-max out-min)]

     (.update controller (scale v in-min in-max range-min range-max))
     (scale (.getResult controller) range-min range-max out-min out-max))))

(defmacro defpid
  [name & specs]
  `(let [spec# (pid (apply hash-map (quote ~specs)))]

     (defn ~name [curr#]
       (pid spec# curr#))
     
     (defn ~(symbol (str name \!)) [sp#]
       (swap! spec# (fn [p#] (assoc p# :set-point sp#))))

     (defn ~(symbol (str name "-specs")) []
       spec#)))
