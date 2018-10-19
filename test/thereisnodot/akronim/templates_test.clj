;; @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
;; @ Copyright (c) Michael Leahcim                                                      @
;; @ You can find additional information regarding licensing of this work in LICENSE.md @
;; @ You must not remove this notice, or any other, from this software.                 @
;; @ All rights reserved.                                                               @
;; @@@@@@ At 2018-10-18 23:38 <thereisnodotcollective@gmail.com> @@@@@@@@@@@@@@@@@@@@@@@@

(ns ^{:doc "Testing for akronim templates"
      :author "Michael Leahcim"}
  thereisnodot.akronim.templates-test
  (:require [clojure.test :refer :all]
            [thereisnodot.akronim.templates :as doctest-templates]))

;; TODO:
;; * set up templating through: https://github.com/weavejester/environ,
;;   :edn "akronim_markdown|akronim_html|akronim_base"
;; * fix up readme

(def case-list
  ["(hello 3 4)"  "=>" "7"
   "(hello 4 4)"  "=>" "8"
   "(hello 2 -2)" "=>" "0"])

(deftest test-docstring-template-nothing
  (testing "General usage"
    (is (=  (doctest-templates/docstring-template-nothing "Hello world" case-list)
            "Hello world"))))
(deftest test-docstring-template-text
  (testing "General usage"
    (is (=  (doctest-templates/docstring-template-text "Hello world" case-list)
            "Hello world\n\n(hello 3 4) => 7\n(hello 4 4) => 8\n(hello 2 -2) => 0\n"))))

(deftest test-docstring-template-markdown
  (testing "General usage"
    (is (=  (doctest-templates/docstring-template-markdown "Hello world" case-list)
            "Hello world\n\n```clojure\n\n(hello 3 4) => 7\n(hello 4 4) => 8\n(hello 2 -2) => 0\n\n```"))))

(deftest test-docstring-template-hljs
  (testing "General usage"
    (is (=   (doctest-templates/docstring-template-hljs "Hello world" case-list)
            "Hello world</br><pre><code class='hljs clojure'>\n(hello 3 4) => 7\n(hello 4 4) => 8\n(hello 2 -2) => 0\n</code></pre>"))))
