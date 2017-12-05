import java.util.*;

/**
 * 
 * @author Michael Sanfelippo, Jeremy Ethridge, Maria Nuila
 * 
 * The Parser utilizes LL parsing which uses the 4 steps taught in class.
 * 
 * It needs an LL parse table which contains the first and follow sets of the grammar, a stack, and a list of Tokens obtained from the Lexer
 *
 */
public class Parser {

	/**
	 * 2d String array to hold the LL parse table
	 */
	private String[][] LLParseTable;
	/**
	 * counter to put a unique id in nodes
	 */
	public int counter = 0;
	/**
	 * stack to hold nodes for the LL parse machine
	 */
	private Stack<PSTNode> stack;
	/**
	 * map to map left hand rules to their corresponding row in the parse table
	 */
	private HashMap<String, Integer> rhsRowNumbers;
	/**
	 * map to map terminals to their corresponding column in the parse table
	 */
	private HashMap<String, Integer> terminalColumnNumbers;
	/**
	 * array to hold each terminal
	 */
	private String[] terminalColumns;
	/**
	 * array to hold each right hand side
	 */
	private String[] rhsRules;
	/**
	 * list of tokens produced by the lexer
	 */
	private ArrayList<Token> tokens;
	/**
	 * the front token
	 */
	private Token front;
	/**
	 * the root of the parse tree, and eventually abstract syntax tree
	 */
	private PSTNode root;

