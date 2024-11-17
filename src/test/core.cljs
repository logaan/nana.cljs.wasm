(ns test.core
 (:require 
    [cljs.pprint :refer [pprint]]
    [nana.core :as nana]))

(defn ^:export record-read-test []
    (let [src      "Record package { name string }"
          expected [(nana/->MacroName "Record")
                    (nana/->Name "package")
                    (nana/->Map {(nana/->String "name")
                                 (nana/->String "string")})]
          actual   (nana/read src)]
          (= expected actual)))

(js/console.log
  (if (record-read-test)
    "Pass"
    "Fail"))
