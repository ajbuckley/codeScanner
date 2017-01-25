/**
 * 
 */
package language;

import java.util.ArrayList;

/**
 * @author Drew Buckley
 * 
 * Abstract Class for Language specific tokenization.
 *
 */
public abstract class ExclusionList extends ArrayList<String> {
	
	public ExclusionList(){
		this.add("if");
		this.add("a");
		this.add("author");
		this.add("and");
		this.add("or");
		
	}
	/**
	 * Returns if it contains the keyword given
	 * 
	 * @param compare
	 * @return
	 */
	public abstract boolean containsKeyword(String compare);
	/**
	 * Returns true if the line starts wit the languages comment character.
	 * 
	 * @param line
	 * @return
	 */
	public abstract boolean isComment(String line);
	/**
	 * Returns the languages comment characters
	 * 
	 * @return
	 */
	public abstract ArrayList<String> getCommentChars();
}
