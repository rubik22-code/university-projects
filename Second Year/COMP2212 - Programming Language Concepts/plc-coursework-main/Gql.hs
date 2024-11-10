import System.Environment (getArgs)
import Tokens
import Grammar
import Data.Char (isSpace, toUpper)
import Data.List (last, isInfixOf, isPrefixOf)
import Text.Read (readMaybe)
import Data.Char (toLower)
import Data.List (intercalate)

type Graph = ([Node], [RelationshipNode])

-- Represents a node from the input file
data Node = Node {
    nodeId :: String,
    nodeProperties :: [(String,String)],
    nodeLabels :: [String]
} deriving (Show)

-- Represents a relationship from the input file
data RelationshipNode = RelationshipNode {
    startId :: String,
    relationshipProperties :: [(String,String)],
    endId :: String,
    relationshipType :: String
} deriving (Show)

-- Main function where the input n4j and program file get read
main :: IO ()
main = do
    args <- getArgs
    let programName = head args
    programContents <- readFile programName
    let tokens = alexScanTokens programContents
    let ast = parseQuery tokens
    
    case ast of
        (FindQuery _ (FromClause inputFile) _) -> do
            graph <- loadGraph inputFile
            let modifiedGraph = executeQuery ast graph
            let selectedProperties = getSelectedProperties ast
            outputGraph selectedProperties modifiedGraph
        (FindWhereQuery _ (FromClause inputFile) _ _) -> do
            graph <- loadGraph inputFile
            let modifiedGraph = executeQuery ast graph
            let selectedProperties = getSelectedProperties ast
            outputGraph selectedProperties modifiedGraph
        (UpdateQuery _ (FromClause inputFile) _ _) -> do
            graph <- loadGraph inputFile
            let modifiedGraph = executeQuery ast graph
            let selectedProperties = getSelectedProperties ast
            outputGraph selectedProperties modifiedGraph
        (UpdateReturnQuery _ (FromClause inputFile) _ _ _) -> do
            graph <- loadGraph inputFile
            let modifiedGraph = executeQuery ast graph
            let selectedProperties = getSelectedProperties ast
            outputGraph selectedProperties modifiedGraph
        (FindCreateQuery _ (FromClause inputFile) _ _) -> do
            graph <- loadGraph inputFile
            let modifiedGraph = executeQuery ast graph
            let selectedProperties = getSelectedProperties ast
            outputGraph selectedProperties modifiedGraph
        (FindWhereCreateQuery _ (FromClause inputFile) _ _ _) -> do
            graph <- loadGraph inputFile
            let modifiedGraph = executeQuery ast graph
            let selectedProperties = getSelectedProperties ast
            outputGraph selectedProperties modifiedGraph
        (DeleteQuery _ (FromClause inputFile) _ _) -> do
            graph <- loadGraph inputFile
            let modifiedGraph = executeQuery ast graph
            let selectedProperties = getSelectedProperties ast
            outputGraph selectedProperties modifiedGraph

-- Parse the contents and build the graph data structure
loadGraph filename = do
    contents <- readFile filename -- reads the input file
    let ls = lines contents -- splits the input in different lines
        ns = createNodes ls -- parses all the nodes
        rs = createRelationshipNodes ls -- parses all the relationships
    return $ buildGraph ns rs

-- Creates the normal nodes
createNodes :: [String] -> [Node]
createNodes lines = concatMap formatNodes tables
  where
    tables = splitTables lines
    formatNodes (header, content) = concatMap (formatNode header) content

-- Splits each of the tables and headings
splitTables :: [String] -> [(String, [String])]
splitTables [] = []
splitTables lines =
    let (table, rest) = break Prelude.null lines
        nonEmptyRest = dropWhile Prelude.null rest
        header = head table
        content = tail table
    in if ":START_ID" `isInfixOf` header
           then splitTables nonEmptyRest  -- Stops processing the current table and moves on to the next one
           else (header, content) : splitTables nonEmptyRest

-- It creates the list of nodes that contain all of the information about a record
formatNode :: String -> String -> [Node]
formatNode header line = do
    let headerFields =  Prelude.drop 1 $ splitOn ',' header -- Splits each element of the header excluding the id column
        contentFields = splitOn ',' line -- Splits each element of the content
        nodeId = trim $ head contentFields -- Gets the nodeId
        props = if ":LABEL" `isInfixOf` last headerFields -- Checks if the table has a label
                then zipWith (\h v -> (trim h, trim v)) (init headerFields) (init $ tail contentFields) -- If it does it combines the init header and value from the content (exclusing the first and last value) in a tuple to create a property
                else zipWith (\h v -> (trim h, trim v)) headerFields (tail contentFields) -- Otherwise it combines them in their entirety
        labels = if ":LABEL" `isInfixOf` last headerFields -- Checks if there is a label column in the table
                 then Prelude.map trim $ splitOn ';' (last contentFields) -- If there is split all of the labels by ;
                 else [] -- Otherwise return empty
    [Node nodeId props labels] -- Return the list of nodes

