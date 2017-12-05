import java.util.*;

/**
 * 
 * @author Michael Sanfelippo, Jeremy Ethridge, Maria Nuila
 *
 * The lexer class separates the user input into different tokens. It does this utilizing a state machine.
 */
public class Lexer {

	/**
	 * hashmap to hold the list of keywords and operators and their associatedIDs
	 */
	private HashMap<String, Integer> tokenList;
	/**
	 * a simple line count
	 */
	private int lineCount;
	/**
	 * a list of tokens that have been parsed from the user's input
	 */
	private ArrayList<Token> output;
	/**
	 * flag to see if the decimal point has already existed for the current number
	 */
	private boolean decimalFlag;

	/**
	 * constructor to create a lexer that sets the decimal flag to false initially and the line count to 1
	 * also puts in all keywords/reserved tokens into the map of tokens
	 */
	public Lexer() {
		decimalFlag = false;
		lineCount = 1;
		output = new ArrayList<Token>();
		tokenList = new HashMap<String, Integer>();
		tokenList.put(",", 0);
		tokenList.put(";", 1);
		tokenList.put("prog", 2);
		tokenList.put("vars", 3);
		tokenList.put("int", 4);
		tokenList.put("string", 5);
		tokenList.put("while", 6);
		tokenList.put("input", 7);
		tokenList.put("print", 8);
		tokenList.put("if", 9);
		tokenList.put("else", 10);
		tokenList.put("<", 11);
		tokenList.put(">", 12);
		tokenList.put("{", 13);
		tokenList.put("}", 14);
		tokenList.put("(", 15);
		tokenList.put(")", 16);
		tokenList.put("*", 17);
		tokenList.put("^", 18);
		tokenList.put("=", 19);
		tokenList.put("-", 20);
		tokenList.put("+", 21);
		tokenList.put("/", 22);
		tokenList.put("==", 23);
		tokenList.put("!=", 24);
		tokenList.put("<=", 25);
		tokenList.put(">=", 26);
		tokenList.put("<<", 27);
		tokenList.put(">>", 28);
		tokenList.put("float", 29);
		tokenList.put("String", 30);
		tokenList.put("id", 31);
		tokenList.put("integer", 32);
		tokenList.put("double", 33);
		tokenList.put("eof", 34);
		tokenList.put("%", 35);
	}

	/**
	 * This method peeks at the next character in the character stream. If there
	 * is no next character, that means that the user's code is syntactically
	 * incorrect
	 * 
	 * @param tokenStream the stream of tokens
	 * @param currentIndex the current index of the stream
	 * @return the next character in the stream if it exists, null otherwise
	 */
	public String peek(char[] tokenStream, int currentIndex) {
		try {
			return tokenStream[currentIndex + 1] + "";
		} catch (IndexOutOfBoundsException e) {
			System.out.println("You made a mistake on line: " + lineCount);
			return null; // returning null here forces me to try...catch(npe) in the main run method, but idk what else to return to indicate error
		}
	}

	/**
	 * This method checks if the current token is a keyword or some other
	 * operator contained in the language
	 * @param token the token to be checked
	 * @return true if it is contained in the language
	 */
	public boolean containsToken(String token) {
		return tokenList.containsKey(token);
	}

	/**
	 * this method will return the keyword/operator's identifying number, or 0
	 * if it is not a keyword/operator
	 * @param token the token to be put in the hashmap
	 * @return the keyword/operator's identifying number, or 0 if it is not one of these things
	 */
	public int getValue(String token) {
		return containsToken(token) ? tokenList.get(token) : 0;
	}

	/**
	 * This method will take a character stream and a current index to recognize
	 * an integer or float
	 * @param tokenStream the character stream
	 * @param currentIndex the current index of the stream
	 * @return the index of the stream where the number ends
	 */
	public int recognizeNumbers(char[] tokenStream, int currentIndex) {
		String str = "";
		char c;
		while (peek(tokenStream, currentIndex).matches("[0-9]|\\.]*")) {
			c = tokenStream[currentIndex];
			str += c;
			currentIndex++;

			if (c == '.' && !decimalFlag) {
				decimalFlag = true;
				while (peek(tokenStream, currentIndex).matches("[0-9]")) {
					c = tokenStream[currentIndex];
					str += c;
					currentIndex++;
				}
			}

		}
		str += tokenStream[currentIndex];
		if (decimalFlag) {
			output.add(new Token("float", getValue("double"), lineCount, str));
		} else {
			output.add(new Token("integer", getValue("integer"), lineCount, str));
		}
		return currentIndex;
	}

