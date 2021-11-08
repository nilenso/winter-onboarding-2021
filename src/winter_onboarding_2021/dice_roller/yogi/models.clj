(ns winter-onboarding-2021.dice-roller.yogi.models
  (:require [winter-onboarding-2021.dice-roller.yogi.operations :as operations]))



;3d6k2 + 5d10k2
(defn build-dice-model [num-faces num-dice operation selector]
  {
   :num-faces num-faces ;6
   :num-dice num-dice   ;3
   :operation operation ;keep
   :selector selector ;2
   :states [{:current-state '(3 2 1)
             :operation :reroll
             :selector {:type :greather-than
                        :value 5}}
            {:current-state '(4 1 1)
             :operation :reroll
             :selector {:type :greather-than
                        :value 5}}
            {:current-state '(4)
             :operation :keep
             :selector {:type :greather-than
                        :value 1}}]})

('(1 2 3 4 5))
;COSt of change


(defn build-selector-model [selector-type value]
  {:type selector-type ; literal-x greater-than-x
   :value value ;345 
   })

