;; @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
;; @ Copyright (c) Michael Leahcim                                                      @
;; @ You can find additional information regarding licensing of this work in LICENSE.md @
;; @ You must not remove this notice, or any other, from this software.                 @
;; @ All rights reserved.                                                               @
;; @@@@@@ At 2018-10-18 23:40 <thereisnodotcollective@gmail.com> @@@@@@@@@@@@@@@@@@@@@@@@

(ns thereisnodot.akronim.templates)

(defn docstring-template-nothing
  [docstring examples] docstring)
(defn docstring-template-text
  [docstring examples]
  (str docstring "\n\n"
       (apply
        str
        (for [[should _ result] (partition 3 examples)]
          (str  should " => " result "\n")))))

(defn docstring-template-markdown
  [docstring examples]
  (str docstring "\n\n"
       "```clojure\n\n"
       (apply
        str
        (for [[should _ result] (partition 3 examples)]
          (str  should " => " result "\n")))
       "\n```"))

(defn docstring-template-hljs
  [docstring examples]
  (str docstring "</br>"
       "<pre><code class='hljs clojure'>\n"
       (apply
        str
        (for [[should _ result] (partition 3 examples)]
          (str  should " => " result "\n")))
       "</code></pre>"))