	/**
	 * Sets up the LL parse table based on the grammar, the first set, and the follow set
	 * I can maybe do this thru a text file... consider it when I get back
	 */
	public void setTable(){
		LLParseTable = new String[rhsRules.length][terminalColumns.length];
		for (int i=0; i<LLParseTable.length; i++){
			for (int j=0; j<LLParseTable[0].length; j++){
				LLParseTable[i][j] = "";
			}
		}
		setCell("kprog", "prog", "prog braceBlock");
		setCell("braceBlock", "{", "{ variableGroup statements }");
		setCell("variableGroup", "id", "epsilon");
		setCell("variableGroup", "vars", "vars parenthesesVariableList");
		setCell("variableGroup", "print", "epsilon");
		setCell("variableGroup", "while", "epsilon");
		setCell("parenthesesVariableList", "(", "( variableList )");
		setCell("variableList", ")", "epsilon");
		setCell("variableList", "integer", "variableDeclaration ; variableList");
		setCell("variableList", "float", "variableDeclaration ; variableList");
		setCell("variableList", "string", "variableDeclaration ; variableList");
		setCell("variableDeclaration", "integer", "basekind variableID");
		setCell("variableDeclaration", "float", "basekind variableID");
		setCell("variableDeclaration", "string", "basekind variableID");
		setCell("basekind", "integer", "integer");
		setCell("basekind", "float", "float");
		setCell("basekind", "string", "string");
		setCell("variableID", "id", "id");
		setCell("statements", "}", "epsilon");
		setCell("statements", "id", "statement ; statements");
		setCell("statements", "print", "statement ; statements");
		setCell("statements", "while", "statement ; statements");
		setCell("statement", "id", "statementAssign");
		setCell("statement", "print", "statementPrint");
		setCell("statement", "while", "statementWhile");
		setCell("statementAssign", "id", "variableID = expression");
		setCell("statementPrint", "print", "print parenthesesExpressions");
		setCell("statementWhile", "while", "while parenthesesExpression braceBlock");
		setCell("parenthesesExpressions", "(", "( expressionList )");
		setCell("parenthesesExpression", "(", "( expression )");
		setCell("expressionList", "(", "expression moreExpressions");
		setCell("expressionList", "integer", "expression moreExpressions");
		setCell("expressionList", "float", "expression moreExpressions");
		setCell("expressionList", "string", "expression moreExpressions");
		setCell("expressionList", "id", "expression moreExpressions");
		setCell("moreExpressions", ")", "epsilon");
		setCell("moreExpressions", ",", ", expressionList");
		setCell("expression", "(", "rightTerm expressionOpt");
		setCell("expression", "integer", "rightTerm expressionOpt");
		setCell("expression", "float", "rightTerm expressionOpt");
		setCell("expression", "string", "rightTerm expressionOpt");
		setCell("expression", "id", "rightTerm expressionOpt");
		setCell("expressionOpt", ")", "epsilon");
		setCell("expressionOpt", ",", "epsilon");
		setCell("expressionOpt", ";", "epsilon");
		setCell("expressionOpt", "==", "operatorRelation rightTerm");
		setCell("expressionOpt", "!=", "operatorRelation rightTerm");
		setCell("expressionOpt", ">=", "operatorRelation rightTerm");
		setCell("expressionOpt", "<=", "operatorRelation rightTerm");
		setCell("expressionOpt", ">", "operatorRelation rightTerm");
		setCell("expressionOpt", "<", "operatorRelation rightTerm");
		setCell("rightTerm", "(", "term rightTermOpt");
		setCell("rightTerm", "integer", "term rightTermOpt");
		setCell("rightTerm", "float", "term rightTermOpt");
		setCell("rightTerm", "string", "term rightTermOpt");
		setCell("rightTerm", "id", "term rightTermOpt");
		setCell("rightTermOpt", ")", "epsilon");
		setCell("rightTermOpt", ",", "epsilon");
		setCell("rightTermOpt", "==", "epsilon");
		setCell("rightTermOpt", "!=", "epsilon");
		setCell("rightTermOpt", ">=", "epsilon");
		setCell("rightTermOpt", "<=", "epsilon");
		setCell("rightTermOpt", ">", "epsilon");
		setCell("rightTermOpt", "<", "epsilon");
		setCell("rightTermOpt", ";", "epsilon");
		setCell("rightTermOpt", "+", "operatorAdd term rightTermOpt");
		setCell("rightTermOpt", "-", "operatorAdd term rightTermOpt");
		setCell("term", "(", "fact termOpt");
		setCell("term", "integer", "fact termOpt");
		setCell("term", "float", "fact termOpt");
		setCell("term", "string", "fact termOpt");
		setCell("term", "id", "fact termOpt");
		setCell("termOpt", ")", "epsilon");
		setCell("termOpt", ",", "epsilon");
		setCell("termOpt", "==", "epsilon");
		setCell("termOpt", "!=", "epsilon");
		setCell("termOpt", ">=", "epsilon");
		setCell("termOpt", "<=", "epsilon");
		setCell("termOpt", ">", "epsilon");
		setCell("termOpt", "<", "epsilon");
		setCell("termOpt", "+", "epsilon");
		setCell("termOpt", "-", "epsilon");
		setCell("termOpt", ";", "epsilon");
		setCell("termOpt", "*", "operatorMultiply fact termOpt");
		setCell("termOpt", "/", "operatorMultiply fact termOpt");
		setCell("termOpt", "^", "operatorMultiply fact termOpt");
		setCell("fact", "(", "parenthesesExpression");
		setCell("fact", "integer", "integer");
		setCell("fact", "float", "float");
		setCell("fact", "string", "string");
		setCell("fact", "id", "id");
		setCell("operatorRelation", "==", "==");
		setCell("operatorRelation", "!=", "!=");
		setCell("operatorRelation", ">=", ">=");
		setCell("operatorRelation", "<=", "<=");
		setCell("operatorRelation", ">", ">");
		setCell("operatorRelation", "<", "<");
		setCell("operatorAdd", "+", "+");
		setCell("operatorAdd", "-", "-");
		setCell("operatorMultiply", "*", "*");
		setCell("operatorMultiply", "/", "/");
		setCell("operatorMultiply", "^", "^");
		setCell("operatorMultiply", "%", "%");
		setCell("termOpt", "%", "operatorMultiply fact termOpt");
		setCell("statements", "if", "statement ; statements");
		setCell("statement", "if", "statementIf");
		setCell("statementIf", "if", "if parenthesesExpression braceBlock statementElse");
		setCell("statementElse", "else", "else braceBlock");
		setCell("statementElse", ";", "epsilon");
	}
	/**
	 * sets up the stack, instantiates the arrays of terminals and nonterminals, along with setting up the LL parse table
	 * @param tokens the list of tokens to be sent to the parser
	 */
	public Parser(ArrayList<Token> tokens) {
		stack = new Stack<>();
		rhsRowNumbers = new HashMap<>();
		terminalColumnNumbers = new HashMap<>();
		terminalColumns = new String[] { "{", "}", "(", ")", "integer", "float", "string", "id", ",", "==", "!=", "<", ">", "<=", ">=", "+", "-", "*", "/", "^", ";", "prog", "vars", "print", "while", "$", "if", "else", "%" };
		rhsRules = new String[] { "kprog", "braceBlock", "variableGroup", "parenthesesVariableList", "variableList", "variableDeclaration", "basekind", "variableID", "statements", "statement", "statementAssign",
				"statementPrint", "statementWhile", "parenthesesExpressions", "parenthesesExpression", "expressionList", "moreExpressions", "expression", "expressionOpt", "rightTerm", "rightTermOpt", "term", "termOpt",
				"fact", "operatorRelation", "operatorAdd", "operatorMultiply", "statementIf", "statementElse" };
		this.tokens = new ArrayList<>(tokens);
		fillRhsRowNumbers();
		fillTerminalColumnNumbers();
		setTable();
	}

