# PLC Coursework

## How to compile and run
### Alex (Lexer)
- cabal install alex (if you dont have it)
- alex Tokens.x which will create Tokens.hs

### Happy (Parser)
- cabal install happy (if you dont have it)
- happy Grammar.y which will create Grammar.hs

### The main Haskell File
- ghc TestHs.hs
- ./TestHs test.txt

## Existing Languages

- Cypher
- SQL

## Keywords

### Retrieval

#### FIND
Retrieves nodes and relationships from the graph based on specified conditions.

##### Retrieve all nodes
```
FIND (n) 
FROM "access.n4j"
RETURN n;
```

##### Retrieve nodes with a specific label
```
FIND (n:Person)
FROM "access.n4j"
RETURN n;
```

##### Retrieve nodes with multiple labels
```
FIND (n:Person:Happy)
FROM "access.n4j"
RETURN n;
```

##### Retrieve nodes with specific properties
```
FIND (n {name: "Robert"})
FROM "access.n4j"
RETURN n;
```

##### Retrieve nodes with outgoing relationships
```
FIND (n) -[:FOUND]-> (m)
FROM "access.n4j"
RETURN n;
```

##### Retrieve nodes with any outgoing relationships
```
FIND (n) -[*]-> (m)
FROM "access.n4j"
RETURN n;
```

##### Retrieve nodes with outgoing relationships and relationship properties
```
FIND (n) -[:FOUND {where: 'outside'}]-> (m)
FROM "access.n4j"
RETURN n;
```

#### WHERE
Specifies conditions for filtering nodes and relationships.

```
FIND (p:LabelName)
WHERE p.PropertyName = 'Something'
RETURN p.PropertyName, p.NumericProperty;
```

#### ORDER
Sorts the result set based on specified properties.

```
FIND (p:LabelName)
WHERE p.PropertyName = 'Something'
ORDER p.NumericProperty DESC
RETURN p.PropertyName;
```

#### LIMIT
Limits the number of results returned.

```
FIND (p:LabelName)
WHERE p.PropertyName = 'Something'
ORDER p.NumericProperty DESC
LIMIT 10
RETURN p.PropertyName;
```

#### AND, OR
Combines multiple conditions in the WHERE clause.

```
FIND (p:LabelName)
WHERE p.PropertyName = 'Something' OR p.PropertyName = 'Nothing'
ORDER p.NumericProperty DESC
LIMIT 10
RETURN p.PropertyName;
```

```
FIND (p:LabelName)
WHERE p.PropertyName = 'Something' AND p.NumericProperty = 'Nothing'
ORDER p.NumericProperty DESC
LIMIT 10
RETURN p.PropertyName;
```

#### IN
Matches property values against a list of values.

```
FIND (p:LabelName)
WHERE p.PropertyName IN ['value1', 'value2']
ORDER p.NumericProperty DESC
LIMIT 10
RETURN p.PropertyName;
```

#### STARTS WITH, ENDS WITH, HAS
Matches property values based on string patterns.
```
FIND (p:LabelName)
WHERE p.PropertyName STARTS WITH 'prefix'
RETURN p;
```
```
FIND (p:LabelName)
WHERE p.PropertyName ENDS WITH 'suffix'
RETURN p;
```
```
FIND (p:LabelName)
WHERE p.PropertyName HAS 'substring'
RETURN p;
```

#### UPDATE
Modifies property values of nodes and relationships.

```
UPDATE (n:LabelName)
SET n.PropertyName = 'NewValue'
WHERE n.Id = 123;
```
```
UPDATE (n:LabelName) -[r:RelationshipType]-> (m:LabelName)
SET r.PropertyName = 'NewValue'
WHERE n.Id = 123 AND m.Id = 456;
```

#### CREATE, DELETE
Creates or deletes relationships between nodes.

```
CREATE (n:LabelName) -[:RelationshipType]-> (m:LabelName)
WHERE n.Id = 123 AND m.Id = 456;
```
```
DELETE (n:LabelName) -[r:RelationshipType]-> (m:LabelName)
WHERE n.Id = 123 AND m.Id = 456;
```

### Comments
Creates a comment that is ignored by the interpreter

```
-- hello
```