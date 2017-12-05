# A7-Programming-Language-Interpreter

A compiler I made using Java as the intermediate language (so there is no assembly code generation, thankfully).

The grammar can be found in the file A7 Programming Language Grammar. Each rule can be visualized as a tree structure. For example, the first rule: 'program = keywordProg braceBlock' can be seen as a sub-tree with program as the root and two child nodes: "keywordProg" and "braceBlock".

This compiler is done via LL parsing. It is a nonbacktracking parser. Initially the grammar contained direct left recursion, but I eliminated this via Left Recursion Elimination to simplify the code. LL parsing is driven by the LL Parse Table which uses the grammar to create a first set and follow set.

The compiler initially takes in source code as input through the console, and the end of input is indicated by the character '$' on a new line. This input is then sent to the Lexer which tokenizes the input into the relevant tokens. After this, a parse tree is created via the LL Parse Table (which can be found in the form of an excel file named LL Parse Table). The parse tree is then converted to an abstract syntax tree. The scope tree is then created from a traversal of the abstract syntax tree. Finally, the abstract syntax tree is then traversed a second time to "run" the input code. 

Some example input/output can be found in the Test Programs file.


