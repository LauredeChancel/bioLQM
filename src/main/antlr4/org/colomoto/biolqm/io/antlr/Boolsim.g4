grammar Boolsim;

// a model can start with comments and contain empty lines anywhere
model: comment* assign+ ;
comment: '#' ~NEWLINE*? NEWLINE+ ;
assign: expr op var NEWLINE* ;

expr:  expr AND expr       #andExpr
    |  not* var            #simpleExpr
;

op: POSITIVE | NEGATIVE;
not: NOT;
var: ID ;

// spaces and line breaks
WS : [' ' '\t' '\r' ]+ -> channel(HIDDEN);
NEWLINE : '\r'? '\n' ;

fragment LETTER: [a-zA-Z_];
fragment ALPHA: LETTER|'_';
fragment DIGIT: [0-9];
fragment IDENT: ALPHA (ALPHA|DIGIT)* ;


// token definitions
POSITIVE: '->';
NEGATIVE: '-|';
AND: '&';
NOT: '^';
ID: IDENT ;

