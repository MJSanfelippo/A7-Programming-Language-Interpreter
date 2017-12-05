
/**
 * 
 * @author Michael Sanfelippo, Jeremy Ethridge, Maria Nuila
 * 
 * The boolean helper class simply groups together any methods that take an AST node and returns whether that node is or is not something
 *
 */
public class NodeIdentifier {

	/**
	 * Determines if the node is a declaration node
	 * @param node the node in question
	 * @return true if the node is a declaration node, false otherwise
	 */
	public static boolean isDeclaration(PSTNode node) {
		if (node == null) {
			return false;
		}
		boolean isString = node.getValue().equalsIgnoreCase("string");
		boolean isInteger = node.getValue().equalsIgnoreCase("integer");
		boolean isFloat = node.getValue().equalsIgnoreCase("float");

		boolean isDeclaration = isString || isInteger || isFloat;
		return isDeclaration;
	}

	/**
	 * Determines if the node is a start block
	 * @param node the node in question
	 * @return true if the node is a start block, false otherwise
	 */
	public static boolean isStartBlock(PSTNode node) {
		return node.getType().equals("{");
	}

	/**
	 * Determines if the node is an end block
	 * @param node the node in question
	 * @return true if the node is an end block, false otherwise
	 */
	public static boolean isEndBlock(PSTNode node) {
		return node.getType().equals("}");
	}

	/**
	 * Determines if the node is a use
	 * @param node the node in question
	 * @return true if the node is a use, false otherwise
	 */
	public static boolean isUse(PSTNode node) {
		return node.getType().equals("id");
	}

	/**
	 * Determines if the node is a relational operator 
	 * @param node the node in question
	 * @return true if the node is a relational operator, false otherwise
	 */
	public static boolean isOperationRelation(PSTNode node) {
		boolean isOpGreaterThan = node.getValue().equals(">");
		boolean isOpLessThan = node.getValue().equals("<");
		boolean isOpEquals = node.getValue().equals("==");
		boolean isOpNotEquals = node.getValue().equals("!=");
		boolean isOpGreaterEquals = node.getValue().equals(">=");
		boolean isOpLessEquals = node.getValue().equals("<=");

		boolean isOperationRelation = isOpGreaterThan | isOpLessThan | isOpEquals | isOpNotEquals | isOpGreaterEquals
				| isOpLessEquals;
		return isOperationRelation;
	}

	/**
	 * Determines if the node is an arithmetic operator
	 * @param node the node in question
	 * @return true if the node is an arithmetic operator, false otherwise
	 */
	public static boolean isOperator(PSTNode node) {
		boolean isAssignment = node.getType().equals("=");
		boolean isOpAdd = node.getType().equals("+");
		boolean isOpMinus = node.getType().equals("-");
		boolean isOpMultiply = node.getType().equals("*");
		boolean isOpDivide = node.getType().equals("/");
		boolean isOpPower = node.getType().equals("^");
		boolean isMod = node.getType().equals("%");
		boolean isOperation = isOpAdd | isOpMinus | isOpMultiply | isOpDivide | isOpPower | isAssignment | isMod;
		return isOperation;
	}

	/**
	 * Determines if the node is a print statement
	 * @param node the node in question
	 * @return true if the node is a print statement, false otherwise
	 */
	public static boolean isPrint(PSTNode node) {
		boolean isPrint = node.getValue().equals("print");
		return isPrint;
	}

	/**
	 * Determines if the node is a while statement
	 * @param node the node in question
	 * @return true if the node is a while statement, false otherwise
	 */
	public static boolean isWhile(PSTNode node) {
		boolean isWhile = node.getValue().equals("while");
		return isWhile;
	}


	/**
	 * Determines if the node is an integer
	 * @param node the node in question
	 * @return true if the node is an integer, false otherwise
	 */
	public static boolean isInteger(PSTNode node) {
		try {
			Integer.parseInt(node.getVariableValue());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Determines if the node is an if statement
	 * @param node the node in question
	 * @return true if it's an if statement, false otherwise
	 */
	public static boolean isIf(PSTNode node){
		boolean isIf = node.getValue().equals("if");
		return isIf;
	}
	
	/**
	 * Determines the truth value of the value in question
	 * Everything is true unless it is either false or 0
	 * @param value the value in question
	 * @return the truth value
	 */
	public static boolean evalTruthValue(String value) {
		return !(value.equals("false") || value.equals("0"));
	}

	/**
	 * Determines if the node is a statement 
	 * @param node the node in question
	 * @return true if the node is a statement, false otherwise
	 */
	public static boolean isStatement(PSTNode node) {
		return node.getType().equals(";");
	}

	/**
	 * Determines if the node is a prog node
	 * @param node the node in question
	 * @return true if the node is a prog node, false otherwise
	 */
	public static boolean isProg(PSTNode node) {
		return node.getType().equals("prog");
	}
}