import java.util.ArrayList;

/**
 * 
 * @author Michael Sanfelippo, Jeremy Ethridge, Maria Nuila
 * 
 * A parse tree node (which will eventually become abstract syntax tree nodes) contains an id, a value, a type, an entry, a variable value, and a list of children nodes.
 * The parse tree (and AST) is built utilizing the Parser class which utilizes the PSTNode class to create parse tree nodes.
 * 
 *
 */
public class PSTNode {

	/**
	 * This is the variable value of the node
	 */
	private String variableValue;
	/**
	 * This is the entry of the node in the symbol table if it's an id
	 */
	private SymbolTableEntry entry;
	/**
	 * this is the type of node (any nonterminal or terminal)
	 */
	private String type;

	/**
	 * this is the unique id of the node
	 */
	private int id = 0;
	/**
	 * this is the value of the node. this could be the same as the type if it
	 * is a nonterminal, or it could be an actual value. ex: if type is integer,
	 * value could be 4.
	 */
	private String value;
	/**
	 * this is a list of other nodes who are children to this node
	 */
	private ArrayList<PSTNode> children;

	/**
	 * constructor for a node that just takes an id
	 * @param counter the id of the node
	 */
	public PSTNode(int counter) {
		id = counter;
		this.type = "";
		this.value = "";
		children = new ArrayList<PSTNode>();
		variableValue = value;
	}

	/**
	 * constructor for a node that takes a type and an id
	 * @param type the type of node
	 * @param counter the id of the node
	 */
	public PSTNode(String type, int counter) {
		id = counter;
		this.type = type;
		this.value = type;
		children = new ArrayList<PSTNode>();
		variableValue = value;
	}

	/**
	 * constructor for a node that takes a type, a value, and an id
	 * @param type the type of node
	 * @param value the value of the node
	 * @param counter the id of the node
	 */
	public PSTNode(String type, String value, int counter) {
		id = counter;
		this.type = type;
		this.value = value;
		children = new ArrayList<PSTNode>();
		variableValue = value;
	}

	/**
	 * Sets the variable value
	 * @param s the value to be set
	 */
	public void setVariableValue(String s) {
		this.variableValue = s;
	}

	/**
	 * Gets the variable value
	 * @return the variable value
	 */
	public String getVariableValue() {
		return variableValue;
	}

	/**
	 * sets the type of node
	 * @param type the type of node
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the entry in the symbol table
	 * @return the entry
	 */
	public SymbolTableEntry getEntry() {
		return entry;
	}

	/**
	 * sets the entry of the node
	 * @param symTabEntry the entry to be set
	 */
	public void setEntry(SymbolTableEntry symTabEntry) {
		this.entry = symTabEntry;
	}

	/**
	 * gets the value of the node
	 * @return the value of the node
	 */
	public String getValue() {
		return value;
	}

	/**
	 * sets the value of the node
	 * @param s the value of the node
	 */
	public void setValue(String s) {
		value = s;
		variableValue = s;
	}

	/**
	 * gets the type of the node
	 * @return the type of node
	 */
	public String getType() {
		return type;
	}

	/**
	 * adds a child to the node
	 * @param node the node to be added as a child
	 */
	public void addChild(PSTNode node) {
		children.add(node);
	}

	/**
	 * gets all the children of the node
	 * @return the children of the node
	 */
	public ArrayList<PSTNode> getChildren() {
		return this.children;
	}

	/**
	 * sets the children of the node
	 * @param children the children to be set to this node
	 */
	public void setChildren(ArrayList<PSTNode> children) {
		this.children = children;
	}

	/**
	 * gets the number of children this node has
	 * @return the number of children this node has
	 */
	public int getNumberOfChildren() {
		if (children == null) {
			return 0;
		}
		return children.size();
	}

	/**
	 * returns the node's id
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * gets a child at a specific index
	 * @param index the index the node is at
	 * @return the child at that index
	 */
	public PSTNode getChildAt(int index) {
		return children.get(index);
	}

	/**
	 * returns the value and the id as a string overrides the Object's
	 * toString() method
	 * @return the string that includes the type, value, id, and variable value
	 */
	@Override
	public String toString() {
		return "type: " + type + ", value: " + value + ", variableValue: " + variableValue + ", id: " + id;
	}
}