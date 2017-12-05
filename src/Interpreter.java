
/**
 * 
 * @author Michael Sanfelippo, Jeremy Ethridge, Maria Nuila
 * 
 * The interpreter runs the AST that gets built by the Parser
 * It generates a scope tree based on the AST and runs the AST utilizing this previously built scope tree
 *
 */
public class Interpreter {

	/**
	 * the root of the AST
	 */
	private PSTNode ASTroot;
	/**
	 * the root of the SCT
	 */
	private SCTNode SCTroot;
	/**
	 * the current scope 
	 */
	private SCTNode currentScope;

	/**
	 * constructor for the Interpreter
	 * it instantiates a new boolean helper object
	 * it also creates a lexer, runs it, creates the parser from the lexer's output, runs the parser, and gets the root of the AST
	 * instantiates the scope tree root and sets the current scope to be pointing to it
	 */
	public Interpreter() {
		Lexer lexer = new Lexer();
		SCTroot = new SCTNode();
		currentScope = SCTroot;
		lexer.run();
		Parser parser = new Parser(lexer.getTokenOutputList());
		parser.parse();
		this.ASTroot = parser.getRoot();
	}


	/**
	 * Simple error function that displays a message and exits the program
	 * @param msg the message to be displayed
	 */
	private void error(String msg) {
		System.out.println(msg);
		System.exit(0);
	}

	/**
	 * creates a new symbol table entry based on the declaration node
	 * @param node the declaration node
	 */
	public void A2SDeclaration(PSTNode node) {
		SymbolTableEntry symTabEntry = new SymbolTableEntry(node.getChildAt(0).getValue(), node);
		currentScope.addEntry(symTabEntry);
		if (node.getNumberOfChildren() > 1) { // if there are more declarations
			if (NodeIdentifier.isDeclaration(node.getChildAt(1))) { // if its child is a declaration too
				A2SDeclaration(node.getChildAt(1)); // call declaration again on this new declaration
			}
		}
	}

	/**
	 * gets the entry of the variable in the symbol table, then sets it to the variable node
	 * @param node the use node
	 */
	public void A2SUse(PSTNode node) {
		SymbolTableEntry entry = currentScope.findEntry(node);
		if (node != null) {
			if (entry != null) {
				node.setEntry(entry);
			}
		}
	}

	/**
	 * generates a scope tree based on the AST
	 * @param node the node to be ran
	 */
	public void AST2SCT(PSTNode node) {
		if (node == null) {
			return;
		}
		if (NodeIdentifier.isStartBlock(node)) {
			A2SStartBlock(node);
		} else if (NodeIdentifier.isEndBlock(node)) {
			endScope();
		} else if (NodeIdentifier.isDeclaration(node)) {
			A2SDeclaration(node);
		} else if (NodeIdentifier.isUse(node)) {
			A2SUse(node);
		} else {
			for (PSTNode child : node.getChildren()) {
				AST2SCT(child);
			}
		}
	}

	/**
	 * creates a new scope node and bilinks it with the current scope
	 * points the current scope to this new scope
	 * @param n the node containing {
	 */
	public void A2SStartBlock(PSTNode n) {
		@SuppressWarnings("unused")
		SCTNode node = new SCTNode(currentScope);
		startNewScope();
		for (PSTNode child : n.getChildren()) {
			AST2SCT(child);
		}
	}

	/**
	 * displays the scope tree
	 * @param root the root of the scope tree
	 */
	public void displaySCT(SCTNode root) {
		System.out.println(root);
		for (SCTNode n : root.getChildren()) {
			displaySCT(n);
		}
	}

	/**
	 * creates the scope tree and runs the AST
	 */
	public void run() {
		AST2SCT(ASTroot);
		run_ast(ASTroot);
	}

