{-# LANGUAGE DeriveGeneric #-}
--SKELETON FILE FOR HANDIN 3 OF COURSEWORK 1 for COMP2209, 2023
--CONTAINS ALL FUNCTIONS REQIURED FOR COMPILATION AGAINST THE TEST SUITE
--MODIFY THE FUNCTION DEFINITIONS WITH YOUR OWN SOLUTIONS
--IMPORTANT : DO NOT MODIFY ANY FUNCTION TYPES
--Julian Rathke, Oct 2023

module Exercises (Expr(..),show,toCNF,resolve,valid) where
import Data.List (nub, delete)


data Expr = Var Char | Not Expr | And [Expr] | Or [Expr] 
   deriving (Eq, Ord)

-- Exercise A7
instance Show Expr where
  show (Var x) = [x]
  show (Not (And xs)) = "~(" ++ show (And xs) ++ ")"
  show (Not (Or xs)) = "~(" ++ show (Or xs) ++ ")"
  show (Not x) = "~" ++ show x
  show (And [x]) = show x
  show (Or [x]) = show x
  show (And (x:xs)) | nested xs = show x ++ " ^ (" ++ show (And xs) ++ ")"
                    | otherwise = show x ++ foldl (\acc e -> acc ++ " ^ " ++ show e) "" xs
  show (Or (x:xs)) | nested xs = show x ++ " v (" ++ show (Or xs) ++ ")"
                   | otherwise = show x ++ foldl (\acc e -> acc ++ " v " ++ show e) "" xs
  show (And []) = "T"
  show (Or []) = "F"

-- Helper function
nested :: [Expr] -> Bool
nested = any isNested
  where
    isNested (Or (_:_:_)) = True
    isNested (And (_:_:_)) = True
    isNested _ = False

-- Exercise A8

toCNF :: Expr -> Expr
toCNF expr 
    | isCNF expr = expr
    | otherwise = checkToCNF . toNNF $ expr

checkToCNF :: Expr -> Expr
checkToCNF (Var e) = And [Or [Var e]]
checkToCNF (Not (Var e)) = And [Or [Not (Var e)]]
checkToCNF (And es) = And [Or [e] | e <- es, isLiteral e]
checkToCNF e = distribute e

-- Negation normal form

toNNF :: Expr -> Expr
toNNF (Not (And es)) = Or (map (toNNF . Not) es)
toNNF (Not (Or es)) = And (map (toNNF . Not) es)
toNNF (And es) = And (map toNNF es)
toNNF (Or es) = Or (map toNNF es)
toNNF (Not (Not e)) = toNNF e
toNNF e = e

-- Distribution law

distribute :: Expr -> Expr
distribute (Or es) = case es of
  [And es1, And es2] -> And [Or [distribute e1, distribute e2] | e1 <- es1, e2 <- es2]
  [e1, And es2] -> And [Or [distribute e1, distribute e2] | e2 <- es2]
  [And es1, e2] -> And [Or [distribute e1, distribute e2] | e1 <- es1]
  _ -> And [Or es]
distribute (And es) = And [distribute e | e <- es]
distribute e = e

-- Testing functions given in the worksheet

isCNF :: Expr -> Bool
isCNF (And es) = all isClause es
isCNF _ = False
    
isClause :: Expr -> Bool
isClause (Or es) = all isLiteral es
isClause _ = False
    
isLiteral :: Expr -> Bool
isLiteral (Var _) = True
isLiteral (Not (Var _)) = True
isLiteral _ = False

-- Exercise A9

resolve :: Expr -> Expr -> Expr
resolve (Or as) (Or bs) = 
    let combined = as ++ bs
        filtered = filter (not . isComplementary combined) combined
    in if length filtered == length combined
      then error "Disjunctions in this expression have no complementary literals"
      else Or (nub filtered)
    where
        isComplementary list expr = any (complements expr) list
        complements (Not x) y = x == y
        complements x (Not y) = x == y
        complements _ _       = False
resolve _ _ = error "Neither argument is a disjunction in this expression"

valid :: [Expr] -> Expr -> Bool
valid [Var e] (Not (Var s)) = False
valid [Not (Var e)] (Var s) = False
valid listOfExpressions givenExpressions = 
    let
        negatedgivenExpressions = toNNF $ Not givenExpressions
        combinedExpressions = listOfExpressions ++ [negatedgivenExpressions]
        cnfExpressions = map toCNF combinedExpressions
        resolvedExpressions = applyResolution cnfExpressions 
    in
        null resolvedExpressions

areComplementary :: Expr -> Expr -> Bool
areComplementary e1 e2 = e1 == negateExpressions e2 || e2 == negateExpressions e1

applyResolution :: [Expr] -> [Expr]
applyResolution expressions = nub [resolve e1 e2 | e1 <- expressions, e2 <- expressions, e1 /= e2, areComplementary e1 e2]

negateExpressions :: Expr -> Expr
negateExpressions (Var x) = Not (Var x)
negateExpressions (Not e) = e
negateExpressions (And es) = Or (map negateExpressions es) 
negateExpressions (Or es) = And (map negateExpressions es)




