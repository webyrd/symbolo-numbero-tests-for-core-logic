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
         '((_.0 (sym _.0)))))

    (is (=
         (run* [q]
           (numbero q)
           (fresh [x]
             (symbolo x)))
         '((_.0 (num _.0)))))

    (is (=
         (run* [q] 
           (fresh [x y]
             (symbolo x)
             (== (list x y) q)))
         '(((_.0 _.1) (sym _.0)))))

    (is (=
         (run* [q]    
           (fresh [x y]
             (numbero x)
             (== (list x y) q)))
         '(((_.0 _.1) (num _.0)))))

    (is (=
         (run* [q]    
           (fresh [x y]
             (numbero x)
             (symbolo y)
             (== (list x y) q)))
         '(((_.0 _.1) (num _.0) (sym _.1)))))

    (is (=
         (run* [q]    
           (fresh [x y]
             (numbero x)
             (== (list x y) q)
             (symbolo y)))
         '(((_.0 _.1) (num _.0) (sym _.1)))))

    (is (=
         (run* [q]    
           (fresh [x y]
             (== (list x y) q)
             (numbero x)
             (symbolo y)))
         '(((_.0 _.1) (num _.0) (sym _.1)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (numbero x)
             (symbolo y))
           (fresh [w z]
             (== (list w z) q)))
         '(((_.0 _.1) (num _.0) (sym _.1)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (numbero x)
             (symbolo y))
           (fresh [w z]
             (== (list w z) q)
             (== w 5)))
         '(((5 _.0) (sym _.0)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (numbero x)
             (symbolo y))
           (fresh [w z]
             (== 'a z)
             (== (list w z) q)))
         '(((_.0 a) (num _.0)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (numbero x)
             (symbolo y))
           (fresh [w z]
             (== (list w z) q)
             (== 'a z)))
         '(((_.0 a) (num _.0)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (!= `(5 a) q)))
         '(((_.0 _.1) (!= ((_.0 5) (_.1 a)))))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (!= '(5 a) q)
             (symbolo x)))
         '(((_.0 _.1) (sym _.0)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (symbolo x)
             (!= '(5 a) q)))
         '(((_.0 _.1) (sym _.0)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (symbolo x)
             (== (list x y) q)
             (!= '(5 a) q)))
         '(((_.0 _.1) (sym _.0)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (!= '(5 a) q)
             (symbolo x)
             (== (list x y) q)))
         '(((_.0 _.1) (sym _.0)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (!= '(5 a) q)
             (== (list x y) q)
             (symbolo x)))
         '(((_.0 _.1) (sym _.0)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (!= '(5 a) q)
             (numbero y)))
         '(((_.0 _.1) (num _.1)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (numbero y)
             (!= '(5 a) q)))
         '(((_.0 _.1) (num _.1)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (numbero y)
             (== (list x y) q)
             (!= '(5 a) q)))
         '(((_.0 _.1) (num _.1)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (!= '(5 a) q)
             (numbero y)
             (== (list x y) q)))
         '(((_.0 _.1) (num _.1)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (!= '(5 a) q)
             (== (list x y) q)
             (numbero y)))
         '(((_.0 _.1) (num _.1)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (!= (list x y) q)
             (numbero x)
             (symbolo y)))
         '(_.0)))

    (is (=
         (run* [q]
           (fresh [x y]
             (numbero x)
             (!= (list x y) q)
             (symbolo y)))
         '(_.0)))

    (is (=
         (run* [q]
           (fresh [x y]
             (numbero x)
             (symbolo y)
             (!= (list x y) q)))
         '(_.0)))
    
    ))
