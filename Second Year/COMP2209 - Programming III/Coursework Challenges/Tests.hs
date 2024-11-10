import Test.HUnit
import Challenges

-- CHALLENGE 1:

-- Helper function to create a test case for Challenge 1
testCaseCreatorChallenge1 :: String -> Puzzle -> Bool -> Test
testCaseCreatorChallenge1 name input expected = TestLabel name $ TestCase (assertEqual name expected (isPuzzleComplete input))

-- Test cases for Challenge 1
challenge1Test1 = testCaseCreatorChallenge1 "3x3, at least one source, at least one sink, wires not connected"
  [ [ Wire [North,West] , Wire [North,South] , Source [North] ], [ Wire [North,West], Wire [East,West], Wire [North,East] ], [ Sink [West] , Wire [North,South] , Wire [North,West] ] ]
  False

challenge1Test2 = testCaseCreatorChallenge1 "3x3, no source, at least one sink, wires not connected"
  [ [ Wire [North,West] , Wire [North,South] , Wire [North] ], [ Wire [North,West], Wire [East,West], Wire [North,East] ], [ Sink [West] , Wire [North,South] , Wire [North,West] ] ]
  False

challenge1Test3 = testCaseCreatorChallenge1 "3x3, at least one source, no sink, wires not connected"
  [ [ Wire [North,West] , Wire [North,South] , Source [North] ], [ Wire [North,West], Wire [East,West], Wire [North,East] ], [ Wire [West] , Wire [North,South] , Wire [North,West] ] ]
  False

challenge1Test4 = testCaseCreatorChallenge1 "3x3, no source, no sink, wires not connected"
  [ [ Wire [North,West] , Wire [North,South] , Wire [North] ], [ Wire [North,West], Wire [East,West], Wire [North,East] ], [ Wire [West] , Wire [North,South] , Wire [North,West] ] ]
  False

challenge1Test5 = testCaseCreatorChallenge1 "3x3, at least one source, at least one sink, wires connected"
  [ [ Wire [East,South] , Wire [East,West] , Source [West] ], [ Wire [North,East], Wire [East,West], Wire [West,South] ], [ Sink [East] , Wire [West,East] , Wire [North,West] ] ]
  True

challenge1Test6 = testCaseCreatorChallenge1 "3x3, at least one source, at least one sink but not connected, wires connected"
  [ [ Wire [East,South] , Wire [East,West] , Source [West] ], [ Wire [North,East], Wire [East,West], Wire [West,South] ], [ Sink [North] , Wire [West,East] , Wire [North,West] ] ]
  False

challenge1Test7 = testCaseCreatorChallenge1 "3x3, at least one source but not connected, at least one sink, wires connected" 
  [ [ Wire [East,South] , Wire [East,West] , Source [North] ], [ Wire [North,East], Wire [East,West], Wire [West,South] ], [ Sink [East] , Wire [West,East] , Wire [North,West] ] ]
  False

challenge1Test8 = testCaseCreatorChallenge1 "3x2, two sources, two sinks, wires connected, two seperate connected puzzles"
  [ [ Sink [East] , Wire [East,West] , Source [West] ], [ Source [East] , Wire [West,East] , Sink [West] ] ]
  True

challenge1Test9 = testCaseCreatorChallenge1 "3x1, source, sink, wires connected, most basic"
  [ [ Sink [East] , Wire [East,West] , Source [West] ] ]
  True

challenge1Test10 = testCaseCreatorChallenge1 "loose wire"
  [ [ Wire [East,West] ] ]
  False

challenge1Test11 = testCaseCreatorChallenge1 "2x2 wires connected but no source or sink"
  [ [ Wire [South,East] , Wire [West, South] ], [ Wire [North,East] , Wire [West, North] ] ]
  False

challenge1Test12 = testCaseCreatorChallenge1 "source connected to sink through sink (acts as wires)"
  [ [ Source [East] , Sink [West, East], Sink [West] ] ]
  True

challenge1Test13 = testCaseCreatorChallenge1 "multiple wires for sink and source"
  [ [ Sink [South,East] , Wire [West, South] ], [ Wire [North,East] , Source [West, North] ] ]
  True

challenge1Test14 = testCaseCreatorChallenge1 "wire pointing outside of the grid"
  [ [ Wire [East,South] , Wire [East,West] , Source [West] ], [ Wire [North,East, West], Wire [East,West], Wire [West,South] ], [ Sink [East] , Wire [West,East] , Wire [North,West] ] ]
  False

