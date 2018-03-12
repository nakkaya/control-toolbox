(defproject control-toolbox "0.2.9"
  :description "Robotics Toolbox"
  :url "https://git.nakkaya.com/nakkaya/control-toolbox/tree/master"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [net.mikera/core.matrix "0.61.0"]
                 [net.mikera/core.matrix "0.44.0"]
                 [net.n01se/clojure-jna "1.0.0"]]
  :java-source-paths ["src/java"]
  :repl-options {:port 7888}
  :jvm-opts ["-Djna.library.path=/usr/local/Cellar/opencv/2.4.9/lib/"]
  :repositories [["releases" {:url "https://clojars.org/repo"
                              :sign-releases false}]])
