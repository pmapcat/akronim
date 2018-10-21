;; @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
;; @ Copyright (c) Michael Leachim                                                      @
;; @ You can find additional information regarding licensing of this work in LICENSE.md @
;; @ You must not remove this notice, or any other, from this software.                 @
;; @ All rights reserved.                                                               @
;; @@@@@@ At 2018-17-10 23:32 <mklimoff222@gmail.com> @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

(ns ^{:doc "Testing for akronim package"
      :author "Michael Leahcim"}
  thereisnodot.akronim.core-test
  (:require [clojure.test :refer :all]
            [thereisnodot.akronim.test-helpers :as test-helpers]
            [thereisnodot.akronim.core :as doctest :refer
             [defns make-test-form make-example-form]]))

(deftest test-make-example-form
  (let [mult (fn [a b] (* a b))]
    (testing "Should work on let declared forms"
      (is
       (=
        (make-example-form
         (mult 3 2) => 6
         (mult 5 8) => 40)
        (list
         ["(mult 3 2)" "=>" "6"]
         ["(mult 5 8)" "=>" "40"]))))
    (testing "Formatting should work"
      (is
       (=
        (make-example-form
         {:hello {:my {:dear {:friend "whatever" :this "thing" :doesnt "work" "as" "expected"}}}} =>
         {:whatever [:whenever :what :else :should :work]})
        (list
       ["{:hello {:my {:dear {:doesnt \"work\",
                     :friend \"whatever\",
                     :this \"thing\",
                     \"as\" \"expected\"}}}}" "=>"
        "{:whatever [:whenever :what :else :should :work]}"]))))
    (testing "Should work on fns from other ns"
      (is
       (=
        (make-example-form
         (+ 3 4) => 7
         (* 2 9) => 18
         (- 3 3) => 0
         (/ 1 0) => 0)
        (list ["(+ 3 4)" "=>" "7" ]
              ["(* 2 9)" "=>" "18"]
              ["(- 3 3)" "=>" "0" ]
              ["(/ 1 0)" "=>" "0" ]))))))

(deftest test-make-test-form
  (let [mult (fn [a b] (* a b))]
    (testing "Basic workage"
      (is
       (=
        ((make-test-form
          (mult 3 2) => 6
          (mult 5 8) => 40)) nil)))
    (testing "Should raise an assert error"
      (is
       (thrown? AssertionError
                ((make-test-form
                  (mult 3 2) => 6
                  (mult 5 8) => 41)))))
    (testing "Should work on fns from other ns"
      (is
       (thrown? ArithmeticException
        ((make-test-form
          (+ 3 4) => 7
          (* 2 9) => 18
          (- 3 3) => 0
          (/ 1 0) => 0)))))))

(deftest test-defns
  (testing "Has example key"
    (is
     (=  (:akronim/example (meta #'test-helpers/test-defns-simple-sum))
         (list ["(test-defns-simple-sum 2 3)" "=>" "5"]
               ["(test-defns-simple-sum 3 3)" "=>" "6"]
               ["(test-defns-simple-sum 0 0)" "=>" "0"]))))
  (testing "should have examples, but not test. Because of `:no-test?` metadata key. But `:doc` shold be inplace"
    (is (=  (:akronim/example (meta #'test-helpers/no-test-fn))
            (list ["(no-test-fn 3 4)" "=>" "7"])))
    (is (=  (:doc (meta #'test-helpers/no-test-fn))
            "Should have an example, but no `:test` implementation"))
    (is (=  (:test (meta #'test-helpers/no-test-fn)) nil)))
  
  (testing "shouldn't have examples or test  because it is empty. "
    (is (=  (:akronim/example (meta #'test-helpers/should-have-no-examples)) nil))
    (is (=  (:test            (meta #'test-helpers/should-have-no-examples)) nil))
    (is (=  (:doc            (meta #'test-helpers/should-have-no-examples))
            "shouldn't have examples or test  because it is empty. ")))
 
  (testing "workage of different meta declarations"
    (let [meta-sym (meta #'test-helpers/different-meta-declarations)]
      (is (=  (:akronim/example meta-sym)
              (list ["(different-meta-declarations 3 3)" "=>" "9"])))
      (is (=  (:doc meta-sym) "Some text"))
      (is (=  (:echo meta-sym) true))
      (is (=  (:whatever meta-sym) "works"))))
  
  
  
  (testing "Test testing function"
    (is (thrown? AssertionError
                 (test #'test-helpers/test-defns-simple-mult-fail))
        "Should raise assertion error, as test is incorrect")
    (is (= (test #'test-helpers/test-defns-simple-sum) :ok) "Should pass"))
  
  (testing "Should also work from other fns"
    (is (=  (test  #'test-helpers/something-to-test) :ok))
    (is (thrown? AssertionError  (test  #'test-helpers/something-to-fail))))
  
  (testing "Should work on variadic functions"
    (is (thrown? AssertionError  (test #'test-helpers/variadic-fn)))))
