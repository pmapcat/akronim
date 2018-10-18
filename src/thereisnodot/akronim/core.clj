;; @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
;; @ Copyright (c) Michael Leahcim                                                      @
;; @ You can find additional information regarding licensing of this work in LICENSE.md @
;; @ You must not remove this notice, or any other, from this software.                 @
;; @ All rights reserved.                                                               @
;; @@@@@@ At 2018-10-18 19:47 <thereisnodotcollective@gmail.com> @@@@@@@@@@@@@@@@@@@@@@@@
(ns
    ^{:doc "Inline asserts and helpers"
      :author "Michael Leahcim"}
    thereisnodot.akronim.core)

(def docstring-append (atom (fn [docstring examples]
                              docstring)))
(def example-key (atom :example))

(defn docstring-template-nothing
  [docstring examples]
  docstring)

(defn docstring-template-markdown-examples
  [docstring examples]
  (str docstring "\n\n"
       "```clojure"
       (apply
        str
        (for [[should _ result] examples]
          (str  should " => " result "\n")))
       "```"))

(defn docstring-template-hljs-examples
  [docstring examples]
  (str docstring "</br>"
       "<pre>"
       "<code class='hljs clojure'>"
       (apply
        str
        (for [[should _ result] examples]
          (str  should " => " result "\n")))
       "</code>"
       "</pre>"))

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
     [(str k) "=>" (str v)])))

(defmacro defns
  "will add examples in the form of :example and :test to the
   metadata"
  [fname docstring examples & decls]
  (list* `defn (with-meta fname
                 (assoc (meta fname)
                        @example-key `(make-example-form ~@examples)
                        :test    `(make-test-form ~@examples)))
         (@docstring-append docstring `(make-example-form ~@examples))
         decls))

(defmacro defns-
  "same as defn, yielding non-public def"
  [name & decls]
  (list* `defns (with-meta name (assoc (meta name) :private true)) decls))

