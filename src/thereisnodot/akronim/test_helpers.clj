;; @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
;; @ Copyright (c) Michael Leahcim                                                      @
;; @ You can find additional information regarding licensing of this work in LICENSE.md @
;; @ You must not remove this notice, or any other, from this software.                 @
;; @ All rights reserved.                                                               @
;; @@@@@@ At 2018-10-18 22:28 <thereisnodotcollective@gmail.com> @@@@@@@@@@@@@@@@@@@@@@@@
(ns
    ^{:doc "Inline asserts and helpers"
      :author "Michael Leahcim"}
    thereisnodot.akronim.test-helpers
  (:require [thereisnodot.akronim.core :refer [defns]]))

(defns no-test-fn
  "Should have an example, but no `:test` implementation"
  {:no-test? true}
  [(no-test-fn 3 4) => 7]
  ([a b]
   (+ a b)))

(defns should-have-no-examples
  "shouldn't have examples or test  because it is empty. "
  [] [] 3)

(defns
  ^{:whatever "works"}
  different-meta-declarations
  "Some text"
  {:echo true}
  [(different-meta-declarations 3 3) => 9]
  [a b]
  (* a b))

(defns variadic-fn
  "Takes several arguments as parameters"
  [(variadic-fn 1 1) => 2
   (variadic-fn 1 1 1) => 3
   (variadic-fn 1 1 1 1) => 5]
  ([a b]
   (variadic-fn a b 0 0))
  ([a b c]
   (variadic-fn a b c 0))  
  ([a b c d]
   (+ a b c d)))

(defns test-defns-simple-mult-fail
  "Multiplies two numbers. Tests should fail"
  [(test-defns-simple-mult-fail 2 3) => 6
   (test-defns-simple-mult-fail 3 3) => 9
   (test-defns-simple-mult-fail 0 1) => 12]
  [a b]
  (* a b))

(defns test-defns-simple-sum
  "Sums two numbers. Tests should pass"
  [(test-defns-simple-sum 2 3) => 5
   (test-defns-simple-sum 3 3) => 6
   (test-defns-simple-sum 0 0) => 0]
  [a b] (+ a b))

(defns ^{:what? "nothng"} test-defns-meta
  "Sums two numbers. Tests should pass"
  [(test-defns-simple-sum 2 3) => 5]
  [a b] (+ a b))

(defns something-to-test
  "Multtplies two numbers. Passes tests correctly"
  [(something-to-test 3 3) => 9
   (something-to-test 2 3) => 6
   (something-to-test 2 8) => 16]
  [a b]
  (* a b))

(defns something-to-fail
  "Sums two numbers. Fails tests"
  [(something-to-test 3 3) => 6
   (something-to-test 2 3) => 5
   (something-to-test 2 8) => 16]
  [a b]
  (+ a b))
