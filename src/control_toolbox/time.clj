(ns control-toolbox.time)

(defn fn-throttler-aux-blocking [f rate-ms t-last-run]
  (fn [& args]
    (let [elapsed (- (System/currentTimeMillis) @t-last-run)
          left-to-wait (- rate-ms elapsed)]
      (when (pos? left-to-wait)
        (Thread/sleep left-to-wait)))
    (let [ret (apply f args)]
      (reset! t-last-run (System/currentTimeMillis))
      ret)))

(defn fn-throttler-aux-non-blocking [f rate-ms t-last-run]
  (fn [& args]
    (let [now (System/currentTimeMillis)]
      (when (>= (- now @t-last-run) rate-ms)
        (let [ret (apply f args)]
          (reset! t-last-run now)
          ret)))))

(defmacro fn-throttler [f rate unit policy]
  (let [unit->ms {:millisecond 1
                  :second 1000
                  :minute 60000
                  :hour 3600000
                  :day 86400000
                  :month 2678400000}
        rate-ms (/ (unit->ms unit) rate)
        state '(atom (System/currentTimeMillis))]
    (if (= policy :blocking)
      `(~'fn-throttler-aux-blocking ~f ~rate-ms ~state)
      `(~'fn-throttler-aux-non-blocking ~f ~rate-ms ~state))))
