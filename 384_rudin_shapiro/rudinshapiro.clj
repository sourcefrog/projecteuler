; http://projecteuler.net/problem=384
; Martin Pool, May 2012.

; I wonder; how should we do this?  There are lots of recurrences.  It
; might be easy to compute the whole thing in to a table, and look it up
; from there.  How big would that table be?  We have to go up to F(45).
; Calculating even that recursively is slightly expensive, taking a few
; seconds, and the result is 1,836,311,903.  If we look for the index at
; which 45! occurs 44! times, it's going to be probably infeasibly big.
;
; Computing (a n) (the number of adjacent pairs of ones in the binary
; representation of a) is trivial to describe but I do wonder if it will
; be expensive to do it naively for every a in a large range.  I suppose
; you could speed it with a lookup table of some kind, processing eg one
; byte at a time, but it gets messy.
;
; I wonder if we can skip this and just generate b(n) or s(n) directly?
; On each step, s(n) must go either up by one or down by one, and it
; must generate each k exactly k times.  This might not be sufficiently
; constrained though, or it might be hard to generate from these rules?
; But, could it be as simple as saying: go down if you can, otherwise go
; up?


; Increment a value in to a vector if there is an element there, otherwise add
; at most one element on the end and increment that.
(defn vector-increment [v a]
  (let [lv (count v)]
    (cond 
      (> lv a) (assoc v a (+ (get v a) 1))
      (= lv a) (conj v 1))))

; Generate a lazy sequence of s values.  The sequence has the property
; that every integer k will occur exactly k times, and that every step
; is either down by one or up by one.
(defn generate-s 
  ([] (generate-s (vec '[0 1]) 1))
  ([occurrences sum]
    ;; (println "sum=" sum " occurrences=" occurrences)
    (let [pred (- sum 1)
          go-down (and (> sum 0) (< (get occurrences pred) pred))
          next-sum (+ sum (if go-down -1 +1))
          next-occurrences (vector-increment occurrences next-sum)]
          (lazy-seq (cons sum (generate-s next-occurrences next-sum))))))

(defn fib [n]
  (if (<= n 1) 1
    (+ (fib (- n 1)) (fib (- n 2)))))

(doall (take 20 (generate-s)))

; vim: ft=clojure
