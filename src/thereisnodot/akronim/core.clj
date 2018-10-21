;; @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
;; @ Copyright (c) Michael Leahcim                                                      @
;; @ You can find additional information regarding licensing of this work in LICENSE.md @
;; @ You must not remove this notice, or any other, from this software.                 @
;; @ All rights reserved.                                                               @
;; @@@@@@ At 2018-10-18 19:47 <thereisnodotcollective@gmail.com> @@@@@@@@@@@@@@@@@@@@@@@@
(ns
    ^{:doc "Inline asserts and helpers"
      :author "Michael Leahcim"}
    thereisnodot.akronim.core
  (:require [thereisnodot.akronim.templates :as tmpl]
            [environ.core :as environ]
            [zprint.core :as zp]))

(def docstring-append
  (condp = (environ/env :akronim-docstring)
    "nothing"  tmpl/docstring-template-nothing
    "text"     tmpl/docstring-template-text
    "markdown" tmpl/docstring-template-markdown
    "hljs"     tmpl/docstring-template-hljs
    tmpl/docstring-template-nothing))

(defmacro make-test-form
  "Will turn user friendly description into an assert/test form"
  [& input]
  `(fn []
    ~(cons
      `do
      (for [[k _ v] (partition 3 input)]
        `(assert (=  ~k ~v))))))

(defmacro make-example-form
  "Will turn input forms into strings for documentation"
  [& input]
  (cons
   list
   (for [[k _ v] (partition 3 input)]
     [(zp/zprint-str k 80) "=>" (zp/zprint-str v 80)])))

;; (defmacro defns
;;   "will add examples in the form of :example and :test to the
;;    metadata"
;;   [fname docstring examples & decls]
;;   (list* `defn (with-meta fname
;;                  (assoc (meta fname)
;;                         :akronim/example
;;                         `(make-example-form ~@examples)
;;                         :test
;;                         (if (:no-test? (meta fname)) nil `(make-test-form ~@examples))
;;                         :doc             (docstring-append docstring examples)))
;;          decls))

(defmacro defns
  "will add examples in the form of :example and :test to the
   metadata"
  [fname & decls]
  (let [;; if string, then docstring, so add
        m (if (string? (first decls)) {:doc (first decls)} {})
        ;; if string then docstring, so skip
        decls  (if (string? (first decls)) (next decls) decls)
        ;; if map, then metadata, so add
        m (if (map? (first decls)) (conj m (first decls)) m)
        ;; if map, then metada, so skip
        decls  (if (map? (first decls)) (next decls) decls)
        ;; if vec, then examples, so add
        examples (if (vector? (first decls)) (first decls) [])
        ;; if vector, then examples, so skip
        decls (if (vector? (first decls)) (next decls) decls)
        m (merge (meta fname) m)
        ;; rest goes to original defn function
        ]
    (if-not (empty? examples)
      (list* `defn (with-meta fname
                     (assoc m
                            :akronim/example
                            `(make-example-form ~@examples)
                            :test
                            (if (:no-test? m) nil `(make-test-form ~@examples))
                            :doc             (docstring-append (:doc m) examples)))
             decls)
      ;; when no examples, let the function be the usual
      (list* `defn (with-meta fname m) decls))))

(defmacro defns-
  "same as defn, yielding non-public def"
  [name & decls]
  (list* `defns (with-meta name (assoc (meta name) :private true)) decls))
