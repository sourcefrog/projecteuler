; https://projecteuler.net/problem=11
; Martin Pool, May 2012

; In the 2020 grid below, four numbers along a diagonal line have been
; marked in red.
; The product of these numbers is 26  63  78  14 = 1788696.

; What is the greatest product of four adjacent numbers in any direction (up,
; down, left, right, or diagonally) in the 2020 grid?

(use '[clojure.string :only (blank? split)])
(use '[clojure.pprint :only (pprint)])

(def grid-contents-string "
08 02 22 97 38 15 00 40 00 75 04 05 07 78 52 12 50 77 91 08
49 49 99 40 17 81 18 57 60 87 17 40 98 43 69 48 04 56 62 00
81 49 31 73 55 79 14 29 93 71 40 67 53 88 30 03 49 13 36 65
52 70 95 23 04 60 11 42 69 24 68 56 01 32 56 71 37 02 36 91
22 31 16 71 51 67 63 89 41 92 36 54 22 40 40 28 66 33 13 80
24 47 32 60 99 03 45 02 44 75 33 53 78 36 84 20 35 17 12 50
32 98 81 28 64 23 67 10 26 38 40 67 59 54 70 66 18 38 64 70
67 26 20 68 02 62 12 20 95 63 94 39 63 08 40 91 66 49 94 21
24 55 58 05 66 73 99 26 97 17 78 78 96 83 14 88 34 89 63 72
21 36 23 09 75 00 76 44 20 45 35 14 00 61 33 97 34 31 33 95
78 17 53 28 22 75 31 67 15 94 03 80 04 62 16 14 09 53 56 92
16 39 05 42 96 35 31 47 55 58 88 24 00 17 54 24 36 29 85 57
86 56 00 48 35 71 89 07 05 44 44 37 44 60 21 58 51 54 17 58
19 80 81 68 05 94 47 69 28 73 92 13 86 52 17 77 04 89 55 40
04 52 08 83 97 35 99 16 07 97 57 32 16 26 26 79 33 27 98 66
88 36 68 87 57 62 20 72 03 46 33 67 46 55 12 32 63 93 53 69
04 42 16 73 38 25 39 11 24 94 72 18 08 46 29 32 40 62 76 36
20 69 36 41 72 30 23 88 34 62 99 69 82 67 59 85 74 04 36 16
20 73 35 29 78 31 90 01 74 31 49 71 48 86 81 16 23 57 05 54
01 70 54 71 83 51 54 69 16 92 33 48 61 43 52 01 89 19 67 48
")


; Throughout this program, cells are named as (row column), starting
; from 0.  Ranges or areas are named closed on the left side, like the
; Clojure range function: so the whole board is in (0 0) - (20 20)

(def grid-width 20)
(def run-length 4)

(def grid-nums
  (map #(Integer/parseInt %) 
       (remove blank?
	(split grid-contents-string #"\s+"))))
     
; chop into a series of rows
(def grid-rows
  (vec (map vec (partition grid-width grid-width grid-nums))))
(def grid-height (count grid-rows))

; Because we're only looking at the product of sets of four numbers in
; each direction, it doesn't matter whether we traverse eg west-east or
; east-west.  So there are four directions we need to look at: e-w, n-s,
; sw-ne, se-nw.

(defn from-grid 
  [[row col]]
  (nth (nth grid-rows row) col))

; Given a point and a direction, produce a list of run-length points in
; that direction.
(defn steps-from 
  [[r c] [dr dc]] 
  (map #(list (+ r (* %1 dr)) (+ c (* %1 dc))) 
       (range run-length))) 

; Given two seqs, produce a list of every element of seqa combined in every
; combination with every element of seqb.
(defn combinations [seqa seqb]
  (mapcat (fn [a] (map (fn [b] (list a b)) seqb)) seqa))

; List of all points in a rectangular area
(defn
  points-in-area
  [r1 r2 c1 c2]
  (combinations (range r1 r2) (range c1 c2)))

; Produce a list of runs originating at each point in a particular area,
; and continuing in direction (dr dc) for run-length.  The area must be such
; that all the runs from it stay in the board.
(defn 
  runs-from-area
  [[r1 r2 c1 c2] [dr dc]]
  (map #(steps-from %1 (list dr dc))
       (points-in-area r1 r2 c1 c2))) 

; Calculate the rectangle from which we can calculate runs in a given
; direction while staying on the board.  For runs in a negative direction, 
; we can start at (- run-length 1), producing eg [3 2 1 0].  For 
; positive runs we can start at (- grid-width setback) producing 
; eg [17 18 19 20].  nb the rectangles, like all ranges here, are closed on
; the left side, so it's correct that the upper bounds run up to 20.
(defn
  valid-origin-for-direction
  [dr dc]
  (let [setback (- run-length 1)]
    [(if (neg? dr) setback 0)
     (if (pos? dr) (- grid-height setback) grid-height)
     (if (neg? dc) setback 0)
     (if (pos? dc) (- grid-width setback) grid-width)]))

(defn
  runs-in-direction [[dr dc]]
  (runs-from-area (valid-origin-for-direction dr dc) [dr dc]))

; All interesting runs
(def valid-runs
  (mapcat runs-in-direction [[0 1] [1 0] [1 1] [-1 1]]))

(defn product-of-run
  [run-points]
  (reduce * (map from-grid run-points)))

(def overall-best
  (reduce max (map product-of-run valid-runs)))

(println overall-best)

; vim: ft=clojure
