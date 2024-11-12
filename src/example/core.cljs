(ns example.core)

(defn ^:export foo []
    (js/console.log "This is cljs printing")
    "Bar")