-- Group all tests together for Challenge 1
groupTestChallenge1 = TestList [challenge1Test1, challenge1Test2, challenge1Test3, challenge1Test4, challenge1Test5, challenge1Test6, challenge1Test7, challenge1Test8, challenge1Test9, challenge1Test10, challenge1Test11, challenge1Test12, challenge1Test13, challenge1Test14]

-- Run tests for Challenge 1
mainChallenge1 :: IO Counts
mainChallenge1 = runTestTT groupTestChallenge1

-- CHALLENGE 2:

-- Helper function to create a test case for Challenge 2
testCaseCreatorChallenge2 :: String -> Puzzle -> Maybe [ [ Rotation ] ] -> Test
testCaseCreatorChallenge2 name input expected = TestLabel name $ TestCase (assertEqual name expected (solveCircuit input))

-- Test cases for Challenge 2
challenge2Test1 = testCaseCreatorChallenge2 "Vertically R90, R0, R90 in task example" [ [ Wire [North,West] , Wire [North,South] , Source [North] ], [ Wire [North,West], Wire [East,West], Wire [North,East] ], [ Sink [West] , Wire [North,South] , Wire [North,West] ] ] (Just [[R180,R90,R270],[R90,R0,R180],[R180,R90,R0]])

challenge2Test2 = testCaseCreatorChallenge2 "Vertically R90, R0, R270 in task example" [ [ Wire [North,West] , Wire [North,South] , Source [North] ], [ Wire [North,West], Wire [East,West], Wire [North,East] ], [ Sink [West] , Wire [North,South] , Wire [North,West] ] ] (Just [[R180,R90,R270], [R90,R0,R180], [R180,R270,R0]])

challenge2Test3 = testCaseCreatorChallenge2 "Vertically R90, R180, R90 in task example" [ [ Wire [North,West] , Wire [North,South] , Source [North] ], [ Wire [North,West], Wire [East,West], Wire [North,East] ], [ Sink [West] , Wire [North,South] , Wire [North,West] ] ] (Just [[R180,R90,R270], [R90,R180,R180], [R180,R90,R0]])

challenge2Test4 = testCaseCreatorChallenge2 "Vertically R90, R180, R270 in task example" [ [ Wire [North,West] , Wire [North,South] , Source [North] ], [ Wire [North,West], Wire [East,West], Wire [North,East] ], [ Sink [West] , Wire [North,South] , Wire [North,West] ] ] (Just [[R180,R90,R270], [R90,R180,R180], [R180,R270,R0]])

challenge2Test5 = testCaseCreatorChallenge2 "Vertically R270, R0, R90 in task example" [ [ Wire [North,West] , Wire [North,South] , Source [North] ], [ Wire [North,West], Wire [East,West], Wire [North,East] ], [ Sink [West] , Wire [North,South] , Wire [North,West] ] ] (Just [[R180,R270,R270], [R90,R0,R180], [R180,R90,R0]])

challenge2Test6 = testCaseCreatorChallenge2 "Vertically R270, R0, R270 in task example" [ [ Wire [North,West] , Wire [North,South] , Source [North] ], [ Wire [North,West], Wire [East,West], Wire [North,East] ], [ Sink [West] , Wire [North,South] , Wire [North,West] ] ] (Just [[R180,R270,R270], [R90,R0,R180], [R180,R270,R0]])

challenge2Test7 = testCaseCreatorChallenge2 "Vertically R270, R180, R90 in task example" [ [ Wire [North,West] , Wire [North,South] , Source [North] ], [ Wire [North,West], Wire [East,West], Wire [North,East] ], [ Sink [West] , Wire [North,South] , Wire [North,West] ] ] (Just [[R180,R270,R270], [R90,R180,R180], [R180,R90,R0]])

challenge2Test8 = testCaseCreatorChallenge2 "Vertically R270, R180, R270 in task example" [ [ Wire [North,West] , Wire [North,South] , Source [North] ], [ Wire [North,West], Wire [East,West], Wire [North,East] ], [ Sink [West] , Wire [North,South] , Wire [North,West] ] ] (Just [[R180,R270,R270], [R90,R180,R180], [R180,R270,R0]])

