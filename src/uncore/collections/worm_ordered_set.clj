(ns uncore.collections.worm-ordered-set
  "A write-once ordered set. It can only be added to."
  (:refer-clojure :exclude [conj vec contains? into empty map get count])
  (require [clojure.core :as core]))

; A minimal implementation of an ordered set
; in its own namespace for convenience.

; An ordered set that can only be added to, not disj'd from.
(defprotocol IOrderedSet
  (conj [self k])
  (vec [self])
  (contains? [self k]))

(defrecord AOrderedSet [i->k ks]
  IOrderedSet
  (conj [self k]
    (if (core/contains? ks k)
      self
      (AOrderedSet. (core/conj i->k k) (core/conj ks k))))
  (vec [self] i->k)
  (contains? [self k] (core/contains? ks k)))

(def empty (AOrderedSet. [] #{}))

(defn get [os i]
  (core/get (vec os) i))

(defn count [os]
  (core/count (vec os)))

(defn into [os coll]
  (reduce conj os coll))

(defn readonly-ordered-set [& vals]
  (apply into empty vals))

(defn ordered-set [& ks] (into empty ks))

#_(defn map [& args]
  (into empty (apply core/map args)))
