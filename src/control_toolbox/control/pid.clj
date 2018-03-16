(ns control-toolbox.control.pid
  (:import (edu.dhbw.mannheim.tigers.sumatra.util PIDController))
  (:use control-toolbox.math))

(defn- update-pid [s]
  (let [{:keys [controller set-point bounds]} s
        [in-min in-max out-min out-max] bounds]
    (.setSetpoint    controller set-point)
    (.setInputRange  controller in-min in-max)
    (.setOutputRange controller out-min out-max)))

(defn new-pid [kp ki kd continuous]
  (PIDController. kp ki kd continuous))

(defn pid
  ([s]
   (let [{:keys [kp kd ki bounds continuous]} s
         [in-min in-max out-min out-max] bounds
         pid        (new-pid kp ki kd
                             (if (nil? continuous) false continuous))
         controller (atom (assoc s :controller pid))
         watch-fn   (fn [_ _ _ new-value] (update-pid new-value))]
     (update-pid @controller)
     (add-watch controller nil watch-fn)
     controller))
  ([s v]
   (let [{:keys [controller bounds]} @s]
     (.update controller v)
     (.getResult controller))))

(defmacro defpid
  [name & specs]
  `(let [spec# (pid (apply hash-map (quote ~specs)))]

     (defn ~name [curr#]
       (pid spec# curr#))
     
     (defn ~(symbol (str name \!)) [sp#]
       (swap! spec# (fn [p#] (assoc p# :set-point sp#))))

     (defn ~(symbol (str name "-specs")) []
       spec#)))
