(ns control-toolbox.control.pid
  (:import (edu.dhbw.mannheim.tigers.sumatra.util PIDController))
  (:use control-toolbox.math))

(defn- power-band [out-max out-min]
  (cond (zero? out-min) [1 0]
        :default [1 -1]))

(defn- update-pid [s pid]
  (let [{:keys [set-point kp kd ki bounds continuous]} s
        [in-max in-min out-max out-min] bounds
        [range-max range-min] (power-band out-max out-min)]
    (.setSetpoint pid (scale set-point in-min in-max range-min range-max))
    (.setInputRange pid range-min range-max)
    (.setOutputRange pid range-min range-max)))

(defn new-pid [kp ki kd continuous]
  (PIDController. kp ki kd continuous))

(defn pid
  ([s]
   (let [{:keys [kp kd ki bounds continuous]} s
         [in-max in-min out-max out-min] bounds
         pid (new-pid kp ki kd (if (nil? continuous) false continuous))
         controller (atom (assoc s :controller pid :error 0.0))
         watch-fn (fn [_key _ref old-value new-value]
                    (update-pid new-value (:controller new-value)))]
     (update-pid s pid)
     (add-watch controller nil watch-fn)
     controller))
  ([s v]
   (let [{:keys [bounds]} @s
         [in-max in-min out-max out-min] bounds
         [range-max range-min] (power-band out-max out-min)]
     (.update (:controller @s) (scale v in-min in-max range-min range-max))
     (swap! s assoc :error (.getError (:controller @s)))
     (scale (.getResult (:controller @s)) range-min range-max out-min out-max))))

(defmacro defpid
  [name & specs]
  `(let [spec# (pid (apply hash-map (quote ~specs)))]

     (defn ~name [curr#]
       (pid spec# curr#))
     
     (defn ~(symbol (str name \!)) [sp#]
       (swap! spec# (fn [p#] (assoc p# :set-point sp#))))

     (defn ~(symbol (str name "-specs")) []
       spec#)))