	/**
	 * Recognizes an identifier (as in, a variable name or something similar to that)
	 * @param tokenStream the stream of characters
	 * @param currentIndex the current index of the stream
	 * @return the ending index of the identifier
	 */
	public int recognizeIdentifier(char[] tokenStream, int currentIndex) {
		char c;
		String str = "";
		try {
			while (peek(tokenStream, currentIndex).matches("[a-zA-Z_$0-9]*")) {
				c = tokenStream[currentIndex];
				str += c;
				currentIndex++;
			}
		} catch (NullPointerException e) {
			System.out.println("A mistake has been made!"); 
		}
		str += tokenStream[currentIndex];
		if (containsToken(str)) {
			output.add(new Token(str, getValue(str), lineCount)); // it's a keyword
		} else {
			output.add(new Token("id", getValue("id"), lineCount, str)); // it's an identifier
		}
		return currentIndex;
	}

	/**
	 * Skips comments in the user's code
	 * @param tokenStream the character stream
	 * @param startIndex the beginning index of the comment
	 * @param token the current token
	 * @return the ending index of the comment
	 */
	public int skipComments(char[] tokenStream, int startIndex, String token) {
		int endIndex = startIndex;
		while (!token.endsWith("\\n")) {
			endIndex++;
			token += tokenStream[endIndex];
		}
		return endIndex - 3; // -3 for the new line char
	}

	/**
	 * Accepts the user inputed code and returns it
	 * @return the user's code as a single string
	 */
	public String gatherInput() {
		Scanner in = new Scanner(System.in);
		String stream = "";
		System.out
				.println("Please enter the program code. To indicate the end of the code, input a '$' on a new line.");
		String input = in.nextLine();
		while (!input.equals("$")) {
			stream += input + " \\n";
			input = in.nextLine();
		}
		in.close();
		return stream;
	}

	/**
	 * Iterates through the character stream and is able to recognize keywords,
	 * operators, strings, numbers, identifiers, and errors in code this is done
	 * via regular expressions along with the use of the peek function to check
	 * what comes next in the stream without actually going there Wasn't sure
	 * how to handle errors properly, so I simply had an empty catch statement
	 */
	public Lexer run() {
		char[] tokenStream = gatherInput().toCharArray();
		String token = "";
		String str = "";
		boolean stringState = false;
		for (int currentIndex = 0; currentIndex < tokenStream.length; currentIndex++) {
			try {
				char c = tokenStream[currentIndex];
				token += c;
				if (token.equals(" ")) {
					token = "";
					decimalFlag = false; // I have to do this every time
				} else if (token.endsWith("\\n")) {
					token = "";
					lineCount++;
					decimalFlag = false;
				} else if (token.equals("/") && peek(tokenStream, currentIndex).equals("/")) {
					currentIndex = skipComments(tokenStream, currentIndex, token);
				} else if (containsToken(token)) {
					if (peek(tokenStream, currentIndex).equals("=")) {
						currentIndex++;
						token += tokenStream[currentIndex];
					} else if (peek(tokenStream, currentIndex).equals(">")) {
						currentIndex++;
						token += tokenStream[currentIndex];
					} else if (peek(tokenStream, currentIndex).equals("<")) {
						currentIndex++;
						token += tokenStream[currentIndex];
					}
					output.add(new Token(token, getValue(token), lineCount));
					token = "";
					decimalFlag = false;
				} else if (token.matches("[0-9]")) {
					currentIndex = recognizeNumbers(tokenStream, currentIndex);
					str = "";
					token = "";
					decimalFlag = false;
				} else if (token.endsWith("\"")) {
					if (!stringState) {
						stringState = true;
					} else {
						output.add(new Token("string", getValue("string"), lineCount, str));
						str = "";
						token = "";
						stringState = false;
						decimalFlag = false;
					}
				} else if (token.matches("^[a-zA-Z_$]") && !stringState) {
					currentIndex = recognizeIdentifier(tokenStream, currentIndex);
					str = "";
					token = "";
					decimalFlag = false;
				} else if (stringState) {
					str += c;
				}
			} catch (Exception e) {
				// should probably put something here...
			}
		}
		output.add(new Token("$", 0, lineCount - 1)); // this is the eof token
		return this;
	}

	/**
	 * Displays the output in a formatted way thanks to overriding Token's toString() method
	 */
	public void displayOutput() {
		for (Token t : output) {
			System.out.println(t);
		}
	}

	/**
	 * gets the token output list to be fed to the parser
	 * @return the token output list
	 */
	public ArrayList<Token> getTokenOutputList() {
		return output;
	}
}