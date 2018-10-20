(defproject thereisnodot/akronim "0.1.2-SNAPSHOT"
  :description "Doctest like functionality for Clojure programming language"
  :url "https://github.com/michaelleachim/akronim"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev {:plugins [[com.jakemccrary/lein-test-refresh "0.23.0"]]}}
  :dependencies [[environ "1.1.0"]
                 [clojure-future-spec "1.9.0-alpha17"]
                 [zprint "0.4.10"]
                 [org.clojure/clojure "1.8.0"]])
