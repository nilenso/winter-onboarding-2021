(ns winter-onboarding-2021.4clojure.yogi)


(defn problem4 [] true)

(defn prob14 [] (list :a :b :c))  

(defn prob5 [] '(1 2 3 4))  

(defn prob6 [] [:a :b :c])

(defn prob7 [] [1 2 3 4])

(defn prob8 [] #{:a :b :c :d})

(defn prob9 [] (conj #{1 4 3} 2))

(defn prob10 [] 20)

(defn prob11 [] (conj {:a 1} {:b 2} [:c 3]))

(defn prob12 [] 3)

(defn prob13 [] [20 30 40])

(defn prob14actual [] 8)

(defn prob15 [x] (* 2 x))

(defn prob16 [x] (clojure.string/join (conj ["Hello, " x, "!"])))

(defn prob17 [] '(6 7 8))


(defn prob18 [] '(6 7))

(defn prob19 [] 5)

(defn prob20 [] 4)

(defn prob21 [x n] 
  ((vec x) n))

(defn prob22 [ip] 
  (reduce 
   (fn [x y] (inc x)) 
   0 
   ip))

(defn prob23 [ip] 
  (reduce 
   (fn [x y] (conj x y)) 
   () 
   ip))

(defn prob24 [ip] 
  (reduce
   +
   ip))

(defn prob25 [ip]
  (filter
   odd?
   ip))

(defn prob26 [x]
  (if 
   (= x 1)
    '(1)
    (if 
     (= x 2) 
      '(1 1)
      (concat  
       (prob26 (- x 1)) 
       (conj () 
        (+ 
         (last (prob26 (- x 1))) 
         (last (prob26(- x 2)))))))))

(defn prob27 [x]
  (=
   (seq x)
   (reverse (seq x))))
