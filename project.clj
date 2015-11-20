(defproject control-toolbox "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/algo.generic "0.1.2"]
                 [net.mikera/core.matrix "0.44.0"]
                 [net.n01se/clojure-jna "1.0.0"]]
  :java-source-paths ["src/java"]
  :repl-options {:port 7888}
    :jvm-opts ["-Djna.library.path=/usr/local/Cellar/opencv/2.4.9/lib/"])
