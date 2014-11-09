(ns symbolo.core
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :refer :all]
            [clojure.core.logic.fd :as fd]
            [clojure.core.logic.protocols :refer :all]))


;; symbolo and numbero definitions courtesy of David Nolen:
;; https://gist.github.com/swannodette/8876121

(declare numbero*)

(defn symbolo* [x]
  (reify
    IConstraintStep
    (-step [this s]
      (reify
        clojure.lang.IFn
        (invoke [_ s]
          (let [x (walk s x)]
            (when (symbol? x)
              ((remcg this) s))))
        IRunnable
        (-runnable? [_]
          (not (lvar? (walk s x))))))
    IVerifyConstraint
    (-verify [_ a cs]
      (not (some (fn [c] (= (-rator c) `numbero))
                 (map (:cm cs) (get (:km cs) (root-var a x))))))
    IConstraintOp
    (-rator [_] `symbolo)
    (-rands [_] [x])
    IReifiableConstraint
    (-reifyc [c v r s]
      (when-not (lvar? (walk r x))
        `(symbolo ~(-reify s x r))))
    IConstraintWatchedStores
    (-watched-stores [this] #{:clojure.core.logic/subst})))

(defn symbolo [x]
  (cgoal (symbolo* x)))

(defn numbero* [x]
  (reify
    IConstraintStep
    (-step [this s]
      (reify
        clojure.lang.IFn
        (invoke [_ s]
          (let [x (walk s x)]
            (when (number? x)
              ((remcg this) s))))
        IRunnable
        (-runnable? [_]
          (not (lvar? (walk s x))))))
    IVerifyConstraint
    (-verify [_ a cs]
      (not (some (fn [c] (= (-rator c) `symbolo))
                 (map (:cm cs) (get (:km cs) (root-var a x))))))
    IConstraintOp
    (-rator [_] `numbero)
    (-rands [_] [x])
    IReifiableConstraint
    (-reifyc [c v r s]
      (when-not (lvar? (walk r x))
        `(numbero ~(-reify s x r))))
    IConstraintWatchedStores
    (-watched-stores [this] #{:clojure.core.logic/subst})))

(defn numbero [x]
  (cgoal (numbero* x)))


(defn lookupo [x env val]
  (fresh []
    (symbolo x)
    (fresh [y v rest]
      (symbolo y)
      (== [[y v] rest] env)
      (conde
        [(== x y)
         (conde
           [(numbero v)
            (fd/in v val (fd/interval 0 100))
            (fd/== v val)]
           [(symbolo v)
            (== v val)]
           [(fresh [f r]
              (== [f r] v)
              (== v val))])]
        [(!= x y) (lookupo x rest val)]))))

(defn eval-expo [exp env val]
  (fresh []
    (conde
      [(symbolo exp) (lookupo exp env val)]
      [(numbero exp)
       (numbero val)
       (fd/in exp val (fd/interval 0 100))
       (fd/== exp val)]
      [(fresh [x body]
         (== [:lambda [x] body] exp)
         (symbolo x)
         (== [:closure x body env] val))]
      [(fresh [e1 e2 x body env1 v]
         (== [e1 e2] exp)
         (symbolo x)
         (eval-expo e1 env [:closure x body env1])
         (eval-expo e2 env v)
         (eval-expo body [[x v] env1] val))]
      [(fresh [e1 e2 n1 n2]
         (== [:+ e1 e2] exp)
         (numbero n1)
         (numbero n2)
         (numbero val)
         (fd/in n1 n2 val (fd/interval 0 100))
         (fd/+ n1 n2 val)
         (eval-expo e1 env n1)
         (eval-expo e2 env n2))]
      [(fresh [e1 e2 n1 n2]
         (== [:- e1 e2] exp)
         (numbero n1)
         (numbero n2)
         (numbero val)
         (fd/in n1 n2 val (fd/interval 0 100))
         (fd/- n1 n2 val)
         (eval-expo e1 env n1)
         (eval-expo e2 env n2))])))

(defn evalo [exp val]
  (fresh []
    (eval-expo exp [] val)))
