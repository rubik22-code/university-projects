{-# LANGUAGE DeriveGeneric #-}
--SKELETON FILE FOR HANDIN 1 OF COURSEWORK 1 for COMP2209, 2023
--CONTAINS ALL FUNCTIONS REQIURED FOR COMPILATION AGAINST THE TEST SUITE
--MODIFY THE FUNCTION DEFINITIONS WITH YOUR OWN SOLUTIONS
--IMPORTANT : DO NOT MODIFY ANY FUNCTION TYPES
--Julian Rathke, Oct 2023

module Exercises (histogram,renderMaze,markGuess,Check(..),Marking) where

-- Add your own imports here --

-- Exercise A1
histogram :: Int -> [Int] -> [Int]

histogram n xs | any (<0) xs || n < 0 = error "Negative values" -- base case

histogram 0 _ = error "Divide by zero" -- base case

histogram n xs = [count n i xs | i <- [0..maximum xs `div` n]]

count :: Int -> Int -> [Int] -> Int
count n i xs = length $ filter (\x -> (x `div` n) == i) xs

-- Exercise A2

renderMaze :: [ ((Int,Int),(Int,Int)) ] -> [String]

renderMaze [] = [] -- base case

renderMaze maze = [[if anyPathExist x y then '#' else ' ' | x <- [0..maxX]] | y <- [0..maxY]] -- marked based on boolean values
    where
        maxX = if null maze then 0 else maximum $ map (fst . fst) maze ++ map (fst . snd) maze
        maxY = if null maze then 0 else maximum $ map (snd . fst) maze ++ map (snd . snd) maze

        anyPathExist x y = any (pathExist x y) maze -- checks if part of any string in the path

        pathExist x y ((x1, y1), (x2, y2))
            | x1 == x2 = x == x1 && y `elem` [min y1 y2 .. max y1 y2] -- vertical path
            | y1 == y2 = y == y1 && x `elem` [min x1 x2 .. max x1 x2] -- horizontal path
            | x1 == x2 && y1 == y2 = x == x1 && y == y1 -- single point
            | otherwise = False -- path doesn't exist


-- Exercise A3

data Check = Green | Yellow | Grey deriving (Eq,Show,Read)
type Marking = [(Char,Check)]

markGuess  :: String -> String -> Marking 
markGuess secret guess
    | length secret /= length guess = error "Length mismatch"
    | otherwise = markYellow secret (markGreen secret guess)

markGreen :: String -> String -> Marking
markGreen secret guess = map checkGreen $ zip secret guess
    where
        checkGreen (s, g) = if s == g then (g, Green) else (g, Grey)

markYellow :: String -> Marking -> Marking
markYellow secret marked = map checkYellow marked
    where
        checkYellow (g, c)
            | c == Grey && g `elem` secret = (g, Yellow)
            | otherwise = (g, c)
