(defproject codepastesite "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.4"]
                 [clj-postgresql "0.4.0"]
                 [org.clojure/java.jdbc "0.3.7"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-jetty-adapter "1.4.0-RC1"]
                 [ring/ring-defaults "0.1.5"]]
;  :plugins [[lein-ring "0.9.6"]]
                                        ;  :ring {:handler codepastesite.handler/app}
  :main ^:skip-aot codepastesite.handler
  :uberjar-name "codepastesite-standalone.jar"
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.2.0"]]}})
