(ns winter-onboarding-2021.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(foo "")

(+ 1 1)

(print (str 1 2 3))

(print (+ 1 3) (+ 1 2 3) 4)

(defn selfie
  "Given a function `f`, return the result of
  applying `f` to itself."
  [f]
  (f f))

(= 42
   (identity 42)
   ((selfie identity) 42)
   ((selfie (selfie identity)) 42)
   ((selfie (selfie (selfie identity))) 42)) ; ad-infinitum