	/**
	 * performs a relational operation on numbers, strings, or IDs containing numbers or strings
	 * @param node the node containing which operation to perform (==, !=, >=, <=, >, <)
	 * @param type the type of relation
	 */
	public void doOperationRelation(PSTNode node, String type) {
		String typeOfChild2 = node.getChildAt(0).getType();
		String typeOfChild1 = node.getChildAt(1).getType();
		if (NodeIdentifier.isOperator(node.getChildAt(0))){
			doOperation(node.getChildAt(0));
			typeOfChild2 = node.getChildAt(0).getChildAt(0).getType();
			
		}
		if (NodeIdentifier.isOperator(node.getChildAt(1))){
			doOperation(node.getChildAt(1));
			typeOfChild1 = node.getChildAt(0).getChildAt(0).getType();
		}
		String child2Value = node.getChildAt(0).getVariableValue();
		String child1Value = node.getChildAt(1).getVariableValue();
		if (node.getChildAt(0).getType().equals("id")) { // if the first argument is an id
			SCTNode realScope2 = currentScope.searchUpperScopes(node.getChildAt(0)); // find the IDs scope
			typeOfChild2 = realScope2.getType(node.getChildAt(0)); // get the IDs type
			child2Value = realScope2.getValue(node.getChildAt(0)); // get the IDs value
		}
		if (node.getChildAt(1).getType().equals("id")) { // same as above
			SCTNode realScope1 = currentScope.searchUpperScopes(node.getChildAt(1));
			typeOfChild1 = realScope1.getType(node.getChildAt(1));
			child1Value = realScope1.getValue(node.getChildAt(1));
		}
		if (typeOfChild2.equals("string") && typeOfChild1.equals("string")) { // if they are both strings, can only perform equals or not equals
			switch (type) {
			case "==":
				node.setVariableValue(Boolean.toString(child1Value.equals(child2Value)));
				break;
			case "!=":
				node.setVariableValue(Boolean.toString(!(child1Value.equals(child2Value))));
				break;
			default:
				error("Incompatible types for operator: " + node.getType());
				break;
			}
		}
		if (typeOfChild2.equals("integer") || typeOfChild2.equals("float")) {
			if (typeOfChild1.equals("integer") || typeOfChild1.equals("float")) { // if they are both numbers
				double child2 = Double.parseDouble(child2Value);
				double child1 = Double.parseDouble(child1Value);

				switch (type) {
				case "==":
					node.setVariableValue(Boolean.toString(child1 == child2));
					break;
				case "!=":
					node.setVariableValue(Boolean.toString(child1 != child2));
					break;
				case ">=":
					node.setVariableValue(Boolean.toString(child1 >= child2));
					break;
				case "<=":
					node.setVariableValue(Boolean.toString(child1 <= child2));
					break;
				case "<":
					node.setVariableValue(Boolean.toString(child1 < child2));
					break;
				case ">":
					node.setVariableValue(Boolean.toString(child1 > child2));
					break;
				default:
					error("Incompatible comparison");
					break;
				}
			}
		}
	}

	/**
	 * determine what to do with the node based on its operation
	 * @param node the relational operator node
	 */
	public void doOperationRelation(PSTNode node) {
		String type = node.getType();
		switch (type) {
		case "==":
			if (node.getChildAt(0).getType().equals("id") || node.getChildAt(1).getType().equals("id")) { // if either argument is an ID, perform the operation
				doOperationRelation(node, type);
			} else { // otherwise simply compare them
				node.setVariableValue(Boolean.toString(node.getChildAt(0).getVariableValue().equals(node.getChildAt(1).getVariableValue())));
			}
			break;
		case "!=":
			if (node.getChildAt(0).getType().equals("id") || node.getChildAt(1).getType().equals("id")) { // same as above
				doOperationRelation(node, type);
			} else {
				node.setVariableValue(Boolean.toString(!(node.getChildAt(0).getVariableValue().equals(node.getChildAt(1).getVariableValue()))));
			}
			break;
		default:
			doOperationRelation(node, type); // if it's not == or !=, it has to be one of these 4: >=, <=, >, <
			break;
		}
	}

