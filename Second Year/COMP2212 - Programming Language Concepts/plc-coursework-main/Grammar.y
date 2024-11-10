{
module Grammar where
import Tokens
}

%name parseQuery
%tokentype { Token }
%error { parseError }


%left OR
%left AND
%right NOT
%nonassoc '=' STARTS_WITH '<' '>' '<=' '>='

%token
  FIND          { TokenFind _ }
  FROM          { TokenFrom _ }
  RETURN        { TokenReturn _ }
  WHERE         { TokenWhere _ }
  AS            { TokenAs _ }
  AND           { TokenAnd _ }
  OR            { TokenOr _ }
  STARTS_WITH   { TokenStartsWith _ }
  UPDATE        { TokenUpdate _ }
  CREATE        { TokenCreate _ }
  DELETE        { TokenDelete _ }
  NOT           { TokenNot _ }
  '-'           { TokenDash _ }
  '['           { TokenLBracket _ }
  ']'           { TokenRBracket _ }
  '{'           { TokenLBrace _ }
  '}'           { TokenRBrace _ }
  ':'           { TokenColon _ }
  '('           { TokenLParen _ }
  ')'           { TokenRParen _ }
  ','           { TokenComma _ }
  '='           { TokenEquals _ }
  '.'           { TokenDot _ }
  '>'           { TokenGreater _ }
  '<'           { TokenLess _ }
  '<='          { TokenLessEquals _ }
  '>='          { TokenGreaterEquals _ }
  STRING        { TokenString _ $$ }
  IDENT         { TokenIdentifier _ $$ }
  INTEGER       { TokenInteger _ $$ }

%%

Query : FindClause FromClause ReturnClause                              { FindQuery $1 $2 $3 }
      | FindClause FromClause WhereClause ReturnClause                  { FindWhereQuery $1 $2 $3 $4}
      | FindClause FromClause UpdateClause ReturnClause                 { UpdateQuery $1 $2 $3 $4}
      | FindClause FromClause WhereClause UpdateClause ReturnClause     { UpdateReturnQuery $1 $2 $3 $4 $5}
      | FindClause FromClause CreateClause ReturnClause                 { FindCreateQuery $1 $2 $3 $4}
      | FindClause FromClause WhereClause CreateClause ReturnClause     { FindWhereCreateQuery $1 $2 $3 $4 $5 }
      | FindClause FromClause DeleteClause ReturnClause                 { DeleteQuery $1 $2 $3 $4 }

-----------------------------

FindClause : FIND IdentifierList                                        { FindClauseIdentifier $2 }
           | FIND IdentifierList RelationshipList                       { FindClauseIdentifierRelationship $2 $3 }
           | FIND RelationshipList                                      { FindClauseRelationship $2 }

FromClause : FROM STRING                                                { FromClause $2 }

ReturnClause : RETURN ReturnElements                                    { ReturnClause $2 }

WhereClause : WHERE Condition                                           { WhereClause $2 }

CreateClause : CREATE RelationshipPattern                               { CreateClause $2 }

UpdateClause : UPDATE UpdateElements                                 { UpdateClause $2 }

DeleteClause : DELETE RelationshipPattern                               { DeleteClause $2 }

-----------------------------

IdentifierList : IdentifierPattern                                      { [$1] }
               | IdentifierPattern ',' IdentifierList                   { $1 : $3 }

IdentifierPattern : IDENT                                               { IdentifierPattern $1 }
                  | '(' IDENT ')'                                       { IdentifierParens $2 }
                  | '(' IDENT ':' Labels ')'                            { IdentifierLabel $2 $4 }
                  | '(' IDENT ':' Labels PropertyList')'                { IdentifierLabelProperty $2 $4 $5 }
                  | '(' IDENT PropertyList ')'                          { IdentifierProperties $2 $3 }

Labels : Label                                                          { [$1] }
       | Label ':' Labels                                               { $1 : $3 }

Label : IDENT                                                           { Label $1 }

--

RelationshipList: RelationshipPattern                                   { [$1] }
                | RelationshipPattern ',' RelationshipList              { $1 : $3 }

