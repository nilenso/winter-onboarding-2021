(ns winter-onboarding-2021.dice-roller.yogi.models
  (:require [winter-onboarding-2021.dice-roller.yogi.operations :as operations]))



;3d6k2 + 5d10k2
(defn build-dice-model [num-faces num-dice operation selector]
  {:num-faces num-faces ;6
   :num-dice num-dice   ;3
   :operation operation ;keep
   :selector selector ;2
   :states [nil]})



(defn build-selector-model [selector-type value]
  {:type selector-type ; literal-x greater-than-x
   :value value ;345 
})

