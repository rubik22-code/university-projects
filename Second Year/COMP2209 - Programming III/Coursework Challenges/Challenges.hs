{-# LANGUAGE DeriveGeneric #-}
-- COMP2209 Functional Programming Challenges
-- Patrick Jfremov-Kustov, (c) University of Southampton 2021

-- DO NOT MODIFY THE FOLLOWING LINES OF CODE
module Challenges (TileEdge(..),Tile(..),Puzzle,isPuzzleComplete,
                   Rotation(..),solveCircuit,
                   LExpr(..),Bind(..),prettyPrint,parseLetx,
                   LamExpr(..),letEnc,compareRedn)
                    where

-- Import standard library and parsing definitions from Hutton 2016, Chapter 13
import Parsing
import qualified Data.Set as Set
import qualified Data.Map as Map
import Data.Set (Set)
import Data.List (intercalate, (\\), nub, transpose, findIndex, find)
import Debug.Trace
import Control.Monad ()
import Data.Maybe (catMaybes, listToMaybe, fromMaybe, fromJust, isJust, maybeToList)

-- Challenge 1
-- Testing Circuits

data TileEdge = North | East | South | West  deriving (Eq,Ord,Show,Read)
data Tile = Source [ TileEdge ] | Sink [ TileEdge ] | Wire [ TileEdge ]  deriving (Eq,Show,Read)
type Puzzle = [ [ Tile ] ]

type Position = (Int, Int)

-- Checking if a puzzle is complete with multiple helper functions acting as conditions
isPuzzleComplete :: Puzzle -> Bool
isPuzzleComplete puzzle = 
    hasAtLeastOneSource puzzle && 
    hasAtLeastOneSink puzzle && 
    allSourcesConnectedToSinks puzzle && 
    allSinksConnectedToSources puzzle && 
    allConnectionsValid puzzle &&
    hasValidSourcesAndSinks puzzle && 
    allTilesConnected puzzle &&
    noLooseEnds puzzle &&
    noTilesPointingOutside puzzle

-- Checking for any source in flattened grid
hasAtLeastOneSource :: Puzzle -> Bool
hasAtLeastOneSource = any (any isSource)

-- Checking for any sink in flattened grid
hasAtLeastOneSink :: Puzzle -> Bool
hasAtLeastOneSink = any (any isSink)

-- If tile is a source
isSource :: Tile -> Bool
isSource (Source _) = True
isSource _          = False

-- If tile is a sink
isSink :: Tile -> Bool
isSink (Sink _) = True
isSink _        = False

-- List of source positions checking if connected to sink
allSourcesConnectedToSinks :: Puzzle -> Bool
allSourcesConnectedToSinks puzzle = all (`isConnectedToSink` puzzle) sources
  where
    sources = findSources puzzle

-- Positions of all sink tiles in the grid
findSinks :: Puzzle -> [Position]
findSinks puzzle = [(x, y) | x <- [0..length puzzle - 1], y <- [0..length (head puzzle) - 1], isSink (puzzle !! x !! y)]

-- Positions of all source tiles in the grid
findSources :: Puzzle -> [Position]
findSources puzzle = [(x, y) | x <- [0..length puzzle - 1], y <- [0..length (head puzzle) - 1], isSource (puzzle !! x !! y)]

-- If given source is connected to sink
isConnectedToSink :: Position -> Puzzle -> Bool
isConnectedToSink pos puzzle = any (isPathToSink pos puzzle []) sinks
  where
    sinks = findSinks puzzle

-- If given sink is connected to source
isConnectedToSource :: Position -> Puzzle -> Bool
isConnectedToSource sinkPos puzzle = any (isPathToSource sinkPos puzzle []) sources
  where
    sources = findSources puzzle

-- Checks path from source to sink recursively
isPathToSink :: Position -> Puzzle -> [Position] -> Position -> Bool
isPathToSink sourcePos puzzle visited sinkPos
    | sourcePos == sinkPos = True
    | sourcePos `elem` visited = False
    | otherwise = any (\nextPos -> isPathToSink nextPos puzzle (sourcePos : visited) sinkPos) (nextPositions sourcePos puzzle)

-- Based on current position and edges
nextPositions :: Position -> Puzzle -> [Position]
nextPositions (x, y) puzzle = concatMap (adjacentPosition (x, y) puzzle) (edges (puzzle !! x !! y))

-- Returns edges of tile
edges :: Tile -> [TileEdge]
edges (Source e) = e
edges (Sink e) = e
edges (Wire e) = e

-- Adjacent position based on current
adjacentPosition :: Position -> Puzzle -> TileEdge -> [Position]
adjacentPosition (x, y) puzzle edge = case edge of
    North -> ([(x - 1, y) | x > 0 && isConnected puzzle South (x - 1, y)])
    South -> ([(x + 1, y) | x < length puzzle - 1 && isConnected puzzle North (x + 1, y)])
    West -> ([(x, y - 1) | y > 0 && isConnected puzzle East (x, y - 1)])
    East -> ([(x, y + 1) | y < length (head puzzle) - 1 && isConnected puzzle West (x, y + 1)])

-- If connected to specific edge direction
isConnected :: Puzzle -> TileEdge -> Position -> Bool
isConnected puzzle direction (x, y) = direction `elem` edges (puzzle !! x !! y)

-- List of sink positions checking if connected to source
allSinksConnectedToSources :: Puzzle -> Bool
allSinksConnectedToSources puzzle = all (`isConnectedToSource` puzzle) sinks
  where
    sinks = findSinks puzzle

-- Checks path from sink to source recursively
isPathToSource :: Position -> Puzzle -> [Position] -> Position -> Bool
isPathToSource sinkPos puzzle visited sourcePos
    | sinkPos == sourcePos = True
    | sinkPos `elem` visited = False
    | otherwise = any (\nextPos -> isPathToSource nextPos puzzle (sinkPos : visited) sourcePos) (nextPositions sinkPos puzzle)

-- Iterating over all positions to check if valid
allConnectionsValid :: Puzzle -> Bool
allConnectionsValid puzzle = all isValidConnection positions
  where
    positions = [(x, y) | x <- [0..length puzzle - 1], y <- [0..length (head puzzle) - 1]]

    isValidConnection :: Position -> Bool
    isValidConnection (x, y) = all (isConnected (x, y) puzzle) (edges (puzzle !! x !! y))

    isConnected :: Position -> Puzzle -> TileEdge -> Bool
    isConnected (x, y) puzzle edge = case edge of
        North -> x > 0 && South `elem` edges (puzzle !! (x-1) !! y)
        South -> x < length puzzle - 1 && North `elem` edges (puzzle !! (x+1) !! y)
        West -> y > 0 && East `elem` edges (puzzle !! x !! (y-1))
        East -> y < length (head puzzle) - 1 && West `elem` edges (puzzle !! x !! (y+1))

-- Checking using multiple helper functions for conditions to be met
hasValidSourcesAndSinks :: Puzzle -> Bool
hasValidSourcesAndSinks puzzle = 
    hasAtLeastOneSource puzzle &&
    hasAtLeastOneSink puzzle &&
    allSourcesConnectedToSinks puzzle &&
    allSinksConnectedToSources puzzle

-- If all tiles connected to other tiles
allTilesConnected :: Puzzle -> Bool
allTilesConnected puzzle = all isConnectedTile positions
  where
    positions = [(x, y) | x <- [0..length puzzle - 1], y <- [0..length (head puzzle) - 1]]
    -- Checks if all the edges are connected
    isConnectedTile :: Position -> Bool
    isConnectedTile (x, y) = all (isEdgeConnected (x, y) puzzle) (edges (puzzle !! x !! y))
    -- Helper functions for finding connected tile
    isEdgeConnected :: Position -> Puzzle -> TileEdge -> Bool
    isEdgeConnected (x, y) puzzle edge = case edge of
        North -> x > 0 && South `elem` edges (puzzle !! (x-1) !! y)
        South -> x < length puzzle - 1 && North `elem` edges (puzzle !! (x+1) !! y)
        West -> y > 0 && East `elem` edges (puzzle !! x !! (y-1))
        East -> y < length (head puzzle) - 1 && West `elem` edges (puzzle !! x !! (y+1))

    edges :: Tile -> [TileEdge]
    edges (Source e) = e
    edges (Sink e) = e
    edges (Wire e) = e

-- Checks all wire tiles have edges connected
noLooseEnds :: Puzzle -> Bool
noLooseEnds puzzle = all hasNoLooseEnds positions
  where
    positions = [(x, y) | x <- [0..length puzzle - 1], y <- [0..length (head puzzle) - 1], isWire (puzzle !! x !! y)]

    hasNoLooseEnds :: Position -> Bool
    hasNoLooseEnds (x, y) = all (isEdgeConnected (x, y) puzzle) (edges (puzzle !! x !! y))

    isWire :: Tile -> Bool
    isWire (Wire _) = True
    isWire _        = False
    -- Helper funcion for hasNoLooseEnds
    isEdgeConnected :: Position -> Puzzle -> TileEdge -> Bool
    isEdgeConnected (x, y) puzzle edge = case edge of
        North -> x > 0 && South `elem` edges (puzzle !! (x-1) !! y)
        South -> x < length puzzle - 1 && North `elem` edges (puzzle !! (x+1) !! y)
        West -> y > 0 && East `elem` edges (puzzle !! x !! (y-1))
        East -> y < length (head puzzle) - 1 && West `elem` edges (puzzle !! x !! (y+1))

-- Checking whether tiles are pointing outside of the bounds
noTilesPointingOutside :: Puzzle -> Bool
noTilesPointingOutside puzzle = all withinBounds positions
  where
    positions = [(x, y) | x <- [0..length puzzle - 1], y <- [0..length (head puzzle) - 1]]

    withinBounds :: Position -> Bool
    withinBounds (x, y) = all (isEdgeWithinBounds (x, y) (length puzzle, length (head puzzle))) (edges (puzzle !! x !! y))
    
    isEdgeWithinBounds :: Position -> (Int, Int) -> TileEdge -> Bool
    isEdgeWithinBounds (x, y) (maxX, maxY) edge = case edge of
        North -> x > 0
        South -> x < maxX - 1
        West  -> y > 0
        East  -> y < maxY - 1

-- Challenge 2
-- Solving Circuits
data Rotation = R0 | R90 | R180 | R270
  deriving (Eq,Show,Read, Ord)

-- To store previously computed results
type Memo = Map.Map PuzzleState (Maybe [[Rotation]])
type PuzzleState = [[Rotation]]

-- Main function (if not already complete, or solvable, then search for solution)
solveCircuit :: Puzzle -> Maybe [[Rotation]]
solveCircuit puzzle
  | isPuzzleComplete puzzle = Just (initialRotationState puzzle)
  | not (isPuzzleSolvable puzzle) = Nothing
  | otherwise = fst $ searchForSolution puzzle (initialRotationState puzzle) Map.empty

-- Check if the puzzle is solvable (has at least one source and one sink).
isPuzzleSolvable :: Puzzle -> Bool
isPuzzleSolvable puzzle = hasAtLeastOneSource puzzle && hasAtLeastOneSink puzzle

-- Generate initial rotation state for the puzzle (all pieces at R0).
initialRotationState :: Puzzle -> [[Rotation]]
initialRotationState puzzle = replicate (length puzzle) (replicate (length (head puzzle)) R0)

-- Recursive function by checking memo for previously computed result and returning, or otherwise rotating the tiles
searchForSolution :: Puzzle -> PuzzleState -> Memo -> (Maybe [[Rotation]], Memo)
searchForSolution puzzle rotations memo
    | Map.member rotations memo = (fromMaybe Nothing $ Map.lookup rotations memo, memo)  -- 
    | isPuzzleCompleteCircuit puzzle rotations = (Just rotations, Map.insert rotations (Just rotations) memo)
    | otherwise = tryRotations puzzle rotations (0, 0) memo

-- If the current puzzle forms a complete circuit
isPuzzleCompleteCircuit :: Puzzle -> PuzzleState -> Bool
isPuzzleCompleteCircuit puzzle rotations = 
    hasAtLeastOneSource puzzle && 
    hasAtLeastOneSink puzzle &&
    allTilesValid puzzle rotations &&
    allTilesConnected puzzle &&
    allConnectionsValid puzzle &&
    allSourcesConnectedToSinks puzzle && 
    allSinksConnectedToSources puzzle && 
    hasValidSourcesAndSinks puzzle && 
    noLooseEnds puzzle &&
    noTilesPointingOutside puzzle

-- Recursive helper
allTilesValid :: Puzzle -> PuzzleState -> Bool
allTilesValid puzzle rotations = allTilesValidHelper puzzle rotations 0 0

-- Helper function, to iterate over each tile
allTilesValidHelper :: Puzzle -> PuzzleState -> Int -> Int -> Bool
allTilesValidHelper puzzle rotations x y
  | x >= length puzzle = True
  | y >= length (head puzzle) = allTilesValidHelper puzzle rotations (x + 1) 0
  | otherwise = 
      let isValid = tileIsValid puzzle rotations (x, y)
      in trace ("Checking tile at (" ++ show x ++ ", " ++ show y ++ "): " ++ show isValid) isValid && allTilesValidHelper puzzle rotations x (y + 1)

tileIsValid :: Puzzle -> PuzzleState -> (Int, Int) -> Set.Set (Int, Int) -> Bool
tileIsValid puzzle rotations (x, y) checked
  | Set.member (x, y) checked = True
  | otherwise =
    let tile = puzzle !! x !! y
        rotation = rotations !! x !! y
        rotatedTile = applyRotation tile rotation
        adjacentPositionsList = adjacentPositions (x, y) puzzle
        newChecked = Set.insert (x, y) checked
    in all (\adjPos -> 
            let isValidConnection = isConnectionValid puzzle rotations rotatedTile adjPos
            in trace ("Checking connection from (" ++ show x ++ ", " ++ show y ++ 
                      ") to " ++ show adjPos ++ ": " ++ show isValidConnection) 
               isValidConnection) adjacentPositionsList

isValidConnection :: Puzzle -> PuzzleState -> Tile -> Position -> Bool
isValidConnection puzzle rotations tile (adjX, adjY) =
    if outOfBounds adjX adjY (length puzzle, length (head puzzle)) then True
    else let
        adjacentTile = puzzle !! adjX !! adjY
        adjacentRotation = rotations !! adjX !! adjY
        rotatedAdjacentTile = applyRotation adjacentTile adjacentRotation
    in edgesConnect (edges tile) (edges rotatedAdjacentTile)

outOfBounds :: Int -> Int -> (Int, Int) -> Bool
outOfBounds x y (maxX, maxY) = x < 0 || y < 0 || x >= maxX || y >= maxY

isConnectionValid :: Puzzle -> PuzzleState -> Tile -> Position -> Bool
isConnectionValid puzzle rotations tile (adjX, adjY) =
    if outOfBounds adjX adjY (length puzzle, length (head puzzle)) then True
    else let
        adjacentTile = puzzle !! adjX !! adjY
        adjacentTileRotation = rotations !! adjX !! adjY
        rotatedAdjacentTile = applyRotation adjacentTile adjacentTileRotation
    in edgesConnect (edges tile) (edges rotatedAdjacentTile)

-- To a given position in the puzzle
adjacentPositions :: Position -> Puzzle -> [Position]
adjacentPositions (x, y) puzzle =
  concatMap validPosition [north, south, east, west]
  where
    puzzleHeight = length puzzle
    puzzleWidth = length (head puzzle)
    north = if x > 0 then Just (x - 1, y) else Nothing
    south = if x < puzzleHeight - 1 then Just (x + 1, y) else Nothing
    east = if y < puzzleWidth - 1 then Just (x, y + 1) else Nothing
    west = if y > 0 then Just (x, y - 1) else Nothing
    validPosition = maybeToList

-- Apply rotation to tile if leads to valid puzzle state
applyRotationIfValid :: Puzzle -> PuzzleState -> Position -> Rotation -> Memo -> (Maybe [[Rotation]], Memo)
applyRotationIfValid puzzle rotations position rotation memo =
    let newRotations = updateRotations rotations position rotation
    in if isValidRotation puzzle newRotations position
       then searchForSolution puzzle newRotations memo
       else (Nothing, memo)

-- Rotate tile at specific position
tryRotations :: Puzzle -> PuzzleState -> Position -> Memo -> (Maybe [[Rotation]], Memo)
tryRotations puzzle rotations position memo
    | isEndOfPuzzle puzzle position = (Just rotations, memo)
    | otherwise = tryAllRotations puzzle rotations position memo

-- Find valid rotation for next position
tryNextPositions :: Puzzle -> PuzzleState -> Position -> Memo -> (Maybe [[Rotation]], Memo)
tryNextPositions puzzle rotations position memo
    | isEndOfPuzzle puzzle position = (Nothing, memo)
    | otherwise = tryRotations puzzle rotations (nextPosition position puzzle) memo

-- Try all possible rotations for a tile 
tryAllRotations :: Puzzle -> PuzzleState -> Position -> Memo -> (Maybe [[Rotation]], Memo)
tryAllRotations puzzle rotations position memo =
    foldl (\(accResult, accMemo) rotation ->
        case accResult of
            Just _ -> (accResult, accMemo)
            Nothing -> applyRotationIfValid puzzle rotations position rotation accMemo -- Try next rotation.
    ) (Nothing, memo) [R0, R90, R180, R270]

-- Helper function
nextPosition :: Position -> Puzzle -> Position
nextPosition (x, y) puzzle
    | y < length (head puzzle) - 1 = (x, y + 1)
    | x < length puzzle - 1 = (x + 1, 0)
    | otherwise = (x, y)  -- End of puzzle

-- Update rotation state for a specific position
updateRotations :: [[Rotation]] -> Position -> Rotation -> [[Rotation]]
updateRotations rotations (x, y) newRotation =
    take x rotations ++
    [updateRow (rotations !! x) y newRotation] ++
    drop (x + 1) rotations

-- Helper function to update a specific row
updateRow :: [Rotation] -> Int -> Rotation -> [Rotation]
updateRow row y newRotation =
    take y row ++ [newRotation] ++ drop (y + 1) row

-- Helper function to check if the new rotation is valid
isValidRotation :: Puzzle -> [[Rotation]] -> Position -> Bool
isValidRotation puzzle rotations pos =
    let tile = puzzle !! fst pos !! snd pos
        rotation = rotations !! fst pos !! snd pos
        rotatedTile = applyRotation tile rotation
        edgesToCheck = rotateEdges (edges rotatedTile) rotation
    in all (all (isValidRotationConnection puzzle rotations)
  . adjacentPositionForTile pos puzzle rotations) edgesToCheck

-- Check if the connection for a rotated tile is valid
isValidRotationConnection :: Puzzle -> [[Rotation]] -> Position -> Bool
isValidRotationConnection puzzle rotations pos =
    let tile = puzzle !! fst pos !! snd pos
        rotation = rotations !! fst pos !! snd pos
        rotatedTile = applyRotation tile rotation
        edgesToCheck = rotateEdges (edges rotatedTile) rotation
    in all (all (isRotatedTileConnected puzzle rotations rotatedTile)
  . adjacentPositionForTile pos puzzle rotations) edgesToCheck

-- Modified adjacentPosition function considering tile's rotation
adjacentPositionForTile :: Position -> Puzzle -> [[Rotation]] -> TileEdge -> [Position]
adjacentPositionForTile (x, y) puzzle rotations edge =
    let actualEdge = rotateEdge (rotations !! x !! y) edge
    in case actualEdge of
        North -> if x > 0 then [(x-1, y)] else []
        South -> if x < length puzzle - 1 then [(x+1, y)] else []
        West -> if y > 0 then [(x, y-1)] else []
        East -> if y < length (head puzzle) - 1 then [(x, y+1)] else []

-- Helper function to apply rotation to an edge
rotateEdge :: Rotation -> TileEdge -> TileEdge
rotateEdge R0 edge = edge
rotateEdge R90 edge = rotateEdge90 edge
rotateEdge R180 edge = rotateEdge180 edge
rotateEdge R270 edge = rotateEdge270 edge

-- Check if rotation made difference to connectivity
isRotatedTileConnected :: Puzzle -> [[Rotation]] -> Tile -> Position -> Bool
isRotatedTileConnected puzzle rotations tile (adjX, adjY) =
    let adjacentTile = puzzle !! adjX !! adjY
        adjacentTileRotation = rotations !! adjX !! adjY
        rotatedAdjacentTile = applyRotation adjacentTile adjacentTileRotation
    in edgesConnect (edges tile) (edges rotatedAdjacentTile)

-- Helper function
edgesConnect :: [TileEdge] -> [TileEdge] -> Bool
edgesConnect edges1 edges2 = any (`elem` oppositeEdges edges2) edges1

-- For connection checking
oppositeEdges :: [TileEdge] -> [TileEdge]
oppositeEdges = map oppositeEdge
    where
        oppositeEdge North = South
        oppositeEdge South = North
        oppositeEdge East = West
        oppositeEdge West = East

-- Apply rotation to a tile
applyRotation :: Tile -> Rotation -> Tile
applyRotation (Source edges) rotation = Source (rotateEdges edges rotation)
applyRotation (Sink edges) rotation = Sink (rotateEdges edges rotation)
applyRotation (Wire edges) rotation = Wire (rotateEdges edges rotation)

-- To rotate edges based on rotation
rotateEdges :: [TileEdge] -> Rotation -> [TileEdge]
rotateEdges edges R0 = edges
rotateEdges edges R90 = map rotateEdge90 edges
rotateEdges edges R180 = map rotateEdge180 edges
rotateEdges edges R270 = map rotateEdge270 edges

-- To rotate an edge
rotateEdge90 :: TileEdge -> TileEdge
rotateEdge90 North = East
rotateEdge90 East = South
rotateEdge90 South = West
rotateEdge90 West = North

-- To rotate an edge
rotateEdge180 :: TileEdge -> TileEdge
rotateEdge180 North = South
rotateEdge180 East = West
rotateEdge180 South = North
rotateEdge180 West = East

-- To rotate an edge
rotateEdge270 :: TileEdge -> TileEdge
rotateEdge270 North = West
rotateEdge270 West = South
rotateEdge270 South = East
rotateEdge270 East = North

-- Helper function
isEndOfPuzzle :: Puzzle -> Position -> Bool
isEndOfPuzzle puzzle (x, y) = x >= length puzzle || y >= length (head puzzle)

-- Challenge 3
-- Pretty Printing Let Expressions

data LExpr = Var Int | App LExpr LExpr | Let Bind  LExpr LExpr | Pair LExpr LExpr | Fst LExpr | Snd LExpr  | Abs Bind LExpr
    deriving (Eq,Show,Read)
data Bind = Discard | V Int
    deriving (Eq,Show,Read)

-- Replaces the input using pattern matching and helper functions
prettyPrint :: LExpr -> String
prettyPrint (Var x) = "x" ++ show x
prettyPrint (App e1 e2) = prettyPrintApp e1 e2
prettyPrint (Let b e1 e2) = "let " ++ prettyPrintForBind b ++ " = " ++ prettyPrint e1 ++ " in " ++ prettyPrint e2
prettyPrint (Pair e1 e2) = "(" ++ prettyPrint e1 ++ ", " ++ prettyPrint e2 ++ ")"
prettyPrint (Fst e) = "fst " ++ prettyPrint e
prettyPrint (Snd e) = "snd " ++ prettyPrint e
prettyPrint (Abs b e) = "\\" ++ prettyPrintBinders [b] e

-- Handles application expressions
prettyPrintApp :: LExpr -> LExpr -> String
prettyPrintApp e1 e2 = case e1 of
    Abs _ _ -> "(" ++ prettyPrint e1 ++ ") " ++ prettyPrint e2
    _       -> prettyPrint e1 ++ " " ++ prettyPrintBracket e2

-- Add parentheses to an application
prettyPrintBracket :: LExpr -> String
prettyPrintBracket e@(App _ _) = "(" ++ prettyPrint e ++ ")"
prettyPrintBracket e = prettyPrint e

-- Handles the Bind data type
prettyPrintForBind :: Bind -> String
prettyPrintForBind Discard = "_"
prettyPrintForBind (V x) = "x" ++ show x

-- Collects binders from nested abstractions and formats them recursively
prettyPrintBinders :: [Bind] -> LExpr -> String
prettyPrintBinders bs (Abs b e) = prettyPrintBinders (bs ++ [b]) e
prettyPrintBinders bs e = unwords (map prettyPrintForBind bs) ++ " -> " ++ prettyPrint e

-- Challenge 4 - Parsing Let Expressions

-- Either uses entire input string and wraps the result, or fails if no / partial input consumption / multiple results
parseLetx :: String -> Maybe LExpr
parseLetx input = case parse expr input of
  [(result, [])] -> Just result
  _              -> Nothing

-- Combinator trying expression functions in order specified
expr :: Parser LExpr
expr = letExpression <|> absExpression <|> appExpression <|> pairExpression <|> fstExpression <|> sndExpression <|> varExpression

-- Creates let expressions when sufficient symbols are satisfied
letExpression :: Parser LExpr
letExpression = do
  symbol "let"
  b <- bind
  bs <- many bind
  symbol "="
  e1 <- expr
  symbol "in"
  Let b (foldr Abs e1 bs) <$> expr

-- Creates abs expressions when sufficient symbols are satisfied
absExpression :: Parser LExpr
absExpression = do
  symbol "\\"
  bs <- some bind
  symbol "->"
  e <- expr
  return $ foldr Abs e bs

-- Creates app expressions when sufficient symbols are satisfied
appExpression :: Parser LExpr
appExpression = do
  e1 <- atomExpression
  rest <- many (token atomExpression)
  return $ foldl App e1 rest

-- Creates pair expressions when sufficient symbols are satisfied
pairExpression :: Parser LExpr
pairExpression = do
  symbol "("
  e1 <- expr
  symbol ","
  e2 <- expr
  symbol ")"
  return $ Pair e1 e2

-- Creates Fst expression when sufficient symbols are satisfied
fstExpression :: Parser LExpr
fstExpression = do
  symbol "fst"
  Fst <$> atomExpression

-- Creates Snd expression when sufficient symbols are satisfied
sndExpression :: Parser LExpr
sndExpression = do
  symbol "snd"
  Snd <$> atomExpression

-- Creates parser for variables when sufficient symbols are satisfied
varExpression :: Parser LExpr
varExpression = do
  symbol "x"
  ds <- some digit
  return $ Var (read ds)

-- Creates atomic expressions using combinators
atomExpression :: Parser LExpr
atomExpression = varExpression <|> pairExpression <|> (do symbol "("; e <- expr; symbol ")"; return e)

-- Combinator for bind
bind :: Parser Bind
bind = varBind <|> discardBind

-- Creates a discard bind when sufficient symbols are satisfied
discardBind :: Parser Bind
discardBind = do
  symbol "_"
  return Discard

-- Creates a variable bind when sufficient symbols are satisfied
varBind :: Parser Bind
varBind = do
  symbol "x"
  ds <- some digit
  return $ V (read ds)

-- Challenge 5
-- Let Encoding in Lambda 

data LamExpr = LamVar Int | LamApp LamExpr LamExpr | LamAbs Int LamExpr
                deriving (Eq, Show, Read)

-- Converts LExpr into LamExpr expression using pattern matching
letEnc :: LExpr -> LamExpr
letEnc (Var x) = LamVar x
letEnc (App e1 e2) = LamApp (letEnc e1) (letEnc e2)
letEnc (Abs (V x) e) = LamAbs x (letEnc e)
letEnc (Abs Discard e) = LamAbs (boundVar e) (letEnc e)
letEnc (Let (V x) e1 e2) = LamApp (LamAbs x (letEnc e2)) (letEnc e1)
letEnc (Let Discard e1 e2) = LamApp (LamAbs (boundVar e2) (letEnc e2)) (letEnc e1)
letEnc (Pair e1 e2) = LamAbs (boundVarPair e1 e2) (LamApp (LamApp (LamVar (boundVarPair e1 e2)) (letEnc e1)) (letEnc e2))
letEnc (Fst e) = LamApp (letEnc e) (LamAbs 0 (LamAbs 1 (LamVar 0)))
letEnc (Snd e) = LamApp (letEnc e) (LamAbs 0 (LamAbs 1 (LamVar 1)))

-- Next available variable index that's not in the list of usedVars
freshBoundVar :: [Int] -> [Int] -> Int -> Int
freshBoundVar usedVars xs x
    | x `elem` usedVars = freshBoundVar usedVars xs (x + 1)
    | otherwise         = x

-- Next available variable index that is not interfering in a given lambda expression 
boundVar :: LExpr -> Int
boundVar e = fromMaybe 0 $ find (`notElem` vars) [0..]
  where
    vars = nub $ returnVars e

-- Next available variable index not interfering for two lambda expressions
boundVarPair :: LExpr -> LExpr -> Int
boundVarPair e1 e2 = fromMaybe 0 $ find (`notElem` vars) [0..]
  where
    vars = nub $ returnVars e1 ++ returnVars e2

-- Extract all variable indices in the expression
returnVars :: LExpr -> [Int]
returnVars (Var x) = [x]
returnVars (App e1 e2) = returnVars e1 ++ returnVars e2
returnVars (Abs bind e) = returnVars e
returnVars (Let bind e1 e2) = returnVars e1 ++ returnVars e2
returnVars (Pair e1 e2) = returnVars e1 ++ returnVars e2
returnVars (Fst e) = returnVars e
returnVars (Snd e) = returnVars e

-- Challenge 6
-- Compare Innermost Reduction for Let_x and its Lambda Encoding

------------
-- LAMBDA --
------------

free :: Int -> LamExpr -> Bool
free x (LamVar y) =  x == y
free x (LamAbs y e) | x == y = False
free x (LamAbs y e) | x /= y = free x e
free x (LamApp e1 e2)  = free x e1 || free x e2

rename :: Int -> LamExpr -> Int
rename x e | free (x+1) e = rename (x+1) e
           | otherwise = x+1 

subst :: LamExpr -> Int ->  LamExpr -> LamExpr
subst (LamVar x) y e | x == y = e
subst (LamVar x) y e | x /= y = LamVar x
subst (LamAbs x e1) y e  |  x /= y && not (free x e)  = LamAbs x (subst e1 y e)
subst (LamAbs x e1) y e  |  x /= y &&     free x e  = let x' = rename x e1 in subst (LamAbs x' (subst e1 x (LamVar x'))) y e
subst (LamAbs x e1) y e  | x == y  = LamAbs x e1
subst (LamApp e1 e2) y e = LamApp (subst e1 y e) (subst e2 y e) 

isLamValue :: LamExpr -> Bool
isLamValue (LamVar _) = True
isLamValue (LamAbs _ _) = True
isLamValue _ = False

-- CALL BY VALUE -- 
cbvlam1 :: LamExpr -> Maybe LamExpr
-- Contexts
cbvlam1 (LamApp e1 e2) | not (isLamValue e1) = 
  do e' <- cbvlam1 e1
     return (LamApp e' e2)
cbvlam1 (LamApp e1 e2) | not (isLamValue e2) = 
  do e' <- cbvlam1 e2
     return (LamApp e1 e')
-- Reductions 
cbvlam1 (LamApp (LamAbs x e1) e) | isLamValue e = Just (subst e1 x e)
-- Otherwise terminated or blocked
cbvlam1 _ = Nothing

-- CALL BY NAME --
cbnlam1 :: LamExpr -> Maybe LamExpr
-- Reductions 
cbnlam1 (LamApp (LamAbs x e1) e) = Just (subst e1 x e)
-- Contexts
cbnlam1 (LamApp e1 e2) = 
  do e' <- cbnlam1 e1
     return (LamApp e' e2)
-- Otherwise terminated or blocked
cbnlam1 _ = Nothing

---------
-- LET --
--------- 

-- Compares number of reduction steps for Let expression with both CBV and CBN
compareRedn :: LExpr -> Int -> (Int, Int, Int, Int)
compareRedn expression upperBound 
    | upperBound < 0 = (0, 0, 0, 0)
    | otherwise = (cbvlet1Length, cbvlam1Length, cbnlet1Length, cbnlam1Length)
    where
        cbvlet1Length = length (cbvlet1ReducedList (Just expression) 0 upperBound) - 1
        cbvlam1Length = length (cbvlam1ReducedList (letEnc expression) 0 upperBound) - 1
        cbnlet1Length = length (cbnlet1ReducedList (Just expression) 0  upperBound) - 1
        cbnlam1Length = length (cbnlam1ReducedList (letEnc expression) 0 upperBound) - 1

-- If variable is free in Let expression
freeVarLet :: Int -> LExpr -> Bool
freeVarLet x (Var y) =  x == y
freeVarLet x (Pair e1 e2) = freeVarLet x e1 || freeVarLet x e2
freeVarLet x (Fst (Pair e1 e2)) = freeVarLet x e1
freeVarLet x (Fst e) = freeVarLet x e
freeVarLet x (Snd (Pair e1 e2)) = freeVarLet x e2
freeVarLet x (Snd e) = freeVarLet x e
freeVarLet x (Abs Discard e) = freeVarLet x e
freeVarLet x (Abs (V y) e) | x == y = False
freeVarLet x (Abs (V y) e) | x /= y = freeVarLet x e
freeVarLet x (Let Discard e1 e2) = freeVarLet x e1 || freeVarLet x e2
freeVarLet x (Let (V y) e1 e2) | x == y = False
freeVarLet x (Let (V y) e1 e2) | x /= y = freeVarLet x e1 || freeVarLet x e2
freeVarLet x (App e1 e2)  = freeVarLet x e1 || freeVarLet x e2

-- If Let expression is a value
isLetValue :: LExpr -> Bool
isLetValue (Pair e1 e2) = isLetValue e1 && isLetValue e2
isLetValue (Abs _ _) = True
isLetValue (Var _) = True
isLetValue _ = False

-- Rename variables in Let expression to account for name collisions
renameVarLet :: Int -> LExpr -> Int
renameVarLet x e = until (\y -> not $ freeVarLet y e) (+ 1) (x + 1)

-- Substitutes variable with expression in Let
substVarLet :: LExpr -> Int ->  LExpr -> LExpr
substVarLet (Var x) y e | x == y = e
substVarLet (Var x) y e | x /= y = Var x
substVarLet (Pair e1 e2) y e = Pair (substVarLet e1 y e) (substVarLet e2 y e)
substVarLet (Fst e1) y e = Fst (substVarLet e1 y e)
substVarLet (Snd e1) y e = Snd (substVarLet e1 y e)
substVarLet (Abs Discard e1) y e = Abs Discard (substVarLet e1 y e)
substVarLet (Abs (V x) e1) y e  |  x /= y && not (freeVarLet x e) = Abs (V x) (substVarLet e1 y e)
substVarLet (Abs (V x) e1) y e  |  x /= y &&     freeVarLet x e = let x' = renameVarLet x e1 in substVarLet (Abs (V x') (substVarLet e1 x (Var x'))) y e
substVarLet (Abs (V x) e1) y e  | x == y  = Abs (V x) e1
substVarLet (Let Discard e1 e2) y e = Let Discard (substVarLet e1 y e) (substVarLet e2 y e)
substVarLet (Let (V x) e1 e2) y e  |  x /= y && not (freeVarLet x e) = Let (V x) (substVarLet e1 y e) (substVarLet e2 y e) 
substVarLet (Let (V x) e1 e2) y e  |  x /= y &&     freeVarLet x e = let x' = renameVarLet x e2 in substVarLet (Let (V x') e1 (substVarLet e2 x (Var x'))) y e
substVarLet (Let (V x) e1 e2) y e  | x == y  = Let (V x) (substVarLet e1 y e) e2  
substVarLet (App e1 e2) y e = App (substVarLet e1 y e) (substVarLet e2 y e)

-- CALL BY VALUE --
-- (same structure as LAMDA template section) --
cbvlet1 :: LExpr -> Maybe LExpr

-- Contexts
cbvlet1 (App e1 e2) | not (isLetValue e1) = do
  e' <- cbvlet1 e1
  return (App e' e2)

cbvlet1 (App e1 e2) | not (isLetValue e2) = do
  e' <- cbvlet1 e2
  return (App e1 e')

cbvlet1 (Let b e1 e2) | not (isLetValue e1) = do
  e' <- cbvlet1 e1
  return (Let b e' e2)

cbvlet1 (Pair e1 e2) | not (isLetValue e1) = do
  e' <- cbvlet1 e1
  return (Pair e' e2)

cbvlet1 (Pair e1 e2) | not (isLetValue e2) = do
  e' <- cbvlet1 e2
  return (Pair e1 e')

cbvlet1 (Fst e) | not (isLetValue e) = do
  e' <- cbvlet1 e
  return (Fst e')

cbvlet1 (Snd e) | not (isLetValue e) = do
  e' <- cbvlet1 e
  return (Snd e')

-- Reductions
cbvlet1 (App (Abs Discard e1) e) | isLetValue e = Just e1
cbvlet1 (App (Abs (V x) e1) e) | isLetValue e = Just (substVarLet e1 x e)
cbvlet1 (Let Discard e1 e2) | isLetValue e1 = Just e2
cbvlet1 (Let (V x) e1 e2) | isLetValue e1 = Just (substVarLet e2 x e1)
cbvlet1 (Fst (Pair e1 e2)) = Just e1
cbvlet1 (Fst e) = Just e
cbvlet1 (Snd (Pair e1 e2)) = Just e2 
cbvlet1 (Snd e) = Just e

-- Otherwise terminated or blocked
cbvlet1 _ = Nothing

-- List of all cbvlet1 reduced expressions
cbvlet1ReducedList :: Maybe LExpr -> Int -> Int -> [Maybe LExpr]
cbvlet1ReducedList Nothing _ _ = []
cbvlet1ReducedList (Just expression) count upperBound = generateList (Just expression) count
  where
    generateList expr e
      | e >= upperBound = [expr]
      | otherwise = case cbvlet1 <$> expr of
          Just (Just expr') -> expr : generateList (Just expr') (e + 1)
          _ -> [expr]

-- List of all cbvlam1 reduced expressions
cbvlam1ReducedList :: LamExpr -> Int -> Int -> [LamExpr]
cbvlam1ReducedList expression count upperBound = generateList expression count
  where
    generateList expr e
      | e >= upperBound = [expr]
      | otherwise = case cbvlam1 expr of
          Just expr' -> expr : generateList expr' (e + 1)
          Nothing -> [expr]

-- CALL BY NAME --
-- (same structure as LAMDA template section) --
cbnlet1 :: LExpr -> Maybe LExpr

-- Reductions
cbnlet1 (App (Abs Discard e1) e) = Just e1
cbnlet1 (App (Abs (V x) e1) e) = Just (substVarLet e1 x e)
cbnlet1 (Let Discard e1 e2) = Just e2
cbnlet1 (Let (V x) e1 e2) = Just (substVarLet e2 x e1)
cbnlet1 (Fst (Pair e1 e2)) = Just e1
cbnlet1 (Fst e) = Just e
cbnlet1 (Snd (Pair e1 e2)) = Just e2 
cbnlet1 (Snd e) = Just e

-- Contexts
cbnlet1 (App e1 e2) = 
  do e' <- cbnlet1 e1
     return (App e' e2)

-- Otherwise terminated or blocked
cbnlet1 _ = Nothing

-- List of all cbnlet1 reduced expressions
cbnlet1ReducedList :: Maybe LExpr -> Int -> Int -> [Maybe LExpr]
cbnlet1ReducedList (Just expression) count upperBound = generateList (Just expression) count
  where
    generateList expr e
      | e >= upperBound = [expr]
      | otherwise = case cbnlet1 =<< expr of
          Just expr' -> expr : generateList (Just expr') (e + 1)
          Nothing -> [expr]
cbnlet1ReducedList Nothing _ _ = []

-- List of all cbnlam1 reduced expressions
cbnlam1ReducedList :: LamExpr -> Int -> Int -> [LamExpr]
cbnlam1ReducedList expression count upperBound = generateList expression count
  where
    generateList expr e
      | e >= upperBound = [expr]
      | otherwise = case cbnlam1 expr of
          Just expr' -> expr : generateList expr' (e + 1)
          Nothing -> [expr]



