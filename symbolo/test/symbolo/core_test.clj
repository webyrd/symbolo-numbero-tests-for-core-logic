(ns symbolo.core-test
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :refer :all :exclude [is]]
            [clojure.core.logic.fd :as fd])
  (:use clojure.test
        symbolo.core))

(deftest disequality-tests
  (testing ""

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (!= '(5 a) q)))
         '(((_0 _1) :- (!= (_1 a) (_0 5))))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list y x) q)
             (!= '(5 a) q)))
         '(((_0 _1) :- (!= (_1 a) (_0 5))))))

    (is (=
         (run* [q]
           (fresh [x y]
             (!= (list x y) q)))
         ;; Simplified answer should just be:
         ;;
         ;; (_0)
         ;;
         ;; There is no way to violate this constraint, since neither
         ;; _1 nor _2 is reified.  Both would need to be reified to be
         ;; able to violate the constraint.
         '((_0 :- (!= (_0 (_1 _2)))))))
    
    ))

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
         ;; (((_0 _1) :- (symbolo.core/symbolo _0)))
         ;;
         ;; since (!= (_1 a) (_0 5)) can never be violated
         '(((_0 _1) :- (!= (_1 a) (_0 5)) (symbolo.core/symbolo _0)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (== (list x y) q)
             (symbolo x)
             (!= '(5 a) q)))
         ;; Simplified answer should just be:
         ;;
         ;; (((_0 _1) :- (symbolo.core/symbolo _0)))
         '(((_0 _1) :- (!= (_1 a) (_0 5)) (symbolo.core/symbolo _0)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (symbolo x)
             (== (list x y) q)
             (!= '(5 a) q)))
         ;; Simplified answer should just be:
         ;;
         ;; (((_0 _1) :- (symbolo.core/symbolo _0)))
         '(((_0 _1) :- (!= (_1 a) (_0 5)) (symbolo.core/symbolo _0)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (!= '(5 a) q)
             (symbolo x)
             (== (list x y) q)))
         ;; Simplified answer should just be:
         ;;
         ;; (((_0 _1) :- (symbolo.core/symbolo _0)))
         '(((_0 _1) :- (!= ((_0 _1) (5 a))) (symbolo.core/symbolo _0)))))

    (is (=
         (run* [q]
           (fresh [x y]
             (!= '(5 a) q)
             (== (list x y) q)
             (symbolo x)))
         ;; Simplified answer should just be:
         ;;
         ;; (((_0 _1) :- (symbolo.core/symbolo _0)))
         '(((_0 _1) :- (!= ((_0 _1) (5 a))) (symbolo.core/symbolo _0)))         
         ))

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

(deftest fd-tests
  (testing "bustado??"

    (is (=
         (run* [q]
           (fd/in q (fd/interval 0 10)))
         '(0 1 2 3 4 5 6 7 8 9 10)))

;;     (is (=
;;          (run* [q]
;;            (fd/in q (fd/interval 0 10))
;;            (fd/== [:foo] q))
;;          '([:foo])))
    
;;     (is (=
;;          (run* [q]
;;            (fd/== [:foo] q)
;;            (fd/in q (fd/interval 0 10)))
;;          '([:foo])))

;;     (is (=
;;          (run* [q]
;;            (fd/in q (fd/interval 0 10))
;;            (== [:foo] q))
;;          '([:foo])))
    
;;     (is (=
;;          (run* [q]
;;            (== [:foo] q)
;;            (fd/in q (fd/interval 0 10)))
;;          '([:foo])))   
    
     ))

(deftest eval-expo-tests
  (testing ""

    (is (=
         (run* [q] (eval-expo 7 [] q))
         '(7)))
    
;    (is (=
;         (run 50 [exp] (eval-expo exp [] 6))
;         '???))

    (is (=
         (run 1 [exp val]
           (fresh [x body]
             (== [:lambda [x] body] exp)
             (symbolo x)             
             (eval-expo exp [] val)))
         '(([[:lambda [_0] _1] [:closure _0 _1 []]] :- (symbolo.core/symbolo _0)))))

    (is (=
         (run 1 [exp val]
           (fresh [x y body]
             (== [:lambda [x] [:lambda [y] body]] exp)
             (symbolo x)
             (symbolo y)
             (eval-expo exp [] val)))
         '(([[:lambda [_0] [:lambda [_1] _2]] [:closure _0 [:lambda [_1] _2] []]] :- (symbolo.core/symbolo _1) (symbolo.core/symbolo _0)))))

    (is (=
         (run 1 [exp]
           (fresh [x y body]
             (== [:lambda [x] [:lambda [y] body]] exp)
             (symbolo x)
             (symbolo y)
             (eval-expo [[exp 1] 2] [] 4)))
         '(([:lambda [_0] [:lambda [_1] 4]] :- (symbolo.core/symbolo _1) (symbolo.core/symbolo _0)))))

    (is (=
         (run 1 [exp]
           (fresh [x y body]
             (== [:lambda [x] [:lambda [y] body]] exp)
             (symbolo x)
             (symbolo y)
             (eval-expo [[exp 1] 2] [] 4)
             (eval-expo [[exp 1] 3] [] 5)))
         '(([:lambda [_0] [:lambda [_1] [:+ 2 _1]]] :- (symbolo.core/symbolo _0) (symbolo.core/symbolo _1)))))

    (is (=
         (run 1 [exp]
           (fresh [x y body]
             (== [:lambda [x] [:lambda [y] body]] exp)
             (symbolo x)
             (symbolo y)
             (eval-expo [[exp 1] 2] [] 4)
             (eval-expo [[exp 1] 3] [] 5)
             (eval-expo [[exp 2] 3] [] 7)))
         '???))
    
    ))
