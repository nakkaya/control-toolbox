(ns control-toolbox.math.vector-2d
  (:require [clojure.core.matrix :as cm]))

(defn add [u v]
  (cm/add u v))

(defn sub [u v]
  (cm/sub u v))

(defn mul [u v]
  (cm/mul u v))

(defn to-polar
  ([[x y]]
   (to-polar x y))
  ([x y]
   {:r (Math/sqrt (add (mul x x) (mul y y))) :t (Math/atan2 y x)}))

(defn equal? [[ux uy] [vx vy]]
  (and (= ux vx)
       (= vy uy)))

(defn to-cartesian [r t]
  [(mul r (Math/cos t)) (mul r (Math/sin t))])

(defn negative [[ux uy]]
  [(- ux)
   (- uy)])

(defn magnitude [[ux uy]]
  (Math/sqrt (add (mul ux ux) (mul uy uy))))

(defn dist [u v]
  (magnitude (sub u v)))

(defn dist-manhattan [[ux uy] [vx vy]]
  (add (Math/abs (sub vx ux))
       (Math/abs (sub vy uy))))

(defn normalize [[ux uy]]
  (let [mag (magnitude [ux uy])]
    (if (== mag 0)
      [0 0]
      [(/ ux mag) (/ uy mag)])))

(defn dot-product [[ux uy] [vx vy]]
  (add (mul ux vx) (mul uy vy)))

(defn cross-product [[ax ay] [bx by]]
  (- (* ax by) (* ay bx)))

(defn project [[ux uy] [vx vy]]
  (let [unit-v (normalize [vx vy])
        dot    (dot-product [ux uy] unit-v)]
    (mul unit-v dot)))

(defn rotate [[ux uy] angle]
  (let [s (Math/sin angle)
        c (Math/cos angle)]
    [(sub (mul c ux) (mul s uy))
     (add (mul s ux) (mul c uy))]))

(defn bisect-angle [u v]
  (add (normalize u) (normalize v)))

(defn in-view-cone? [view-pos direction angle point]
  (let [vp (sub point view-pos)
        direction (sub direction view-pos)
        cos (/ (dot-product vp direction)
               (mul (magnitude vp) (magnitude direction)))]
    (> cos (Math/cos angle))))

(defn parallel? [[ux uy] [vx vy]]
  (or (equal? (normalize [ux uy]) (normalize [vx vy]))
      (equal? (normalize [ux uy]) (negative (normalize [vx vy])))))

(defn perpendicular? [u v]
  (= 0.0 (Math/abs (dot-product u v))))

(defn closest-point-on-line [a b c]
  (let [ac (sub c a)
        ab (sub b a)
        proj-mag (dot-product ac (normalize ab))]
    (cond (< proj-mag 0) a
          (> proj-mag (magnitude ab)) b
          :default (add (project ac ab) a))))

(defn cross-track-distance [start end loc]
  (let [xtrack-pt (closest-point-on-line start end loc)]
    (dist xtrack-pt loc)))

(defn closest-point-on-circle [p c r]
  (let [v (normalize (sub p c))]
    (add c (mul v r))))

(defn line-circle-collision 
  "Given line segment AB and circle C with radius R, returns true if 
   circle collides with the line segmen."
  [a b c r]
  (let [closest (closest-point-on-line a b c)
	distance (magnitude (sub c closest))]
    (if (<= distance r)
      true false)))

(defn point-in-circle? 
  "Test if point a falls within the circle c with radius r."
  [[ax ay] [cx cy] r]
  (< (add (Math/pow (sub cx ax) 2) (Math/pow (sub cy ay) 2)) (Math/pow r 2)))

(defn bearing 
  "Direction of u with respect to v."
  [[ux uy] [vx vy]]
  (let [ang (sub (/ Math/PI 2) (Math/atan2 (sub uy vy) (sub ux vx)))] 
    (cond (> ang Math/PI) (sub ang (mul 2 Math/PI))
	  (< ang (- Math/PI)) (add ang (mul 2 Math/PI))
	  :else ang)))

(defn octant
  "Provides info on which octant (1-8) the vector lies in."
  [u]
  (let [[x y] u
        {r :r t :t} (to-polar x y)
        angle (let [a (Math/toDegrees t)]
                (if (< a 0) (add 360 a) a))
        bounds [[1 45] [2 90] [3 135] [4 180] [5 225] [6 270] [7 315] [8 360]]]
    (first (filter #(< angle (second %)) bounds))))

(defn quadrant
  "Provides info on which quadrant (1-4) the vector lies in."
  [u]
  (let [[x y] u
        {:keys [r t]} (to-polar x y)
        angle (let [a (Math/toDegrees t)]
                (if (< a 0) (add 360 a) a))
        bounds [[1 90] [2 180] [3 270] [4 360]]]
    (first (filter #(< angle (second %)) bounds))))

(defn distance-behind-line
  "Given line ab calculate a point c, d distance behind a."
  [a b d]
  (let [[bx by] (sub b a)
        {:keys [r t]} (to-polar bx by)]
    (add a (to-cartesian (- d) t))))

(defn circle-circle-collision
  "Given two circles c1 with radius r1 and c2 with radius r2, return true if circles collide."
  [c1 r1 c2 r2]
  (<= (dist c1 c2) (Math/sqrt (Math/pow (add r1 r2) 2))))

(defn line-intersection
  "Given two line segments ab and cd returns the intersection point if they intersect otherwise nil.
  http://paulbourke.net/geometry/lineline2d/"
  [[x1 y1] [x2 y2] [x3 y3] [x4 y4]]
  (when (and (not= [x1 y1] [x2 y2])
             (not= [x3 y3] [x4 y4]))

    (let [ua (/ (sub (mul (sub x4 x3) (sub y1 y3))
                     (mul (sub y4 y3) (sub x1 x3)))

                (sub (mul (sub y4 y3) (sub x2 x1))
                     (mul (sub x4 x3) (sub y2 y1))))

          ub (/ (sub (mul (sub x2 x1) (sub y1 y3))
                     (mul (sub y2 y1) (sub x1 x3)))

                (sub (mul (sub y4 y3) (sub x2 x1))
                     (mul (sub x4 x3) (sub y2 y1))))]

      (when (and (>= ua 0)
                 (<= ua 1)
                 (>= ub 0)
                 (<= ub 1))

        [(add x1 (mul ua (sub x2 x1)))
         (add y1 (mul ua (sub y2 y1)))]))))

(defn- in-range? [x a b]
  (cond (< x a) false
        (> x b) false
        :else true))

(defn point-in-rectangle
  "Given corners of a rectangle a,b,c,d and a point p,
    returns true if p in the rectangle,"
  [a b c d [px py]]
  (let [min-x (apply min (map first [a b c d]))
        max-x (apply max (map first [a b c d]))
        min-y (apply min (map second [a b c d]))
        max-y (apply max (map second [a b c d]))]
    (and (in-range? px min-x max-y)
         (in-range? py min-y max-y))))

(defn rectangle-circle-collision
  "Given corners of a rectangle a,b,c,d and a circle with
    center cc with radius r return true if circle collides with the
    rectangle."
  [[a b c d] cc r]
  (or (point-in-rectangle a b c d cc)
      (line-circle-collision a b cc r)
      (line-circle-collision b c cc r)
      (line-circle-collision c d cc r)
      (line-circle-collision d a cc r)))

(defn angle-between-points [[ux uy] [vx vy]]
  (let [det (- (* ux vy) (* uy vx))
        dot (+ (* ux vx) (* uy vy))]
    (Math/atan2 det dot)))
