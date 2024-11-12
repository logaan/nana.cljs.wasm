(ns test.core
 (:require [nana.core :as nana]))

(defn ^:export kitten []
    (nana/puppy)
    (js/console.log "Test core kitten")
    "kitten")