challenge2Test9  = testCaseCreatorChallenge2 "Missing wire tile should return Nothing" [ [ Wire [North,West] , Source [North] ], [ Wire [North,West], Wire [East,West], Wire [North,East] ], [ Sink [West] , Wire [North,South] , Wire [North,West] ] ] Nothing

challenge2Test10 = testCaseCreatorChallenge2 "No rotation needed" [ [ Wire [East,South] , Wire [East,West] , Source [West] ], [ Wire [North,East], Wire [East,West], Wire [West,South] ], [ Sink [East] , Wire [West,East] , Wire [North,West] ] ] (Just [[R0,R0,R0],[R0,R0,R0],[R0,R0,R0]])

-- Group all tests together for Challenge 2
groupTestChallenge2 = TestList [challenge2Test1, challenge2Test2, challenge2Test3, challenge2Test4, challenge2Test5, challenge2Test6, challenge2Test7, challenge2Test8, challenge2Test9, challenge2Test10]

-- Run tests for Challenge 2
mainChallenge2 :: IO Counts
mainChallenge2 = runTestTT groupTestChallenge2

-- CHALLENGE 3:

-- Helper function to create a test case for Challenge 3
testCaseCreatorChallenge3 :: String -> LExpr -> String -> Test
testCaseCreatorChallenge3 name input expected = TestLabel name $ TestCase (assertEqual name expected (prettyPrint input))

-- Test cases for Challenge 3

challenge3Test1 = testCaseCreatorChallenge3 "Task example 1" (App (Abs (V 1) (Var 1)) (Abs (V 1) (Var 1))) "(\\x1 -> x1) \\x1 -> x1"

challenge3Test2 = testCaseCreatorChallenge3 "Task example 2" (Let Discard (Var 0) (Abs (V 1) (App (Var 1) (Abs (V 1) (Var 1)))) ) "let _ = x0 in \\x1 -> x1 \\x1 -> x1"

challenge3Test3 = testCaseCreatorChallenge3 "Task example 3" (Abs (V 1) (Abs Discard (Abs (V 2) (App (Var 2 ) (Var 1 ) ) ) ) ) "\\x1 _ x2 -> x2 x1"

challenge3Test4 = testCaseCreatorChallenge3 "Task example 4" (App (Var 2) (Abs (V 1) (Abs Discard (Var 1)))) "x2 \\x1 _ -> x1"

challenge3Test5 = testCaseCreatorChallenge3 "Simple Case" (Var 1) "x1"

challenge3Test6 = testCaseCreatorChallenge3 "Simple Case" (App (Var 1) (Var 2)) "x1 x2"

challenge3Test7 = testCaseCreatorChallenge3 "Simple Case" (Abs (V 1) (Var 1)) "\\x1 -> x1"

challenge3Test9 = testCaseCreatorChallenge3 "Simple Case" (Pair (Var 1) (Var 2)) "(x1, x2)"

challenge3Test10 = testCaseCreatorChallenge3 "Simple Case" (Fst (Pair (Var 1) (Var 2))) "fst (x1, x2)"

challenge3Test11 = testCaseCreatorChallenge3 "Simple Case" (Snd (Pair (Var 1) (Var 2))) "snd (x1, x2)"

challenge3Test12 = testCaseCreatorChallenge3 "Nested Case" (App (App (Var 1) (Var 2)) (Var 3)) "x1 x2 x3"

challenge3Test14 = testCaseCreatorChallenge3 "Complex Case" (Let (Discard) (Var 1) (Var 2)) "let _ = x1 in x2"

-- Group all tests together for Challenge 3
groupTestChallenge3 = TestList [challenge4Test5, challenge4Test6, challenge3Test7, challenge3Test9, challenge3Test10, challenge3Test11, challenge3Test12, challenge3Test14]

-- Run tests for Challenge 3
mainChallenge3 :: IO Counts
mainChallenge3 = runTestTT groupTestChallenge3

-- CHALLENGE 4:

-- Helper function to create a test case for Challenge 4
testCaseCreatorChallenge4 :: String -> String -> Maybe LExpr -> Test
testCaseCreatorChallenge4 name input expected = TestLabel name $ TestCase (assertEqual name expected (parseLetx input))

-- Test cases for Challenge 4

challenge4Test1 = testCaseCreatorChallenge4 "Task example 1" "x1 (x2 x3)" (Just (App (Var 1) (App (Var 2) (Var 3))))

