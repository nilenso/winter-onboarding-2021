(ns winter-onboarding-2021.dice-roller.yogi.models
  (:require [winter-onboarding-2021.dice-roller.yogi.operations :as operations]))



(defn init [dice-model]
  (assoc dice-model
         :states (conj (dice-model :states)
                       (take (dice-model :num-dice)
                             (repeatedly #(+ 1
                                             (rand-int (dice-model :num-faces))))))))

(defn build-dice-model [num-faces num-dice operation selector]
  (init {:type :dice-model
         :num-faces num-faces ;6
         :num-dice num-dice   ;3
         :operation operation ;keep
         :selector selector ;2
         :states [nil]}))



(defn build-selector-model [selector-type value]
  {:type selector-type ; literal-x greater-than-x
   :value value ;345 
   })



(defn build-bin-op [left right operator]
  {:type :bin-op
   :left left
   :right right
   :operator operator})

(defn build-literal [x]
  {:type :literal
   :value x})