	/**
	 * perform an arithmetic operation on strings
	 * the only supported operation for strings is adding, which will concatenate the strings
	 * @param node the operator node
	 * @param val1 the value of the first argument
	 * @param val2 the value of the second argument
	 */
	public void doArithmeticStrings(PSTNode node, String val1, String val2) {
		String opType = node.getType();
		String result = "";
		switch (opType) {
		case "+":
			result = val1 + val2;
			break;
		default:
			error("Operator: " + opType + " not supported for strings");
			break;
		}
		node.setValue(result);
	}

	/**
	 * Performs an if operation by checking the condition and running the block if it is true
	 * @param node the if satement node
	 */
	public void doIf(PSTNode node){
		PSTNode evalNode = node.getChildAt(0).getChildAt(0); // the node containing the evaluation
		PSTNode blockNode = node.getChildAt(1); // the node containing the block
		String value = "";
		if (evalNode.getNumberOfChildren() > 0) { 
			run_ast(evalNode);
			value = evalNode.getVariableValue();
		} else if (evalNode.getType().equals("id")) {
			value = currentScope.getValue(evalNode);
		} else {
			value = evalNode.getVariableValue();
		}
		boolean condition = NodeIdentifier.evalTruthValue(value);
		if (condition) { 
			run_ast(blockNode);
		} else { // if the condition is false, and there is an else statement, run it
			if (node.getChildAt(2) != null){
				run_ast(node.getChildAt(2).getChildAt(0));
			}
		}
	}
	/**
	 * perform an arithmetic operation on numbers
	 * @param node the operator node
	 * @param val1 the value of the first argument
	 * @param val2 the value of the second argument
	 */
	public void doArithmeticNumbers(PSTNode node, double val1, double val2) {
		String opType = node.getType();
		double result = 0;
		switch (opType) {
		case "+":
			result = val1 + val2;
			break;
		case "-":
			result = val1 - val2;
			break;
		case "/":
			result = val1 / val2;
			break;
		case "*":
			result = val1 * val2;
			break;
		case "^":
			result = Math.pow(val1, val2);
			break;
		case "%":
			result = val1 % val2;
			break;
		default:
			error("Probably impossible to get here");
			break;
		}
		node.setValue(Double.toString(result));
	}

	/**
	 * NOTE: As of 11/20/17, arithmetic operations work from right to left. THEY DO NOT UTILIZE PEMDAS (OR BEDMAS FOR MY BRITISH FRIENDS).
	 * 
	 * determines what kind of arithmetic operation to do on the arguments based on the operator type and the types of the two arguments
	 * @param node the operator node
	 */
	public void doArithmetic(PSTNode node) {
		String type1 = node.getChildAt(1).getType();
		String type2 = node.getChildAt(0).getType();
		String val1 = node.getChildAt(1).getVariableValue();
		String val2 = node.getChildAt(0).getVariableValue();

		SCTNode realScope1 = currentScope.searchUpperScopes(node.getChildAt(1)); // get the real scope of the first argument
		SCTNode realScope2 = currentScope.searchUpperScopes(node.getChildAt(0)); // get the real scope of the second argument
		if (type1.equals("id")) {
			type1 = realScope1.getType(node.getChildAt(1)); // give the type and values their proper values if it is an id
			val1 = realScope1.getValue(node.getChildAt(1));
		}
		if (type2.equals("id")) { 
			type2 = realScope2.getType(node.getChildAt(0)); // same as above
			val2 = realScope2.getValue(node.getChildAt(0));
		}
		// values are string by default
		// if it is an integer, turn the type to an integer
		// if it is not a string and not an integer, it must be a float
		try {
			Integer.parseInt(val1);
			type1 = "integer";
		} catch (Exception e) {
			if (!type1.equals("string")) {
				type1 = "float";
			}
		}
		try {
			Integer.parseInt(val2);
			type2 = "integer";
		} catch (Exception e) {
			if (!type2.equals("string")) {
				type2 = "float";
			}
		}
		if ((type1.equals("integer") || type1.equals("float")) && (type2.equals("integer") || type2.equals("float"))) {
			double doubleVal1 = Double.parseDouble(val1);
			double doubleVal2 = Double.parseDouble(val2);
			doArithmeticNumbers(node, doubleVal1, doubleVal2);
		} else if (type1.equals("string") && type2.equals("string")) {
			doArithmeticStrings(node, val1, val2);
		} else if (type1.equals("string") && (type2.equals("integer") || type2.equals("float"))) {
			doArithmeticStrings(node, val1, val2);
		} else if (type2.equals("string") && (type1.equals("integer") || type1.equals("float"))) {
			doArithmeticStrings(node, val1, val2);
		} else {
			error("Unsupported types with given operator");
		}
	}

