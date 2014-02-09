(ns symbolo.core-test
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :refer :all :exclude [is]])
  (:use clojure.test
        symbolo.core))

(deftest symbolo-numbero-tests
  (testing ""
    
    (is (=
         (run* [q] (symbolo q) (numbero q))
         '()))

    (is (=
         (run* [q] (numbero q) (symbolo q))
         '()))

    (is (=
         (run* [q]
           (fresh [x]
             (numbero x)
             (symbolo x)))
         '()))

    (is (=
         (run* [q]
           (fresh [x]
             (symbolo x)
             (numbero x)))
         '()))

    (is (=
         (run* [q]
           (numbero q)
           (fresh [x]
             (symbolo x)
             (== x q)))
         '()))

    (is (=
         (run* [q]
           (symbolo q)
           (fresh [x]
             (numbero x)
             (== x q)))
         '()))

    (is (=
         (run* [q]
           (fresh [x]
             (numbero x)
             (== x q))
           (symbolo q))
         '()))

    (is (=
         (run* [q]
           (fresh [x]
             (symbolo x)
             (== x q))
           (numbero q))
         '()))

    (is (=
         (run* [q]
           (fresh [x]
             (== x q)
             (symbolo x))
           (numbero q))
         '()))

    (is (=
         (run* [q]
           (fresh [x]
             (== x q)
             (numbero x))
           (symbolo q))
         '()))

    (is (=
         (run* [q]
           (symbolo q)
           (fresh [x]
             (numbero x)))
         '((_0 :- (symbolo.core/symbolo _0)))))

    (is (=
         (run* [q]
           (numbero q)
           (fresh [x]
             (symbolo x)))
         '((_0 :- (symbolo.core/numbero _0)))))

    (is (=
         (run* [q] 
           (fresh [x y]
             (symbolo x)
             (== (list x y) q)))
         '(((_0 _1) :- (symbolo.core/symbolo _0)))))

    (is (=
         (run* [q]    
           (fresh [x y]
             (numbero x)
             (== (list x y) q)))
         '(((_0 _1) :- (symbolo.core/numbero _0)))))

    (is (=
         (run* [q]    
           (fresh [x y]
             (numbero x)
             (symbolo y)
             (== (list x y) q)))
         ;; ideally should be ordered from _0 to _1:
         ;; (symbolo.core/symbolo _1) (symbolo.core/numbero _0)
         '(((_0 _1) :- (symbolo.core/symbolo _1) (symbolo.core/numbero _0)))))

    (is (=
         (run* [q]    
           (fresh [x y]
             (numbero x)
             (== (list x y) q)
             (symbolo y)))
         '(((_0 _1) :- (symbolo.core/symbolo _1) (symbolo.core/numbero _0)))))

    (is (=
         (run* [q]    
           (fresh [x y]
             (== (list x y) q)
             (numbero x)
             (symbolo y)))
         '(((_0 _1) :- (symbolo.core/symbolo _1) (symbolo.core/numbero _0)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (numbero x)
             (symbolo y))
           (fresh [w z]
             (== (list w z) q)))
         '(((_0 _1) :- (symbolo.core/symbolo _1) (symbolo.core/numbero _0)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (numbero x)
             (symbolo y))
           (fresh [w z]
             (== (list w z) q)
             (== w 5)))
         '(((5 _0) :- (symbolo.core/symbolo _0)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (numbero x)
             (symbolo y))
           (fresh [w z]
             (== 'a z)
             (== (list w z) q)))
         '(((_0 a) :- (symbolo.core/numbero _0)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (numbero x)
             (symbolo y))
           (fresh [w z]
             (== (list w z) q)
             (== 'a z)))
         '(((_0 a) :- (symbolo.core/numbero _0)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (!= `(5 a) q)))
         '(((_0 _1) :- (!= (_1 symbolo.core-test/a) (_0 5))))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (!= '(5 a) q)
             (symbolo x)))
         ;; Simplified answer should just be:
         ;;
         ;; (((_0 _1) :- (symbolo.core/symbolo _0) (!= (_1 a) (_0 5))))
         ;;
         ;; since (!= (_1 a) (_0 5)) can never be violated
         '(((_0 _1) :- (symbolo.core/symbolo _0) (!= (_1 a) (_0 5))))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (symbolo x)
             (!= '(5 a) q)))
         ;; Simplified answer should just be:
         ;;
         ;; (((_0 _1) :- (symbolo.core/symbolo _0)))
         '(((_0 _1) :- (symbolo.core/symbolo _0) (!= (_1 a) (_0 5))))))

    (is (=
         (run* [q]
           (fresh [x y]
             (symbolo x)
             (== (list x y) q)
             (!= '(5 a) q)))
         ;; Simplified answer should just be:
         ;;
         ;; (((_0 _1) :- (symbolo.core/symbolo _0)))
         '(((_0 _1) :- (symbolo.core/symbolo _0) (!= (_1 a) (_0 5))))))

    (is (=
         (run* [q]
           (fresh [x y]
             (!= '(5 a) q)
             (symbolo x)
             (== (list x y) q)))
         ;; Simplified answer should just be:
         ;;
         ;; (((_0 _1) :- (symbolo.core/symbolo _0)))         
         '(((_0 _1) :- (symbolo.core/symbolo _0) (!= ((_0 _1) (5 a)))))))

    (is (=
         (run* [q]
           (fresh [x y]
             (!= '(5 a) q)
             (== (list x y) q)
             (symbolo x)))
         ;; Simplified answer should just be:
         ;;
         ;; (((_0 _1) :- (symbolo.core/symbolo _0)))         
         '(((_0 _1) :- (symbolo.core/symbolo _0) (!= ((_0 _1) (5 a)))))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (!= '(5 a) q)
             (numbero y)))
         ;; Simplified answer should just be:
         ;;
         ;; (((_0 _1) :- (symbolo.core/numbero _1)))
         '(((_0 _1) :- (!= (_1 a) (_0 5)) (symbolo.core/numbero _1)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (numbero y)
             (!= '(5 a) q)))
         ;; Simplified answer should just be:
         ;;
         ;; (((_0 _1) :- (symbolo.core/numbero _1)))
         '(((_0 _1) :- (!= (_1 a) (_0 5)) (symbolo.core/numbero _1)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (numbero y)
             (== (list x y) q)
             (!= '(5 a) q)))
         ;; Simplified answer should just be:
         ;;
         ;; (((_0 _1) :- (symbolo.core/numbero _1)))
         '(((_0 _1) :- (!= (_1 a) (_0 5)) (symbolo.core/numbero _1)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (!= '(5 a) q)
             (numbero y)
             (== (list x y) q)))
         ;; Simplified answer should just be:
         ;;
         ;; (((_0 _1) :- (symbolo.core/numbero _1)))
         '(((_0 _1) :- (symbolo.core/numbero _1) (!= ((_0 _1) (5 a)))))))

    (is (=
         (run* [q]
           (fresh [x y]
             (!= '(5 a) q)
             (== (list x y) q)
             (numbero y)))
         ;; Simplified answer should just be:
         ;;
         ;; (((_0 _1) :- (symbolo.core/numbero _1)))
         '(((_0 _1) :- (symbolo.core/numbero _1) (!= ((_0 _1) (5 a)))))))

    (is (=
         (run* [q]
           (fresh [x y]
             (!= (list x y) q)
             (numbero x)
             (symbolo y)))
         ;; Simplified answer should just be:
         ;;
         ;; (_0)
         '((_0 :- (!= (_0 (_1 _2)))))))

    (is (=
         (run* [q]
           (fresh [x y]
             (numbero x)
             (!= (list x y) q)
             (symbolo y)))
         ;; Simplified answer should just be:
         ;;
         ;; (_0)         
         '((_0 :- (!= (_0 (_1 _2)))))))

    (is (=
         (run* [q]
           (fresh [x y]
             (numbero x)
             (symbolo y)
             (!= (list x y) q)))
         ;; Simplified answer should just be:
         ;;
         ;; (_0)         
         '((_0 :- (!= (_0 (_1 _2)))))))
    
    ))
