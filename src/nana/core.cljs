(ns nana.core
  (:require
    [cljs.pprint :refer [pprint]]
    [jasentaa.monad :as m]
    [jasentaa.position :refer [strip-location]]
    [jasentaa.parser :refer [parse-all]]
    [jasentaa.parser.basic :as b]
    [jasentaa.parser.combinators :as c]))

(enable-console-print!)

(def upper-case-letter (b/from-re #"[A-Z]"))
(def lower-case-letter (b/from-re #"[a-z]"))
(def letter (c/any-of upper-case-letter lower-case-letter))
(def lower-case-word (c/and-then lower-case-letter (c/plus letter)))
(def title-case-word (c/and-then upper-case-letter (c/plus letter)))

(defn ^:export puppy []
  (js/console.log "Nana core puppy")
  "puppy")

(defrecord Record [name params])
(defrecord MacroName [name])
(defrecord Name [name])
(defrecord Map [value])
(defrecord String [text])
  
(def macro-name
  (m/do* 
    (name <- (c/token title-case-word))
    (m/return (->MacroName (strip-location name)))))
  
(def nname
  (m/do* 
    (name <- (c/token lower-case-word))
    (m/return (->Name (strip-location name)))))

(def name-type-pair
  (m/do*
    (name <- (c/token lower-case-word))
    (type <- (c/token lower-case-word))
    (m/return [(->Name (strip-location name))
               (->Name (strip-location type))])))

(def nmap
  (m/do*
    (c/symb "{")
    (pairs <- (c/many name-type-pair))
    (c/symb "}")
    (m/return (->Map (into {} pairs)))))

(def expressions
  (c/many (c/any-of macro-name nname nmap)))

(defn read [src]
  (let [result (parse-all expressions src)]
    (println result)
    (pprint result)
    (js/console.log (clj->js result))
    result))