	/**
	 * sets the value of a variable in a specific scope
	 * @param node the node containing the id
	 * @param scope the scope the id is in
	 * @param value the value to be set to
	 */
	public void setValueOfVariable(PSTNode node, SCTNode scope, String value) {
		scope.findEntry(node).setValue(value);
	}

	/**
	 * performs an operation on the node depending on what type it is
	 * if it is an assignment, perform the assignment operation
	 * otherwise it must be arithmetic, perform the arithmetic operation
	 * @param node the operator node
	 */
	public void doOperation(PSTNode node) {

		String opType = node.getType();
		switch (opType) {
		case "=":
			if (NodeIdentifier.isOperator(node.getChildAt(1))) {
				doOperation(node.getChildAt(1));
			}
			SCTNode realScope1 = currentScope.searchUpperScopes(node.getChildAt(0));
			SCTNode realScope2 = currentScope.searchUpperScopes(node.getChildAt(1));
			String type = realScope1.getType(node.getChildAt(0));
			if (type.equals("integer")) {
				if (!NodeIdentifier.isInteger(node.getChildAt(1)) && !node.getChildAt(1).getType().equals("id")) {
					int num = (int) Double.parseDouble(node.getChildAt(1).getVariableValue());
					node.getChildAt(0).setVariableValue(Integer.toString(num));
					setValueOfVariable(node.getChildAt(0), realScope1, Integer.toString(num));
				} else if (node.getChildAt(1).getType().equals("id")) {
					String val = realScope2.getValue(node.getChildAt(1));
					setValueOfVariable(node.getChildAt(0), realScope1, val);
				} else {
					String val = node.getChildAt(1).getVariableValue();
					node.getChildAt(0).setVariableValue(val);
					setValueOfVariable(node.getChildAt(0), realScope1, val);
				}
			} else if (type.equals("float")) {
				double num = Double.parseDouble(node.getChildAt(1).getVariableValue());
				node.getChildAt(0).setVariableValue(Double.toString(num));
				setValueOfVariable(node.getChildAt(0), realScope1, Double.toString(num));
			} else {
				node.getChildAt(0).setVariableValue(node.getChildAt(1).getVariableValue());
				String val = node.getChildAt(1).getVariableValue();
				setValueOfVariable(node.getChildAt(0), realScope1, val);
			}
			break;
		default:
			doArithmetic(node);
			break;

		}
	}

	/**
	 * perform the print operation
	 * @param node the print node
	 */
	public void doPrint(PSTNode node) {
		PSTNode n = null;
		if (node.getNumberOfChildren() > 1) { // if print has children, check if any of its children has children
			for (PSTNode nodes : node.getChildren()) {
				if (nodes.getType().equals(",")) { // if its children have a comma, set the pointer node to be the child
					n = nodes.getChildAt(0);
				}
			}
		} else {
			n = node.getChildAt(0).getChildAt(0); // otherwise set it to be its grandkid
		}

		if (NodeIdentifier.isOperator(n)) {
			doOperation(n);
		} else if (NodeIdentifier.isOperationRelation(n)) {
			doOperationRelation(n);
		}
		if (n.getType().equals("id")) { // if it's an id, print out the id's value
			SCTNode realScope = currentScope.searchUpperScopes(n);
			System.out.print(realScope.getValue(n));
		} else { // otherwise just print the value
			System.out.print(n.getVariableValue());
		}

		if (n.getNumberOfChildren() != 0) { // if it has children
			if (n.getChildAt(0).getType().equals(",")) { // and its child is a comma, indicating there is more stuff to print, do a print function again
				doPrint(n);
			}
			if (n.getNumberOfChildren() > 2) {
				if (n.getChildAt(2).getType().equals(",")) { // same as above
					doPrint(n);
				}
			}
		}
	}

