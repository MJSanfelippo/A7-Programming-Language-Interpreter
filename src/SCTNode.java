import java.util.ArrayList;

/**
 * 
 * @author Michael Sanfelippo, Jeremy Ethridge, Maria Nuila
 *
 * A scope tree node containg a symbol table, a pointer to its parent scope tree node, and a list of scope tree node children.
 */
public class SCTNode {

	/**
	 * the symbol table contained in the node
	 */
	private SymbolTable symbolTable;
	/**
	 * the parent of this node
	 */
	private SCTNode parent;
	/**
	 * a list of children of this node
	 */
	private ArrayList<SCTNode> children;

	/**
	 * constructor that instantiates a new symbol table, sets the parent to null, and instantiates the list of children
	 */
	public SCTNode() {
		symbolTable = new SymbolTable();
		parent = null;
		children = new ArrayList<SCTNode>();
	}

	/**
	 * constructor that instantiates a new symbol table, sets the parent to the parent that got passed in, adds this as a child to the parent, and instantiates the list of children
	 * @param parent the parent sct node
	 */
	public SCTNode(SCTNode parent){
		symbolTable = new SymbolTable();
		this.parent = parent;
		children = new ArrayList<SCTNode>();
		parent.addChild(this);
	}
	
	/**
	 * adds a new SCTnode child to the list of children
	 * @param child the child to add
	 */
	public void addChild(SCTNode child) {
		children.add(child);
	}

	/**
	 * gets the parent of this scope node
	 * @return the parent
	 */
	public SCTNode getParent() {
		return parent;
	}

	/**
	 * gets the children of this scope node
	 * @return the children
	 */
	public ArrayList<SCTNode> getChildren() {
		return children;
	}

	/**
	 * gets the type of the variable from the symbol table
	 * @param node the node containing the variable for which the type will be found
	 * @return the type of the variable
	 */
	public String getType(PSTNode node) {
		String type = findEntry(node).getDeclaration().getType();
		return type;
	}

	/**
	 * gets the value of the variable in this scope's symbol table
	 * @param node the variable node to get the value from
	 * @return the value in the symbol table
	 */
	public String getValue(PSTNode node) {
		String value = findEntry(node).getValue();
		return value;
	}

	/**
	 * searches upper scopes in case of variable shadowing
	 * @param node the node to be searched for
	 * @return the scope tree node containing the variable
	 */
	public SCTNode searchUpperScopes(PSTNode node) {
		boolean found = false;
		SCTNode possibleShadow = this;
		while (!found && !(possibleShadow.getParent() == (null))) {
			try {
				possibleShadow.findEntry(node).getValue();
				found = true;
			} catch (NullPointerException e) {
				possibleShadow = possibleShadow.getParent();
			}

		}
		return possibleShadow;
	}

	/**
	 * finds the entry of the variable in the symbol table
	 * @param n the variable node for which we will find its entry
	 * @return the entry containing the variable
	 */
	public SymbolTableEntry findEntry(PSTNode n) {
		return symbolTable.findEntryById(n.getValue());
	}

	/**
	 * adds an entry to the symbol table
	 * @param symTabEntry
	 */
	public void addEntry(SymbolTableEntry symTabEntry) {
		symbolTable.addSymbolTableEntry(symTabEntry);
	}

	/**
	 * generates a string containing the symbol table
	 * @return the string containing the symbol table
	 */
	@Override
	public String toString() {
		String s = "";
		s += symbolTable;
		return s;
	}
}