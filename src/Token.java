/**
 * 
 * @author Michael Sanfelippo, Jeremy Ethridge, Maria Nuila
 * 
 * A token can be described as a piece of the input. It can be a string, a number, a variable ID, a variable declaration, a while keyword etc.
 * The input must be separated into tokens as dictated by the Lexer class
 *
 */

public class Token {

    /**
     * The type of token
     */
	private String type;
	/**
	 * The id of the token
	 */
	private int id;
	/**
	 * The line number of the token
	 */
	private int lineNumber;
	/**
	 * the value of the token
	 */
	private String value;
	
    /**
     * constructor for a token that takes in a type, an id, and a line number
     * @param type the type of token
     * @param id the id of the token
     * @param lineNumber the line number of the token
     */
	public Token (String type, int id, int lineNumber){
		this.type = type;
		this.id = id;
		this.lineNumber = lineNumber;
		this.value = type;
	}
	
	/**
	 * constructor for a token that takes in a type, an id, a line number, and a value
	 * @param type the type of token
	 * @param id the id of the token
	 * @param lineNumber the line number of the token
	 * @param value the value of the token
	 */
	public Token(String type, int id, int lineNumber, String value){
		this.type = type;
		this.id = id;
		this.lineNumber = lineNumber;
		this.value = value;
	}
	
	/**
	 * gets the type of the token
	 * @return the type of the token
	 */
	public String getType(){
		return type;
	}
	
	/**
	 * gets the line number of the token
	 * @return the line number of the token
	 */
	public int getLineNumber(){
		return lineNumber;
	}
        
	/**
	 * Determines if a token is equal to another
	 * @param o the object in question
	 * @return true if the token is the same, false otherwise
	 */
	@Override
	public boolean equals(Object o){
		if (o instanceof Token){
			Token temp = (Token) o;
			return this.type.equals(temp.getType());
		}
		return false;
	}
	
	/**
	 * gets the value of the token
	 * @return the value of the token
	 */
	public String getValue(){
		return value;
	}
	
	/**
	 * @return the hashcode of the token
	 */
	@Override
	public int hashCode() {
		return type.hashCode() + id + value.hashCode();
	}
	
	/**
	 * @return a string containing the token's id, line number, and value
	 */
	@Override
	public String toString(){
		String str = "(tok: " + id + " line= " + lineNumber + " str= \"" + value + "\"";
		if (type.equals("integer")){ // put integer if its number
			str += " int= " + value + ")";
		} else if (type.equals("double")){ // put double if float
			str += " float= " + value + ")";
		} else {
			str += ")";
		}
		str += "     " + type;
		return str;
	}
	
}