-- Creates the relationship nodes
createRelationshipNodes :: [String] -> [RelationshipNode]
createRelationshipNodes lines = concatMap formatNodes tables
  where
    tables = splitRelationshipTables lines
    formatNodes (header, content) = concatMap (formatRelationshipNode header) content

-- -- Find tables based on headers and not empty space (not working)
-- splitRelationshipTables :: [String] -> [(String, [String])]
-- splitRelationshipTables [] = []
-- splitRelationshipTables lines =
--     let (header, contentAndRest) = break (\line -> ":ID" `isPrefixOf` line || ":START_ID" `isPrefixOf` line) lines
--         content = Prelude.drop 1 contentAndRest
--         nonEmptyRest = content
--     in if not (Prelude.null content)  -- If there's content after skipping the header
--            then (concat header, content) : splitRelationshipTables nonEmptyRest
--            else []  -- No more tables found

-- Splits each of the relationship tables and headings
splitRelationshipTables :: [String] -> [(String, [String])]
splitRelationshipTables [] = []
splitRelationshipTables lines =
    let (table, rest) = break Prelude.null lines
        nonEmptyRest = dropWhile Prelude.null rest
        header = head table
        content = tail table
    in if ":START_ID" `isInfixOf` header
           then (header, content) : splitRelationshipTables nonEmptyRest
           else splitRelationshipTables nonEmptyRest

-- It creates the list of nodes that contain all of the information about a record
formatRelationshipNode :: String -> String -> [RelationshipNode]
formatRelationshipNode header line = do
    let headerFields = (reverse . Prelude.drop 2 . reverse) $ Prelude.drop 1 $ splitOn ',' header
        contentFields = splitOn ',' line
        startId = trim $ head contentFields
        endId = trim . (!! 1) . reverse $ contentFields
        relType = trim $ last contentFields
        props = zipWith (\h v -> (trim h, trim v)) headerFields $ (reverse . Prelude.drop 2 . reverse) $ Prelude.drop 1 contentFields
    [RelationshipNode startId props endId relType]

-- Helper function that removes whitespace at the beggining and at the end
trim :: String -> String
trim = f . f -- Applies it twice
   where f = reverse . dropWhile isSpace

-- Splits on a specified char
splitOn :: Char -> String -> [String]
splitOn c = Prelude.foldr (\x (y:ys) -> if x == c then []:y:ys else (x:y):ys) [[]]

-- Combines the relationship and normal nodes into a graph
buildGraph :: [Node] -> [RelationshipNode] -> Graph
buildGraph ns rs = (ns, rs)

--------------------

-- Executes the query on the graph
executeQuery :: Query -> Graph -> Graph
executeQuery (FindWhereQuery findClause fromClause whereClause returnClause) (nodes, relationships) = do
  let filteredNodes = filterNodes findClause whereClause nodes
      filteredRelationships = filterRelationships findClause whereClause relationships
  returnNodesAndRelationships returnClause filteredNodes filteredRelationships
executeQuery _ graph = graph

-- Filters the nodes based on a predicate
filterNodes :: FindClause -> WhereClause -> [Node] -> [Node]
filterNodes (FindClauseIdentifier [IdentifierParens _]) (WhereClause condition) nodes = filter (evaluateCondition condition) nodes
filterNodes _ _ nodes = nodes

-- Filters the realtionship nodes based on a predicate
filterRelationships :: FindClause -> WhereClause -> [RelationshipNode] -> [RelationshipNode]
filterRelationships (FindClauseIdentifier [IdentifierParens _]) (WhereClause condition) relationships = filter (evaluateRelationshipCondition condition) relationships
filterRelationships _ _ relationships = relationships

