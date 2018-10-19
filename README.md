# akronim

Library for doctest like functionality for Clojure programming language

## Usage

### Installation

[![Clojars Project](https://clojars.org/thereisnodot/akronim/latest-version.svg)](https://clojars.org/thereisnodot/akronim) 

### Usage

The akronim package provides `defns` macro 
to make testing and inplace documentation easier

#### Getting started

```clojure
(require '[thereisnodot.akronim.core :refer [defns]))

;; Tests

(defns simple-sum
  "Will execute function over value of the map"
  [(simple-sum 3 4) => 7
   (simple-sum 4 5) => 9]
  [a b]
  (+ a b))
  
(test #'simple-sum) => :ok

;; On the other hand, erroneous tests will give AssertionError

(defns simple-sum-will-fail
  "Will execute function over value of the map"
  [(simple-sum 3 4) => 222]
  [a b]
  (+ a b))
  
(test #'simple-sum-will-fail) => 
AssertionError Assert failed: (clojure.core/= (test-defns-simple-sum-will-fail 3 4) 222) 

;; Examples. Providing examples in this form is useful for external documentation tools

(:akronim/example (meta #'simple-sum)) => 
  ["(simple-sum 3 4)" "=>" "7"] 
  ["(simple-sum 4 5)" "=>" "9"]
  
```


#### Using with core.test

Here is how to use it in conjunction with testing pipeline. 

```clojure

(ns thereisnodot.utils.html-test
  (:require [clojure.test :refer :all]
            [thereisnodot.utils.html :as html]))
            
(deftest test-inlines-within-namespace
  (doseq [[symbol access] (ns-publics 'thereisnodot.utils.html)]
    (when (:test (meta access))
      (testing (str "Testing inlines of: " symbol)
        (is (=  (test access) :ok))))))
```

#### Altering docstring

To alter docstring of a function at the time of declaration 
set the following config option either as: `.lein-env` or `.boot-env` or environment
variables or Java system properties

Set `akronim_docstring` parameter as:
* **nothing** will not alter docstring on declaration
* **text**    will append examples at the end of the docstring
* **markdown** will wrap the declaration into markdown Clojure code block and append examples  at the end of the docstring
* **hljs** will wrap the declaration into HTML code for https://highlightjs.org and append examples at the end of the docstring

Default is **nothing**

```clojure

(defns simple-sum
  "Will execute function over value of the map"
  [(simple-sum 3 4) => 7
   (simple-sum 4 5) => 9]
  [a b]
  (+ a b))
  
;; with nothing set
(:doc (meta #'simple-sum)) => "Will execute function over value of the map"

;; with text set
(:doc (meta #'simple-sum)) => 
"Will execute function over value of the map

(simple-sum 3 4) => 7
(simple-sum 4 5) => 9"

;; with markdown set
(:doc (meta #'simple-sum)) => 
"Will execute function over value of the map

```clojure
(simple-sum 3 4) => 7
(simple-sum 4 5) => 9
```"

;; with hljs set
(:doc (meta #'simple-sum)) => 
"Will execute function over value of the map</br>
<pre>
  <code class='cljs clojure'>
    (simple-sum 3 4) => 7
    (simple-sum 4 5) => 9
  </code>
</pre>
```


#### Gotchas

* Right now, `akronim` requires docstring to be present in the generated function. 
* There are tools that assume function declaration form to be `defn` not `defns`. 

### Development

This will clone the repo and start a local repl and run tests

```shell
git clone github.com/MichaelLeachim/akronim;
cd akronim;
tmuxinator . ;
```

