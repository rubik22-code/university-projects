{
module Tokens where
}

%wrapper "posn"

$digit = 0-9
$alpha = [a-zA-Z]
$alphanum = [$alpha $digit]

tokens :-
  $white+                               ;
  "--".*                                ;
  "FIND"                                { \p s -> TokenFind p }
  "FROM"                                { \p s -> TokenFrom p }
  "RETURN"                              { \p s -> TokenReturn p }
  "WHERE"                               { \p s -> TokenWhere p }
  "AS"                                  { \p s -> TokenAs p }
  "AND"                                 { \p s -> TokenAnd p }
  "OR"                                  { \p s -> TokenOr p }
  "STARTS WITH"                         { \p s -> TokenStartsWith p }
  "UPDATE"                              { \p s -> TokenUpdate p }
  "CREATE"                              { \p s -> TokenCreate p }
  "DELETE"                              { \p s -> TokenDelete p }
  "NOT"                                 { \p s -> TokenNot p }
  "-"                                   { \p s -> TokenDash p }
  "["                                   { \p s -> TokenLBracket p }
  "]"                                   { \p s -> TokenRBracket p }
  "{"                                   { \p s -> TokenLBrace p }
  "}"                                   { \p s -> TokenRBrace p }
  ":"                                   { \p s -> TokenColon p }
  "("                                   { \p s -> TokenLParen p }
  ")"                                   { \p s -> TokenRParen p }
  ","                                   { \p s -> TokenComma p }
  "="                                   { \p s -> TokenEquals p }
  "."                                   { \p s -> TokenDot p }
  ">"                                   { \p s -> TokenGreater p}
  "<"                                   { \p s -> TokenLess p}
  "<="                                  { \p s -> TokenLessEquals p}
  ">="                                  { \p s -> TokenGreaterEquals p}
  \"($alphanum | $white | [\.\-\/])+\"   { \p s -> TokenString p (init (tail s)) }
  \'($alphanum | $white)+\'             { \p s -> TokenString p (init (tail s)) }
  $alpha [$alpha $digit \_ \']*         { \p s -> TokenIdentifier p s }
  $digit+                               { \p s -> TokenInteger p (read s) }
  
{

data Token =
    TokenFind AlexPosn
  | TokenFrom AlexPosn
  | TokenReturn AlexPosn
  | TokenWhere AlexPosn
  | TokenAs AlexPosn
  | TokenAnd AlexPosn
  | TokenOr AlexPosn
  | TokenStartsWith AlexPosn
  | TokenUpdate AlexPosn
  | TokenCreate AlexPosn
  | TokenDelete AlexPosn
  | TokenNot AlexPosn
  | TokenDash AlexPosn
  | TokenLBracket AlexPosn
  | TokenRBracket AlexPosn
  | TokenLBrace AlexPosn
  | TokenRBrace AlexPosn
  | TokenColon AlexPosn
  | TokenLParen AlexPosn
  | TokenRParen AlexPosn
  | TokenComma AlexPosn
  | TokenEquals AlexPosn
  | TokenDot AlexPosn
  | TokenGreater AlexPosn
  | TokenLess AlexPosn
  | TokenLessEquals AlexPosn
  | TokenGreaterEquals AlexPosn
  | TokenString AlexPosn String
  | TokenIdentifier AlexPosn String
  | TokenInteger AlexPosn Int
  deriving (Eq, Show)

tokenPosn :: Token -> String
tokenPosn (TokenFind (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenFrom (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenReturn (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenWhere (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenAnd (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenOr (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenStartsWith (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenUpdate (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenCreate (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenDelete (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenDash (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenLBracket (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenRBracket (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenLBrace (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenRBrace (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenColon (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenLParen (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenRParen (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenComma (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenEquals (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenDot (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenGreater (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenLess (AlexPn _ l c)) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenString (AlexPn _ l c) _) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenIdentifier (AlexPn _ l c) _) = show(l) ++ ":" ++ show(c)
tokenPosn (TokenInteger (AlexPn _ l c) _) = show(l) ++ ":" ++ show(c)
}