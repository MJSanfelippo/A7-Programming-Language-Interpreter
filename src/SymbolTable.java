import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Michael Sanfelippo, Jeremy Ethridge, Maria Nuila
 * 
 * A symbol table simply holds many symbol table entries. It cannot contain duplicate entries where a duplicate entry is defined as two entries having the same variable name
 * It must be searchable for a specific ID to find the corresponding entry
 *
 */
public class SymbolTable {
	/**
	 * a set containing the entries to the symbol table
	 */
	private Set<SymbolTableEntry> symbolTable;

	/**
	 * constructor for the symbol table that just instantiates the set containing the entries
	 */
	public SymbolTable() {
		this.symbolTable = new HashSet<SymbolTableEntry>();
	}

	/**
	 * displays an error and exits the program
	 * @param msg the message to be displayed
	 */
	public void error(String msg){
		System.out.println(msg);
		System.exit(0);
	}
	
	/**
	 * adds the entry to the symbol table
	 * it will produce an error if it is a duplicate variable name
	 * @param symbolTableEntry the entry to be added
	 */
	public void addSymbolTableEntry(SymbolTableEntry symbolTableEntry) {
		if (!symbolTable.add(symbolTableEntry)){
			error("ERROR: duplicate variable name");
		}
	}

	/**
	 * finds an entry in the symbol table based on the variable's name
	 * @param id the name of the variable
	 * @return the entry if found, null otherwise
	 */
	public SymbolTableEntry findEntryById(String id) {
		for (SymbolTableEntry n : symbolTable) {
			if (id.equals(n.getName())) {
				return n;
			}
		}
		return null;
	}

	/**
	 * generates a string containing all the entries in the symbol table
	 * @return a string containing all the entries in the symbol table
	 */
	@Override
	public String toString() {
		String s = "";
		for (SymbolTableEntry n : symbolTable) {
			s += n;
		}
		return s;
	}
}