(ns winter-onboarding-2021.4clojure.govind)

(defn problem1 [] true)

(defn problem2 [] 4)

(defn problem3 [] "HELLO WORLD")

;; nth element in a list = n-1th elem in rest of the list
(defn problem21 [elems n]
  (if (= n 0)
    (first elems)
    (problem21 (rest elems) (dec n))))

(defn problem39 [[f1 & rest1] [f2 & rest2]]
  (if (or (nil? f1) (nil? f2))
    '()
    (conj (problem39 rest1 rest2) f2 f1)))

;; [1 2 3]
;; 5
;; [1] [2] [3] [] []
;; nil
;; 


;; acc => [['() '() '()] i]
(defn problem43 [elems n]
  (let [fx
        (fn [[deinterleaved-list index] elem]
          (let [i (rem index n)
                list-at-i (get deinterleaved-list i)]
            [(assoc deinterleaved-list
                    i
                    (conj (vec list-at-i) elem))
             (inc index)]))]
    (first (reduce fx
                   [[] 0]
                   elems))))

(defn problem44 [rotation elems]
  (let [index (mod (+ rotation (count elems))
                   (count elems))
        [begining end] (split-at index elems)]
    (concat end begining)))

(problem44 6 [1 2 3 4 5])


(defn problem53 [elems]
  (let [fx
        (fn [{:keys [longest current]
              :as   acc} elem]
          (let [new-current
                (if (or (empty? current)
                        (> elem (last current)))
                  (conj current elem)
                  [elem])]
            (assoc acc
                   :current new-current
                   :longest (if (> (count new-current) 
                                   (count longest))
                              new-current
                              longest))))
        
        longest-subseq 
        (:longest
         (reduce fx
                 {:longest []
                  :current []}
                 elems))]
    (if (< 1 (count longest-subseq))
      longest-subseq
      [])))

(problem53 [1 2  5 3 4 ])