evaluateCondition :: Condition -> Node -> Bool
evaluateCondition (ConditionOr cond1 cond2) node = evaluateCondition cond1 node || evaluateCondition cond2 node
evaluateCondition (ConditionAnd cond1 cond2) node = evaluateCondition cond1 node && evaluateCondition cond2 node
evaluateCondition (ConditionIdentifier (IdentifierLabel ident labels)) node = any (`elem` nodeLabels node) (map show labels)
evaluateCondition (ConditionIdentifier (IdentifierParens _)) node = True -- Return true for any node
evaluateCondition (ConditionProperty (IdentifierPattern ident) prop (ComparisonEquals value)) node = case findStringProperty node prop of
  Just propValue -> case value of
    StringValue str -> propValue == str
    IntegerValue int -> case readMaybe (dropWhile (/= ':') propValue) of
      Just numericValue -> numericValue == fromIntegral int
      Nothing -> False
  Nothing -> False
evaluateCondition (ConditionProperty (IdentifierPattern ident) prop (ComparisonStartsWith value)) node = case findStringProperty node prop of
  Just propValue -> booleanStartsWith propValue value
  Nothing -> False
evaluateCondition (ConditionProperty (IdentifierPattern ident) "age" (ComparisonLessThan value)) node = case findNumericProperty node "age:integer" of
  Just numericValue -> case value of
    IntegerValue int -> numericValue < fromIntegral int
    _ -> False
  Nothing -> False
evaluateCondition (ConditionProperty (IdentifierPattern ident) "age" (ComparisonGreaterThan value)) node = case findNumericProperty node "age:integer" of
  Just numericValue -> case value of
    IntegerValue int -> numericValue > fromIntegral int
    _ -> False
  Nothing -> False
evaluateCondition (ConditionProperty (IdentifierPattern ident) "age" (ComparisonLessThanOrEqual value)) node = case findNumericProperty node "age:integer" of
  Just numericValue -> case value of
    IntegerValue int -> numericValue <= fromIntegral int
    _ -> False
  Nothing -> False
evaluateCondition (ConditionProperty (IdentifierPattern ident) "age" (ComparisonGreaterThanOrEqual value)) node = case findNumericProperty node "age:integer" of
  Just numericValue -> case value of
    IntegerValue int -> numericValue >= fromIntegral int
    _ -> False
  Nothing -> False
evaluateCondition (ConditionProperty (IdentifierPattern ident) prop (ComparisonLessThan value)) node = case findNumericProperty node prop of
  Just numericValue -> case value of
    IntegerValue int -> numericValue < fromIntegral int
    _ -> False
  Nothing -> False
evaluateCondition (ConditionProperty (IdentifierPattern ident) prop (ComparisonGreaterThan value)) node = case findNumericProperty node prop of
  Just numericValue -> case value of
    IntegerValue int -> numericValue > fromIntegral int
    _ -> False
  Nothing -> False
evaluateCondition (ConditionProperty (IdentifierPattern ident) prop (ComparisonLessThanOrEqual value)) node = case findNumericProperty node prop of
  Just numericValue -> case value of
    IntegerValue int -> numericValue <= fromIntegral int
    _ -> False
  Nothing -> False
evaluateCondition (ConditionProperty (IdentifierPattern ident) prop (ComparisonGreaterThanOrEqual value)) node = case findNumericProperty node prop of
  Just numericValue -> case value of
    IntegerValue int -> numericValue >= fromIntegral int
    _ -> False
  Nothing -> False
evaluateCondition _ _ = True

evaluateRelationshipCondition :: Condition -> RelationshipNode -> Bool
evaluateRelationshipCondition (ConditionOr cond1 cond2) relationship = evaluateRelationshipCondition cond1 relationship || evaluateRelationshipCondition cond2 relationship
evaluateRelationshipCondition (ConditionAnd cond1 cond2) relationship = evaluateRelationshipCondition cond1 relationship && evaluateRelationshipCondition cond2 relationship
evaluateRelationshipCondition (ConditionIdentifier _) _ = True -- Return true for any relationship
evaluateRelationshipCondition (ConditionProperty (IdentifierPattern ident) "relationshipType" (ComparisonEquals value)) relationship = case value of
  StringValue str -> relationshipType relationship == str
  _ -> False
evaluateRelationshipCondition (ConditionProperty (IdentifierPattern ident) prop (ComparisonEquals value)) relationship = case lookup prop (relationshipProperties relationship) of
  Just propValue -> case value of
    StringValue str -> propValue == str
    IntegerValue int -> case readMaybe propValue of
      Just numericValue -> fromIntegral int == numericValue
      Nothing -> False
  Nothing -> False
evaluateRelationshipCondition (ConditionProperty (IdentifierPattern ident) prop (ComparisonStartsWith value)) relationship = case lookup prop (relationshipProperties relationship) of
  Just propValue -> booleanStartsWith propValue value
  Nothing -> False