challenge4Test2 = testCaseCreatorChallenge4 "Task example 2" "x1 x2 x3" (Just (App (App (Var 1) (Var 2)) (Var 3)))

challenge4Test3 = testCaseCreatorChallenge4 "Task example 3" "let x1 x3 = x2 in x1 x2" (Just (Let (V 1) (Abs (V 3) (Var 2)) (App (Var 1) (Var 2))))

challenge4Test4 = testCaseCreatorChallenge4 "Task example 4" "let x1 _ x3 = x3 in \\x3 -> x1 x3 x3" (Just (Let (V 1) (Abs Discard (Abs (V 3) (Var 3))) (Abs (V 3) (App (App (Var 1) (Var 3)) (Var 3)))))

challenge4Test5 = testCaseCreatorChallenge4 "Simple Case" "x1" (Just (Var 1))

challenge4Test6 = testCaseCreatorChallenge4 "Error Case" "" Nothing

-- Group all tests together for Challenge 4
groupTestChallenge4 = TestList [challenge4Test1, challenge4Test2, challenge4Test3, challenge4Test4, challenge4Test5, challenge4Test6]

-- Run tests for Challenge 4
mainChallenge4 :: IO Counts
mainChallenge4 = runTestTT groupTestChallenge4

-- CHALLENGE 5:

-- Helper function to create a test case for challenge 5
testCaseCreatorChallenge5 :: String -> LExpr -> LamExpr -> Test

-- Used first creator to test whether test cases pass and then the current creator to check for alpha equivalence

-- testCaseCreatorChallenge5 name input expected = TestLabel name (TestCase (assertEqual name expected (letEnc input)))

testCaseCreatorChallenge5 name input expected = TestLabel name (TestCase (assertBool name (ifAlphaEquivalent expected (letEnc input))))

-- Checks alpha equivalence of two lambda expressions
ifAlphaEquivalent :: LamExpr -> LamExpr -> Bool
ifAlphaEquivalent (LamVar x) (LamVar y) = x == y
ifAlphaEquivalent (LamApp e1 e2) (LamApp e1' e2') = ifAlphaEquivalent e1 e1' && ifAlphaEquivalent e2 e2'
ifAlphaEquivalent (LamAbs x e) (LamAbs y e') = ifAlphaEquivalent e (renameVar y x e')
ifAlphaEquivalent _ _ = False

-- Renames variable in lambda expression
renameVar :: Int -> Int -> LamExpr -> LamExpr
renameVar old new (LamVar x) = LamVar (if x == old then new else x)
renameVar old new (LamApp e1 e2) = LamApp (renameVar old new e1) (renameVar old new e2)
renameVar old new (LamAbs x e) = LamAbs (if x == old then new else x) (renameVar old new e)

-- Test cases for Challenge 5

challenge5Test1 = testCaseCreatorChallenge5 "Task example 1"  (Let Discard (Abs (V 1) (Var 1)) (Abs (V 1) (Var 1))) (LamApp (LamAbs 0 (LamAbs 2 (LamVar 2))) (LamAbs 2 (LamVar 2)))

challenge5Test2 = testCaseCreatorChallenge5 "Task example 2" (Fst (Pair (Abs (V 1) (Var 1)) (Abs Discard (Var 2)))) (LamApp (LamAbs 0 (LamApp (LamApp (LamVar 0) (LamAbs 2 (LamVar 2))) (LamAbs 0 (LamVar 2)))) (LamAbs 0 (LamAbs 1 (LamVar 0))))

challenge5Test3 = testCaseCreatorChallenge5 "Simple Case" (Var 2) (LamVar 2)

challenge5Test4 = testCaseCreatorChallenge5 "Simple Case" (App (Var 1) (Var 2)) (LamApp (LamVar 1) (LamVar 2))

challenge5Test5 = testCaseCreatorChallenge5 "Simple Case" (Abs (V 3) (Var 3)) (LamAbs 3 (LamVar 3))

challenge5Test6 = testCaseCreatorChallenge5 "Nested Case" (Let Discard (Var 4) (Var 5)) (LamApp (LamAbs 0 (LamVar 6)) (LamVar 4))

challenge5Test7 = testCaseCreatorChallenge5 "Complex Case" (Fst (Pair (Var 6) (Var 7)))
  (LamApp (LamAbs 0 (LamApp (LamApp (LamVar 0) (LamVar 6)) (LamVar 7))) (LamAbs 0 (LamAbs 1 (LamVar 0))))