	/**
	 * performs a while operation
	 * @param node the while node
	 */
	public void doWhile(PSTNode node) {
		PSTNode evalNode = node.getChildAt(0).getChildAt(0); // the node containing the evaluation
		PSTNode blockNode = node.getChildAt(1); // the node containing the block
		String value = "";
		if (evalNode.getNumberOfChildren() > 0) { 
			run_ast(evalNode);
			value = evalNode.getVariableValue();
		} else if (evalNode.getType().equals("id")) {
			value = currentScope.getValue(evalNode);
		} else {
			value = evalNode.getVariableValue();
		}
		boolean condition = NodeIdentifier.evalTruthValue(value);
		while (condition) { // this is taken from the lecture notes
			run_ast(blockNode);
			run_ast(evalNode);
			value = evaluateNode(evalNode);
			condition = NodeIdentifier.evalTruthValue(value);
		}
	}

	/**
	 * evaluate the node
	 * @param evalNode the node to be evaluated
	 * @return the value of the node
	 */
	public String evaluateNode(PSTNode evalNode) {
		String value;
		if (evalNode.getNumberOfChildren() > 0) {
			value = evalNode.getVariableValue();
		} else if (evalNode.getType().equals("id")) {
			value = currentScope.getValue(evalNode);
		} else {
			value = evalNode.getVariableValue();
		}
		return value;
	}

	/**
	 * NOTE: As of 11/20/17, it will only ever go to the first kid. I have no idea how to implement sibling scopes, thus it is not getting implemented
	 * changes the current scope pointer to be the kid of the current scope
	 */
	public void startNewScope() {
		currentScope = currentScope.getChildren().get(0);
	}

	/**
	 * changes the current scope pointer to be the parent of the current scope
	 */
	public void endScope() {
		currentScope = currentScope.getParent();
	}

	/**
	 * runs the AST using a switch statement to determine what to do depending on what type of node it is
	 * @param node the node to be ran
	 */
	public void run_ast(PSTNode node) {
		if (NodeIdentifier.isDeclaration(node)) { // this entire block was basically taken from the lecture notes
			return;
		} else if (NodeIdentifier.isEndBlock(node)) {
			endScope();
		} else if (NodeIdentifier.isProg(node)) {
			run_ast(node.getChildAt(0));
		} else if (NodeIdentifier.isStartBlock(node)) {
			startNewScope();
			for (PSTNode n : node.getChildren()) {
				run_ast(n);
			}
		} else if (NodeIdentifier.isStatement(node)) {
			for (PSTNode n : node.getChildren()) {
				run_ast(n);
			}
		} else if (NodeIdentifier.isUse(node)) {
			if (node.getEntry() != null) {
				if (null != node.getEntry().getValue()) {
					String value = currentScope.getValue(node);
					node.setVariableValue(value);
				}
			}
		} else if (NodeIdentifier.isOperator(node)) {
			doOperation(node);
		} else if (NodeIdentifier.isPrint(node)) {
			doPrint(node);
			System.out.println();
		} else if (NodeIdentifier.isWhile(node)) {
			PSTNode evalNode = node.getChildAt(0).getChildAt(0);
			run_ast(evalNode);
			String value = evaluateNode(evalNode);
			boolean condition = NodeIdentifier.evalTruthValue(value);
			if (condition) {
				doWhile(node);
			}
		} else if (NodeIdentifier.isOperationRelation(node)) {
			doOperationRelation(node);
		} else if (NodeIdentifier.isIf(node)){
			doIf(node);
		}
	}

	/**
	 * creates a new interpreter object and runs it
	 * @param args any command-line arguments, but I expect none
	 */
	public static void main(String[] args) {
		Interpreter interpreter = new Interpreter();
		interpreter.run();
	}
}