RelationshipPattern : IdentifierPattern '-' '[' IDENT ':' Type PropertyList ']' '-' '>' IdentifierPattern   { RelationshipPatternTypeProperies $1 $4 $6 $7 $11 }
                    | IdentifierPattern '-' '[' IDENT ':' Type ']' '-' '>' IdentifierPattern                { RelationshipPatternType $1 $4 $6 $10 }
                    | IdentifierPattern '-' '[' ':' Type ']' '-' '>' IdentifierPattern                      { RelationshipType $1 $5 $9 }
                    | IdentifierPattern '-' '[' IDENT PropertyList ']' '-' '>' IdentifierPattern            { RelationshipNoType $1 $4 $5 $9 }
                    | IdentifierPattern '-' '[' IDENT ']' '-' '>' IdentifierPattern                         { RelationshipPattern $1 $4 $8 }
                    | IdentifierPattern '-' '[' PropertyList ']' '-' '>' IdentifierPattern                  { RelationshipProperty $1 $4 $8 }
                    | IdentifierPattern '<' '-' '[' IDENT ':' Type PropertyList ']' '-' IdentifierPattern   { RevRelationshipPatternTypeProperies $1 $5 $7 $8 $11 }
                    | IdentifierPattern '<' '-' '[' IDENT ':' Type ']' '-' IdentifierPattern                { RevRelationshipPatternType $1 $5 $7 $10 }
                    | IdentifierPattern '<' '-' '[' ':' Type ']' '-' IdentifierPattern                      { RevRelationshipType $1 $6 $9 }
                    | IdentifierPattern '<' '-' '[' IDENT PropertyList ']' '-' IdentifierPattern            { RevRelationshipNoType $1 $5 $6 $9 }
                    | IdentifierPattern '<' '-' '[' IDENT ']' '-' IdentifierPattern                         { RevRelationshipPattern $1 $5 $8 }
                    | IdentifierPattern '<' '-' '[' PropertyList ']' '-' IdentifierPattern                  { RevRelationshipProperty $1 $5 $8 }
                    | IdentifierPattern '-' '[' IDENT ':' Type PropertyList ']' '-' IdentifierPattern       { NoRelationshipPatternTypeProperies $1 $4 $6 $7 $10 }
                    | IdentifierPattern '-' '[' IDENT ':' Type ']' '-' IdentifierPattern                    { NoRelationshipPatternType $1 $4 $6 $9 }
                    | IdentifierPattern '-' '[' ':' Type ']' '-' IdentifierPattern                          { NoRelationshipType $1 $5 $8 }
                    | IdentifierPattern '-' '[' IDENT PropertyList ']' '-' IdentifierPattern                { NoRelationshipNoType $1 $4 $5 $8 }
                    | IdentifierPattern '-' '[' IDENT ']' '-' IdentifierPattern                             { NoRelationshipPattern $1 $4 $7 }
                    | IdentifierPattern '-' '[' PropertyList ']' '-' IdentifierPattern                      { NoRelationshipProperty $1 $4 $7 }

--

PropertyList : '{' Properties '}'                                       { PropertyList $2 }

Properties : Property                                                   { [$1] }
           | Property ',' Properties                                    { $1 : $3 }

Property : Name '=' Value                                              { Property $1 $3 }
         | Name                                                        { PropertyName $1 }

--

Name : IDENT                                                            { Name $1 }

Type : IDENT                                                            { Type $1 }

--

ValueList : Value                                                       { [$1] }
          | Value ',' ValueList                                         { $1 : $3 }

Value : STRING                                                          { StringValue $1 }
      | INTEGER                                                         { IntegerValue $1 }
      | IDENT                                                           { IdentValue $1 }
      | '[' ValueList ']'                                               { ListValue $2 }

Condition : Comparison                                                  { ConditionComparison $1 }
          | NOT '(' Condition ')'                                       { ConditionNegated $3 }
          | Condition AND Condition                                     { ConditionAnd $1 $3 }
          | Condition OR Condition                                      { ConditionOr $1 $3 }
          | IdentifierPattern                                           { ConditionIdentifier $1}
          | IdentifierPattern '.' IDENT Comparison                      { ConditionProperty $1 $3 $4 }

Comparison : '=' Value                                                  { ComparisonEquals $2 }
           | STARTS_WITH STRING                                         { ComparisonStartsWith $2 }
           | '<' Value                                                  { ComparisonLessThan $2 }
           | '>' Value                                                  { ComparisonGreaterThan $2 }
           | '<=' Value                                                 { ComparisonLessThanOrEqual $2 }
           | '>=' Value                                                 { ComparisonGreaterThanOrEqual $2 }

UpdateElements : UpdateElement                                          { [$1] }
               | UpdateElement ',' UpdateElements                       { $1 : $3 }

UpdateElement : IDENT '.' IDENT '=' Value                               { UpdateElem $1 $3 $5 }

ReturnElements : ReturnElement                                          { [$1] }
               | ReturnElement ',' ReturnElements                       { $1 : $3 }