-- Group all tests together for Challenge 5
groupTestChallenge5 :: Test
groupTestChallenge5 = TestList [challenge5Test1, challenge5Test2, challenge5Test3, challenge5Test4, challenge5Test5, challenge5Test7]

-- Run tests for Challenge 5
mainChallenge5 :: IO Counts
mainChallenge5 = runTestTT groupTestChallenge5

-- CHALLENGE 6:

-- Helper function to create a test case for Challenge 6
testCaseCreatorChallenge6 :: String -> LExpr -> Int -> (Int, Int, Int, Int) -> Test
testCaseCreatorChallenge6 name input upperBound expected =
  TestLabel name (TestCase (assertEqual name expected (compareRedn input upperBound)))

-- Test cases for Challenge 6
challenge6Test1 :: Test
challenge6Test1 = testCaseCreatorChallenge6
  "Task Example 1"
  (Let (V 3) (Pair (App (Abs (V 1) (App (Var 1) (Var 1))) (Abs (V 2) (Var 2))) (App (Abs (V 1) (App (Var 1) (Var 1))) (Abs (V 2) (Var 2)))) (Fst (Var 3)))
  10
  (6, 8, 4, 6)

challenge6Test2 :: Test
challenge6Test2 = testCaseCreatorChallenge6
  "Task Example 2"
  (Let Discard (App (Abs (V 1) (Var 1)) (App (Abs (V 1) (Var 1)) (Abs (V 1) (Var 1)))) (Snd (Pair (App (Abs (V 1) (Var 1)) (Abs (V 1) (Var 1))) (Abs (V 1) (Var 1))))) 
  10
  (5, 7, 2, 4)

challenge6Test3 :: Test
challenge6Test3 = testCaseCreatorChallenge6
  "Task Example 3"
  (Let (V 2) (Let (V 1) (Abs (V 0) (App (Var 0) (Var 0))) (App (Var 1) (Var 1))) (Snd (Pair (Var 2) (Abs (V 1) (Var 1)))))
  100
  (100, 100, 2, 4)

challenge6Test4 :: Test
challenge6Test4 = testCaseCreatorChallenge6
  "Simple Case"
  (Let (V 4) (Var 2) (Var 4))
  10
  (1, 1, 1, 1)

challenge6Test5 :: Test
challenge6Test5 = testCaseCreatorChallenge6
  "Nested Case"
  (Let (V 5) (Let (V 6) (Var 1) (Var 6)) (Var 5))
  10
  (2, 2, 2, 2)


challenge6Test6 :: Test
challenge6Test6 = testCaseCreatorChallenge6
  "Complex Case"
  (Let (V 7) (Abs (V 1) (Pair (Var 1) (Var 7))) (Var 7))
  10
  (1, 1, 1, 1)

challenge6Test7 :: Test
challenge6Test7 = testCaseCreatorChallenge6
  "Edge Case"
  (Let Discard (Var 8) (Var 9))
  10
  (1, 1, 1, 1)

challenge6Test8 :: Test
challenge6Test8 = testCaseCreatorChallenge6
  "Error Case"
  (Let (V 10) (Abs (V 11) (Var 11)) (Var 12))
  10
  (1, 1, 1, 1)

challenge6Test9 :: Test
challenge6Test9 = testCaseCreatorChallenge6
  "Large and Complex Case"
  (Let (V 11) (Abs (V 12) (Let (V 13) (Var 12) (Var 13))) (Var 11))
  100
  (1, 1, 1, 1)

-- Group all tests together for Challenge 6
groupTestChallenge6 :: Test
groupTestChallenge6 = TestList [challenge6Test1, challenge6Test2, challenge6Test3, challenge6Test4, challenge6Test5, challenge6Test6, challenge6Test7, challenge6Test8, challenge6Test9]

-- Run tests for Challenge 5
mainChallenge6 :: IO Counts
mainChallenge6 = runTestTT groupTestChallenge6

-- Main function that can be used to run tests on all of the challenges if needed

-- main :: IO ()
-- main = do
--   _ <- mainChallenge1
--   _ <- mainChallenge2
--   _ <- mainChallenge3
--   _ <- mainChallenge4
--   _ <- mainChallenge5
--   return ()