	/**
	 * fill the rhs row numbers map
	 */
	private void fillRhsRowNumbers() {
		for (int i = 0; i < rhsRules.length; i++) {
			rhsRowNumbers.put(rhsRules[i], i);
		}
	}

	/**
	 * fill the terminal column map
	 */
	private void fillTerminalColumnNumbers() {
		for (int i = 0; i < terminalColumns.length; i++) {
			terminalColumnNumbers.put(terminalColumns[i], i);
		}
	}

	/**
	 * setup the parse machine push a "dummy" node $ onto the stack create the
	 * root node and have it be equal to the start symbol of the grammar, which
	 * is kprog then push this root onto the stack
	 */
	public void setup() {
		stack.push(new PSTNode("$", counter++));
		root = new PSTNode("kprog", counter++);
		stack.push(root);
	}

	/**
	 * determines if a node is a terminal node or not
	 * @param s the potentially terminal node
	 * @return true if the node is terminal, false otherwise
	 */
	private boolean isTerminal(PSTNode s) {
		for (int i = 0; i < terminalColumns.length; i++) {
			if (s.getType().equals(terminalColumns[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Simple error function that displays a message and exits the program
	 * @param msg the error message
	 */
	private void error(String msg) {
		System.out.println(msg);
		System.exit(0);
	}

	/**
	 * gets the cell in the parse table corresponding to the front of the input and the top of the stack
	 * @param row the lhs of the rule
	 * @param col the token on the front of the input
	 * @return the corresponding cell which is either a rhs rule, epsilon, or an empty string
	 */
	private String getCell(String row, String col) {
		int rowNum = rhsRowNumbers.get(row);
		int colNum = terminalColumnNumbers.get(col);
		String cell = LLParseTable[rowNum][colNum];
		return cell;
	}
	public void setCell(String row, String col, String value){
		int rowNum = rhsRowNumbers.get(row);
		int colNum = terminalColumnNumbers.get(col);
		LLParseTable[rowNum][colNum] = value;
	}

	/**
	 * copy everything from the kid node into the mom node
	 * @param mom the mom node
	 * @param kid the kid node
	 */
	private void copyGuts(PSTNode mom, PSTNode kid) {
		mom.setValue(kid.getValue());
		mom.setType(kid.getType());
		mom.setVariableValue(kid.getVariableValue());
		mom.setChildren(kid.getChildren()); 
	}

	/**
	 * gets the root of the AST
	 * @return the root of the AST
	 */
	public PSTNode getRoot() {
		return root;
	}

	/**
	 * this is a fix aimed at the trivial rules i.e. when the parent has only a
	 * single child, thus the only thing to do is to hoist the kid to the parent
	 * @param node the node to be fixed
	 */
	private void P2ATrivialFix(PSTNode node) {
		copyGuts(node, node.getChildAt(0)); // trivial fix if the rule's rhs only has 1 thing in it
	}

	/**
	 * the fix for the prog node
	 * @param node the prog node
	 */
	private void P2AFixProg(PSTNode node) {
		node.getChildAt(0).addChild(node.getChildAt(1)); // set bblock as kid of prog
		copyGuts(node, node.getChildAt(0)); // hoist prog
	}

	private void P2AFixStatementIf(PSTNode node){
		node.getChildAt(0).addChild(node.getChildAt(1)); // add parexpr as kid to if
		node.getChildAt(0).addChild(node.getChildAt(2)); // add bblock as kid to if
		if (node.getChildAt(3).getNumberOfChildren() != 0){
			node.getChildAt(0).addChild(node.getChildAt(3)); // if else doesn't go to epsilon, add as kid to if
		}
		copyGuts(node, node.getChildAt(0)); // hoist if
	}
	/**
	 * fix for the brace block node
	 * @param node the brace block node
	 */
	private void P2AFixBBlock(PSTNode node) {
		if (node.getChildAt(1).getNumberOfChildren() != 0) { // check if vargroup leads to epsilon
			node.getChildAt(0).addChild(node.getChildAt(1)); // if vargroup does not go to epsilon, add it as child to {
		}
		if (node.getChildAt(2).getNumberOfChildren() != 0) {
			node.getChildAt(0).addChild(node.getChildAt(2)); // add statements as child to {
		}
		node.getChildAt(0).addChild(node.getChildAt(3)); // add } as child to {
		copyGuts(node, node.getChildAt(0)); // hoist {
	}

	/**
	 * fix for the else statement node
	 * @param node the else statement node
	 */
	private void P2AFixStatementElse(PSTNode node){
		if (node.getNumberOfChildren() != 0){ // if it does not go to epsilon
			node.getChildAt(0).addChild(node.getChildAt(1)); // add bblock as child to kelse
			copyGuts(node, node.getChildAt(0)); // hoist kelse
		}
	}
	/**
	 * fix for the variable group node
	 * @param node the variable group node
	 */
	private void P2AFixVariableGroup(PSTNode node) {
		if (node.getNumberOfChildren() != 0) { // if it does not go to epsilon
			node.getChildAt(0).addChild(node.getChildAt(1)); // add parvarlist as child to kvars
			copyGuts(node, node.getChildAt(0)); // hoist kvars
		}
	}

	/**
	 * fix for the parentheses variable list node
	 * @param node the parentheses variable list node
	 */
	private void P2AFixParenthesesVariableList(PSTNode node) {
		if (node.getChildAt(1).getNumberOfChildren() != 0) {
			node.getChildAt(0).addChild(node.getChildAt(1)); // add varlist as child to open paren
		}
		node.getChildAt(0).addChild(node.getChildAt(2)); // add ) as child to (
		copyGuts(node, node.getChildAt(0)); // hoist (
	}

	/**
	 * fix for the variable list node
	 * @param node the variable list node
	 */
	private void P2AFixVariableList(PSTNode node) {
		if (node.getNumberOfChildren() != 0) { // check if it goes to epsilon
			if (node.getChildAt(2).getNumberOfChildren() != 0) {
				node.getChildAt(0).addChild(node.getChildAt(2)); // add varlist as child to vardecl
			}
			copyGuts(node, node.getChildAt(0)); // hoist vardecl
		}
	}

	/**
	 * fix for the variable declaration node
	 * @param node the variable declaration node
	 */
	private void P2AFixVariableDeclaration(PSTNode node) {
		node.getChildAt(0).addChild(node.getChildAt(1)); // add varid as kid to bkind
		copyGuts(node, node.getChildAt(0)); // hoist bkind
	}

	/**
	 * fix for the statements node
	 * @param node the statements node
	 */
	private void P2AFixStatements(PSTNode node) {
		if (node.getNumberOfChildren() != 0) { // check if statements goes to epsilon
			node.getChildAt(1).addChild(node.getChildAt(0)); // if it does not go to epsilon, add the statement as a child to ;
			if (node.getChildAt(2).getNumberOfChildren() != 0) { // check if child statements goes to epsilon
				node.getChildAt(1).addChild(node.getChildAt(2)); // if it does not go to epsilon, add it as child to ;
			}
			copyGuts(node, node.getChildAt(1)); // hoist ;
		}
	}

	/**
	 * fix for the statement assign node
	 * @param node the statement assign node
	 */
	private void P2AFixStatementAssign(PSTNode node) {
		node.getChildAt(1).addChild(node.getChildAt(0)); // add varid as kid to =
		node.getChildAt(1).addChild(node.getChildAt(2)); // add expr as kid to =
		copyGuts(node, node.getChildAt(1)); // hoist =
	}

	/**
	 * fix for the statement print node
	 * @param node the statement print node
	 */
	private void P2AFixStatementPrint(PSTNode node) {
		node.getChildAt(0).addChild(node.getChildAt(1)); // add parexprs as kid to print
		copyGuts(node, node.getChildAt(0)); // hoist print
	}

	/**
	 * fix for the statement while node
	 * @param node the statement while node
	 */
	private void P2AFixStatementWhile(PSTNode node) {
		node.getChildAt(0).addChild(node.getChildAt(1)); // add parexpr as kid to while
		node.getChildAt(0).addChild(node.getChildAt(2)); // add bblock as kid to while
		copyGuts(node, node.getChildAt(0)); // hoist while
	}

	/**
	 * fix for the parentheses expressions node
	 * @param node the parentheses expressions node
	 */
	private void P2AFixParenthesesExpressions(PSTNode node) {
		node.getChildAt(0).addChild(node.getChildAt(1)); // add exprlist as kid to (
		node.getChildAt(0).addChild(node.getChildAt(2)); // add ) as kid to (
		copyGuts(node, node.getChildAt(0)); // hoist (
	}

	/**
	 * fix for the parentheses expression node
	 * @param node the parentheses expression node
	 */
	private void P2AFixParenthesesExpression(PSTNode node) {
		node.getChildAt(0).addChild(node.getChildAt(1)); // add expr as kid to (
		node.getChildAt(0).addChild(node.getChildAt(2)); // add ) as kid to (
		copyGuts(node, node.getChildAt(0)); // hoist (
	}

	/**
	 * fix for the expression list node
	 * @param node the expression list node
	 */
	private void P2AFixExpressionList(PSTNode node) {
		if (node.getChildAt(1).getNumberOfChildren() != 0) { // check if moreexprs goes to epsilon
			node.getChildAt(0).addChild(node.getChildAt(1)); // add moreexprs as kid to expr if not epsilon
		}
		copyGuts(node, node.getChildAt(0)); // hoist expr
	}

	/**
	 * fix for the more expressions node
	 * @param node  the more expressions node
	 */
	private void P2AFixMoreExpressions(PSTNode node) {
		if (node.getNumberOfChildren() != 0) { // check if moreexprs leads to epsilon
			node.getChildAt(0).addChild(node.getChildAt(1)); // add moreexprs as kid to ,
			copyGuts(node, node.getChildAt(0)); // hoist ,
		}
	}

	/**
	 * fix for the expression node
	 * @param node the expression node
	 */
	private void P2AFixExpression(PSTNode node) {
		if (node.getChildAt(1).getNumberOfChildren() != 0) { // check if expropt goes to epsilon
			node.getChildAt(1).addChild(node.getChildAt(0)); // if it does not go to epsilon, add rightTerm as kid to expropt
			copyGuts(node, node.getChildAt(1)); // hoist expropt
		} else {
			copyGuts(node, node.getChildAt(0)); // hoist rightTerm
		}
	}

	/**
	 * fix for the expression opt node
	 * @param node the expression opt node
	 */
	private void P2AFixExpressionOpt(PSTNode node) {
		if (node.getNumberOfChildren() != 0) { // check if it leads to epsilon
			node.getChildAt(0).addChild(node.getChildAt(1)); // add rterm as kid to oprel
			copyGuts(node, node.getChildAt(0)); // hoist oprel
		}
	}

	/**
	 * fix for the right term node
	 * @param node the right term node
	 */
	private void P2AFixRightTerm(PSTNode node) {
		if (node.getChildAt(1).getNumberOfChildren() != 0) { // check if rtermopt goes to epsilon
			node.getChildAt(1).addChild(node.getChildAt(0)); // add term as child to rtermopt
			copyGuts(node, node.getChildAt(1)); // hoist rtermopt
		} else {
			copyGuts(node, node.getChildAt(0)); // hoist term
		}
	}

	/**
	 * fix for the right term opt node
	 * @param node the right term opt node
	 */
	private void P2AFixRightTermOpt(PSTNode node) {
		if (node.getNumberOfChildren() != 0) { // check if it goes to epsilon
			if (node.getChildAt(2).getNumberOfChildren() != 0) { // check if its rightTermOpt child goes to epsilon
				node.getChildAt(2).addChild(node.getChildAt(1)); // add term as kid to rightTermOpt child
				node.getChildAt(0).addChild(node.getChildAt(2)); // add rightTermOpt child as kid to operatorAdd
			} else {
				node.getChildAt(0).addChild(node.getChildAt(1)); // add term as kid to opadd
			}
			copyGuts(node, node.getChildAt(0)); // hoist opadd
		}
	}

	/**
	 * fix for the term node
	 * @param node the term node
	 */
	private void P2AFixTerm(PSTNode node) {
		if (node.getChildAt(1).getNumberOfChildren() != 0) { // check if rightTermOpt goes to epsilon
			node.getChildAt(1).addChild(node.getChildAt(0)); // add term as child to rtermopt
			copyGuts(node, node.getChildAt(1)); // hoist rterm opt
		} else {
			copyGuts(node, node.getChildAt(0)); // hoist term
		}
	}

	/**
	 * fix for the term opt node
	 * @param node the term opt node
	 */
	private void P2AFixTermOpt(PSTNode node) {
		P2AFixRightTermOpt(node); // it's the exact same fix as rightTermOpt
	}

	/**
	 * fixes the node based on what node it is
	 * @param node the node to be fixed
	 */
	private void P2AFix(PSTNode node) {
		switch (node.getType()) {
		case "operatorRelation":
			P2ATrivialFix(node);
			break;
		case "operatorAdd":
			P2ATrivialFix(node);
			break;
		case "operatorMultiply":
			P2ATrivialFix(node);
			break;
		case "fact":
			P2ATrivialFix(node);
			break;
		case "basekind":
			P2ATrivialFix(node);
			break;
		case "statement":
			P2ATrivialFix(node);
			break;
		case "variableID":
			P2ATrivialFix(node);
			break;
		case "kprog":
			P2AFixProg(node);
			break;
		case "braceBlock":
			P2AFixBBlock(node);
			break;
		case "variableGroup":
			P2AFixVariableGroup(node);
			break;
		case "parenthesesVariableList":
			P2AFixParenthesesVariableList(node);
			break;
		case "variableList":
			P2AFixVariableList(node);
			break;
		case "variableDeclaration":
			P2AFixVariableDeclaration(node);
			break;
		case "statements":
			P2AFixStatements(node);
			break;
		case "statementAssign":
			P2AFixStatementAssign(node);
			break;
		case "statementPrint":
			P2AFixStatementPrint(node);
			break;
		case "statementWhile":
			P2AFixStatementWhile(node);
			break;
		case "statementIf":
			P2AFixStatementIf(node);
			break;
		case "statementElse":
			P2AFixStatementElse(node);
			break;
		case "parenthesesExpressions":
			P2AFixParenthesesExpressions(node);
			break;
		case "parenthesesExpression":
			P2AFixParenthesesExpression(node);
			break;
		case "expressionList":
			P2AFixExpressionList(node);
			break;
		case "moreExpressions":
			P2AFixMoreExpressions(node);
			break;
		case "expression":
			P2AFixExpression(node);
			break;
		case "expressionOpt":
			P2AFixExpressionOpt(node);
			break;
		case "rightTerm":
			P2AFixRightTerm(node);
			break;
		case "rightTermOpt":
			P2AFixRightTermOpt(node);
			break;
		case "term":
			P2AFixTerm(node);
			break;
		case "termOpt":
			P2AFixTermOpt(node);
			break;
		default:
			break;
		}

	}

	/**
	 * post-order walk of the tree, converting the pst to an ast
	 * @param node the root of the pst
	 */
	private void PST2AST(PSTNode node) {
		for (PSTNode n : node.getChildren()) {
			PST2AST(n);
		}
		P2AFix(node);
	}

	/**
	 * pre-order walk of the tree to display the contents of the tree
	 * @param node the node of the tree
	 */
	public void treeSerializer(PSTNode node) {
		String info = "( Node: " + node.getId() + "    type: " + node.getType();
		info += node.getValue().equals(node.getType()) ? "" : "    value: " + node.getValue();
		info += node.getNumberOfChildren() == 0 ? "" : "    kids IDs: ";
		for (PSTNode n : node.getChildren()) {
			info += " " + n.getId();
		}
		info += " )";
		System.out.println(info);
		for (PSTNode n : node.getChildren()) {
			treeSerializer(n);
		}
	}

	/**
	 * makes nodes of the right hand side of the rule
	 * @param rightHandSide the right hand side of the rule
	 * @return a list containing the nodes of the right hand side
	 */
	private ArrayList<PSTNode> makeNodes(String[] rightHandSide) {
		ArrayList<PSTNode> nodes = new ArrayList<PSTNode>();
		for (int i = 0; i < rightHandSide.length; i++) {
			nodes.add(new PSTNode(rightHandSide[i], counter++));
		}
		return nodes;
	}

	/**
	 * links the mom to the kids
	 * @param mom the mom node
	 * @param kids the kids nodes
	 */
	private void link(PSTNode mom, ArrayList<PSTNode> kids) {
		for (int i = 0; i < kids.size(); i++) {
			if (!kids.get(i).getType().equals("epsilon")) {
				mom.addChild(kids.get(i));
			}
		}
	}

	/**
	 * attaches the value of the front node to the top node
	 * @param front the front node of the list
	 * @param top the top of the stack
	 */
	private void attach(Token front, PSTNode top) {
		top.setValue(front.getValue());
	}

	/**
	 * the LL parse machine follows the rules m1-m4
	 * it creates a parse tree, then converts it to an abstract syntax tree
	 */
	public void parse() {
		setup();
		while (true) {
			if (stack.isEmpty()) {
				if (tokens.size() == 0) {
					break;
				} else {
					error("Bad input");
				}
			}
			PSTNode top = stack.peek();
			front = tokens.get(0);
			if (top.getType().equals(front.getType())) {
				attach(front, top);
				stack.pop();
				tokens.remove(0);
			} else if (isTerminal(top)) {
				error("You input bad code");
			} else {
				front = tokens.get(0);
				String cell = getCell(top.getType(), front.getType());
				if (cell.equals("")) {
					error("You input bad code");
				} else {
					PSTNode mom = top;
					stack.pop();
					ArrayList<PSTNode> nodes = makeNodes(cell.split(" "));
					if (cell.equals("epsilon")) {
						cell = "";
					} else {
						for (int i = nodes.size() - 1; i >= 0; i--) {
							stack.push(nodes.get(i));
						}
					}
					link(mom, nodes);
				}
			}
		}
		PST2AST(root);
	}
}