evaluateRelationshipCondition (ConditionProperty (IdentifierPattern ident) prop (ComparisonLessThan value)) relationship = case findNumericProperty relationship prop of
  Just numericValue -> case value of
    IntegerValue int -> numericValue < fromIntegral int
    _ -> False
  Nothing -> False
evaluateRelationshipCondition (ConditionProperty (IdentifierPattern ident) prop (ComparisonGreaterThan value)) relationship = case findNumericProperty relationship prop of
  Just numericValue -> case value of
    IntegerValue int -> numericValue > fromIntegral int
    _ -> False
  Nothing -> False
evaluateRelationshipCondition (ConditionProperty (IdentifierPattern ident) prop (ComparisonLessThanOrEqual value)) relationship = case findNumericProperty relationship prop of
  Just numericValue -> case value of
    IntegerValue int -> numericValue <= fromIntegral int
    _ -> False
  Nothing -> False
evaluateRelationshipCondition (ConditionProperty (IdentifierPattern ident) prop (ComparisonGreaterThanOrEqual value)) relationship = case findNumericProperty relationship prop of
  Just numericValue -> case value of
    IntegerValue int -> numericValue >= fromIntegral int
    _ -> False
  Nothing -> False
evaluateRelationshipCondition _ _ = True

class HasProperties a where
  findStringProperty :: a -> String -> Maybe String

instance HasProperties Node where
  findStringProperty node prop = lookup prop (nodeProperties node)

instance HasProperties RelationshipNode where
  findStringProperty relationship prop = lookup prop (relationshipProperties relationship)

findNumericProperty :: HasProperties a => a -> String -> Maybe Double
findNumericProperty node prop = findStringProperty node prop >>= readMaybe

booleanStartsWith :: String -> String -> Bool
booleanStartsWith str [] = True
booleanStartsWith [] _ = False
booleanStartsWith str value@(c:cs)
  | length str >= 2 && head str == '"' && last str == '"' && length value > 0 =
      let str' = tail (init str)
      in booleanStartsWith str' value
  | toLower (head str) == toLower c = booleanStartsWith (tail str) cs
  | otherwise = False

returnNodesAndRelationships :: ReturnClause -> [Node] -> [RelationshipNode] -> Graph
returnNodesAndRelationships (ReturnClause [ReturnIdentifier ident]) nodes relationships = (nodes, relationships)
returnNodesAndRelationships _ nodes relationships = (nodes, relationships) 

--------------------------------

-- Output the filtered graph in the desired format
outputGraph :: [String] -> Graph -> IO ()
outputGraph selectedProperties (nodes, _) = do
    let header = intercalate ", " (map formatHeader selectedProperties)
    let rows = map (nodeToRow selectedProperties) nodes
    let output = unlines (header : rows)
    putStrLn output

nodeToRow :: [String] -> Node -> String
nodeToRow selectedProperties node =
    let propertyValues = map (getPropertyValue node) selectedProperties
    in intercalate ", " propertyValues

getPropertyValue :: Node -> String -> String
getPropertyValue node property
    | map toUpper property `elem` ["ID", "LABEL"] = case map toUpper property of
        "ID" -> nodeId node
        "LABEL" -> intercalate "," (nodeLabels node)
        _ -> ""
    | otherwise = case lookup (property ++ ":integer") (nodeProperties node) of
        Just value -> value
        Nothing -> case lookup (property ++ ":string") (nodeProperties node) of
            Just value -> value
            Nothing -> "null"

formatHeader :: String -> String
formatHeader header
    | map toUpper header `elem` ["ID", "LABEL"] = case map toUpper header of
        "ID" -> ":ID"
        "LABEL" -> ":LABEL"
        _ -> header
    | otherwise = header