ReturnElement : IDENT                                                   { ReturnIdentifier $1 }
              | IDENT '.' IDENT                                         { ReturnProperty $1 $3 }
              | IDENT '.' IDENT AS STRING                                { ReturnPropertyAs $1 $3 $5 }
              | RelationshipPattern                                     { ReturnRelationship $1 }
{

parseError :: [Token] -> a
parseError [] = error "Unknown Parse Error" 
parseError (t:ts) = error ("Parse error at line:column " ++ (tokenPosn t))

data Query = FindQuery FindClause FromClause ReturnClause
           | FindWhereQuery FindClause FromClause WhereClause ReturnClause
           | UpdateQuery FindClause FromClause UpdateClause ReturnClause
           | UpdateReturnQuery FindClause FromClause WhereClause UpdateClause ReturnClause
           | FindCreateQuery FindClause FromClause CreateClause ReturnClause
           | FindWhereCreateQuery FindClause FromClause WhereClause CreateClause ReturnClause
           | DeleteQuery FindClause FromClause DeleteClause ReturnClause
           deriving (Show)

data FindClause = FindClauseIdentifier [IdentifierPattern]
                | FindClauseIdentifierRelationship [IdentifierPattern] [RelationshipPattern]
                | FindClauseRelationship [RelationshipPattern]
                deriving (Show)

data FromClause = FromClause String deriving (Show)

data ReturnClause = ReturnClause [ReturnElement] deriving (Show)

data WhereClause = WhereClause Condition deriving (Show)

data CreateClause = CreateClause RelationshipPattern deriving (Show)

data UpdateClause = UpdateClause [UpdateElement] deriving (Show)

data DeleteClause = DeleteClause RelationshipPattern deriving (Show)

data IdentifierPattern = IdentifierPattern String
                       | IdentifierParens String
                       | IdentifierLabel String [Label]
                       | IdentifierLabelProperty String [Label] PropertyList
                       | IdentifierProperties String PropertyList
                       deriving (Show)

data RelationshipPattern = RelationshipPatternTypeProperies IdentifierPattern String Type PropertyList IdentifierPattern
                         | RelationshipPatternType IdentifierPattern String Type IdentifierPattern
                         | RelationshipType IdentifierPattern Type IdentifierPattern
                         | RelationshipNoType IdentifierPattern String PropertyList IdentifierPattern
                         | RelationshipPattern IdentifierPattern String IdentifierPattern
                         | RelationshipProperty IdentifierPattern PropertyList IdentifierPattern
                         | RevRelationshipPatternTypeProperies IdentifierPattern String Type PropertyList IdentifierPattern
                         | RevRelationshipPatternType IdentifierPattern String Type IdentifierPattern
                         | RevRelationshipType IdentifierPattern Type IdentifierPattern
                         | RevRelationshipNoType IdentifierPattern String PropertyList IdentifierPattern
                         | RevRelationshipPattern IdentifierPattern String IdentifierPattern
                         | RevRelationshipProperty IdentifierPattern PropertyList IdentifierPattern
                         | NoRelationshipPatternTypeProperies IdentifierPattern String Type PropertyList IdentifierPattern
                         | NoRelationshipPatternType IdentifierPattern String Type IdentifierPattern
                         | NoRelationshipType IdentifierPattern Type IdentifierPattern
                         | NoRelationshipNoType IdentifierPattern String PropertyList IdentifierPattern
                         | NoRelationshipPattern IdentifierPattern String IdentifierPattern
                         | NoRelationshipProperty IdentifierPattern PropertyList IdentifierPattern
                         deriving (Show)

data PropertyList = PropertyList [Property] deriving (Show)

data Property = Property Name Value 
              | PropertyName Name 
              deriving (Show)

data Name = Name String deriving (Show)

data Type = Type String deriving (Show)

data Label = Label String deriving (Show)

data Value = StringValue String
           | IntegerValue Int
           | IdentValue String
           | ListValue [Value]
           deriving (Show)

data Condition = ConditionComparison Comparison
               | ConditionAnd Condition Condition
               | ConditionOr Condition Condition
               | ConditionIdentifier IdentifierPattern
               | ConditionProperty IdentifierPattern String Comparison
               | ConditionNegated Condition
               | ConditionStartsWith String String String
               deriving (Show)

data Comparison = ComparisonEquals Value
                | ComparisonStartsWith String
                | ComparisonLessThan Value
                | ComparisonGreaterThan Value
                | ComparisonLessThanOrEqual Value
                | ComparisonGreaterThanOrEqual Value
                deriving (Show)

data Assignment = Assignment String Value
                | AssignmentIncrement String String String String String String
                deriving (Show)

data UpdateElement = UpdateElem String String Value
                  deriving (Show)

data ReturnElement = ReturnIdentifier String
                   | ReturnProperty String String
                   | ReturnPropertyAs String String String
                   | ReturnRelationship RelationshipPattern
                   deriving (Show)
}