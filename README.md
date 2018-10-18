# akronim

Library for doctest like functionality for Clojure programming language

## Usage

### Installation

Add `[thereisnodot/akronim 0.1.0]` to the `project.clj`

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

(:example (meta #'simple-sum)) => 
  ["(simple-sum 3 4)" "=>" "7"] 
  ["(simple-sum 4 5)" "=>" "9"]
```


#### Using with core.test

Here is how to use it in conjunction with testing pipeline. 
For example:

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

#### Using with external documentation tools and customization

Altering docstring with examples. 

There are several built in functions:
* `docstring-template-nothing` will not alter docstring
* `docstring-template-markdown-examples` will implement markdown like examples
* `docstring-template-hljs-examples`  will implement html `hightlight.js` like docs

They can be used like this
```clojure
(require '[thereisnodot.akronim.core :as akronim :refer [defns]))


(reset! akronim/docstring-append akronim/docstring-template-nothing) ;;default
;; This will append examples to the end of the docstring in markdown format
(reset! akronim/docstring-append akronim/docstring-template-markdown-examples) 
;; this will make it in a hljs like format
(reset! akronim/docstring-append akronim/docstring-template-hljs-examples) 
```

##### Writing your own docstring-append

Here is an example implementation: 

```clojure
(require '[thereisnodot.akronim.core :as akronim :refer [defns]))

;; This will append examples to the end of the docstring in 
;; markdown format
(reset! akronim/docstring-append 
  (fn [docstring examples]
    (str docstring "\n"
       "```clojure"
       (apply
        str
        (for [[should _ result] examples]
          (str  should " => " result "\n")))
       "```")))
```

#### Replacing :example to :whatever 

When the meta key should be replace for something else, 
it is possible to do through the following action

```clojure
(require '[thereisnodot.akronim.core :as akronim :refer [defns]))

(reset! akronim/example-key :example) ;; default
(reset! akronim/example-key :whatever-custom) ;; change
```

#### Gotchas

* Right now, `akronim` requires docstring to be present in the generated function. 
* There are tools that assume your function declaration form to be `defn` not
  `defns`. 
  
  
### Development

This will clone the repo and start a local repl and run tests

```shell
git clone github.com/MichaelLeachim/akronim;
cd akronim;
tmuxinator . ;
echo "Happy hacking";
```