getSelectedProperties :: Query -> [String]
getSelectedProperties query =
    case query of
        FindQuery _ _ (ReturnClause returnElements) -> extractReturnElements returnElements
        FindWhereQuery _ _ _ (ReturnClause returnElements) -> extractReturnElements returnElements
        UpdateQuery _ _ _ (ReturnClause returnElements) -> extractReturnElements returnElements
        UpdateReturnQuery _ _ _ _ (ReturnClause returnElements) -> extractReturnElements returnElements
        FindCreateQuery _ _ _ (ReturnClause returnElements) -> extractReturnElements returnElements
        FindWhereCreateQuery _ _ _ _ (ReturnClause returnElements) -> extractReturnElements returnElements
        DeleteQuery _ _ _ (ReturnClause returnElements) -> extractReturnElements returnElements
    where
        extractReturnElements :: [ReturnElement] -> [String]
        extractReturnElements = concatMap extractReturnElement

        extractReturnElement :: ReturnElement -> [String]
        extractReturnElement (ReturnIdentifier ident) = []
        extractReturnElement (ReturnProperty _ prop) = [prop]
        extractReturnElement (ReturnPropertyAs _ prop _) = [prop]
        extractReturnElement (ReturnRelationship (RelationshipPatternTypeProperies (IdentifierPattern startIdent) _ _ _ (IdentifierPattern endIdent))) = extractVarWithType startIdent ++ extractVarWithType endIdent
        extractReturnElement (ReturnRelationship (RelationshipPatternType (IdentifierPattern startIdent) _ _ (IdentifierPattern endIdent))) = extractVarWithType startIdent ++ extractVarWithType endIdent
        extractReturnElement (ReturnRelationship (RelationshipType (IdentifierPattern startIdent) _ (IdentifierPattern endIdent))) = extractVarWithType startIdent ++ extractVarWithType endIdent
        extractReturnElement (ReturnRelationship (RelationshipNoType (IdentifierPattern startIdent) _ _ (IdentifierPattern endIdent))) = extractVarWithType startIdent ++ extractVarWithType endIdent
        extractReturnElement (ReturnRelationship (RelationshipPattern (IdentifierPattern startIdent) _ (IdentifierPattern endIdent))) = extractVarWithType startIdent ++ extractVarWithType endIdent
        extractReturnElement (ReturnRelationship (RelationshipProperty (IdentifierPattern startIdent) _ (IdentifierPattern endIdent))) = extractVarWithType startIdent ++ extractVarWithType endIdent
        extractReturnElement (ReturnRelationship (RevRelationshipPatternTypeProperies (IdentifierPattern startIdent) _ _ _ (IdentifierPattern endIdent))) = extractVarWithType startIdent ++ extractVarWithType endIdent
        extractReturnElement (ReturnRelationship (RevRelationshipPatternType (IdentifierPattern startIdent) _ _ (IdentifierPattern endIdent))) = extractVarWithType startIdent ++ extractVarWithType endIdent
        extractReturnElement (ReturnRelationship (RevRelationshipType (IdentifierPattern startIdent) _ (IdentifierPattern endIdent))) = extractVarWithType startIdent ++ extractVarWithType endIdent
        extractReturnElement (ReturnRelationship (RevRelationshipNoType (IdentifierPattern startIdent) _ _ (IdentifierPattern endIdent))) = extractVarWithType startIdent ++ extractVarWithType endIdent
        extractReturnElement (ReturnRelationship (RevRelationshipPattern (IdentifierPattern startIdent) _ (IdentifierPattern endIdent))) = extractVarWithType startIdent ++ extractVarWithType endIdent
        extractReturnElement (ReturnRelationship (RevRelationshipProperty (IdentifierPattern startIdent) _ (IdentifierPattern endIdent))) = extractVarWithType startIdent ++ extractVarWithType endIdent
        extractReturnElement (ReturnRelationship (NoRelationshipPatternTypeProperies (IdentifierPattern startIdent) _ _ _ (IdentifierPattern endIdent))) = extractVarWithType startIdent ++ extractVarWithType endIdent
        extractReturnElement (ReturnRelationship (NoRelationshipPatternType (IdentifierPattern startIdent) _ _ (IdentifierPattern endIdent))) = extractVarWithType startIdent ++ extractVarWithType endIdent
        extractReturnElement (ReturnRelationship (NoRelationshipType (IdentifierPattern startIdent) _ (IdentifierPattern endIdent))) = extractVarWithType startIdent ++ extractVarWithType endIdent
        extractReturnElement (ReturnRelationship (NoRelationshipNoType (IdentifierPattern startIdent) _ _ (IdentifierPattern endIdent))) = extractVarWithType startIdent ++ extractVarWithType endIdent
        extractReturnElement (ReturnRelationship (NoRelationshipPattern (IdentifierPattern startIdent) _ (IdentifierPattern endIdent))) = extractVarWithType startIdent ++ extractVarWithType endIdent
        extractReturnElement (ReturnRelationship (NoRelationshipProperty (IdentifierPattern startIdent) _ (IdentifierPattern endIdent))) = extractVarWithType startIdent ++ extractVarWithType endIdent

        extractVarWithType :: String -> [String]
        extractVarWithType ident =
            case splitOn '.' ident of
                [_, varWithType] -> [varWithType]
                _ -> []