
/**
 * 
 * @author Michael Sanfelippo, Jeremy Ethridge, Maria Nuila
 * 
 * A symbol table entry contains the name of the variable, the value of the variable (which upon declaration will be null), and a pointer to the declaration in the AST
 * Symbol table entries are put into the symbol table for a specfic scope
 *
 */
public class SymbolTableEntry {

	/**
	 * the name of the entry (variable's name)
	 */
	private String name;
	/**
	 * the value of the variable
	 */
	private String value;
	/**
	 * the link to where the variable was declared in the abstract syntax tree
	 */
	private PSTNode declaration;

	/**
	 * constructor for a symbol table entry
	 * the value is never set with the declaration
	 * @param name the name of the variable (entry)
	 * @param declaration the link to the declaration in the ast
	 */
	public SymbolTableEntry(String name, PSTNode declaration) {
		this.name = name;
		this.declaration = declaration;
	}

	/**
	 * gets the name of the variable
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the name of the variable
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * gets the value of the variable
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * sets the value of the variable
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * gets the declaration of the entry
	 * @return the declaration
	 */
	public PSTNode getDeclaration() {
		return declaration;
	}

	/**
	 * sets the declaration
	 * @param declaration the declaration to set
	 */
	public void setDeclaration(PSTNode declaration) {
		this.declaration = declaration;
	}

	/**
	 * returns a string containing the name, value, and declaration
	 * @return a string containing the name, value, and declaration
	 */
	@Override
	public String toString() {
		return "id: " + name + "     value: " + value + "    node: " + declaration;
	}
	
	/**
	 * compares two entries to see if their names are the same
	 * @param o the object being compared
	 * @return true if the variable names are the same, false otherwise
	 */
	@Override
	public boolean equals(Object o){
		if (o == this){
			return true;
		}
		if (!(o instanceof SymbolTableEntry)){
			return false;
		}
		SymbolTableEntry entry = (SymbolTableEntry) o;
		return entry.getName().equals(name);
	}
	
	/**
	 * returns the hashcode of the name
	 * @return the hashcode of the name
	 */
	@Override
	public int hashCode(){
		return name.hashCode();
	}

}