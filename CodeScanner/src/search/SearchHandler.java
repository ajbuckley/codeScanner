/**
 * 
 */
package search;

import beans.FileBean;

/**
 * @author Drew
 *
 */
public interface SearchHandler {
	public void generateCandidates(FileBean f);
	public void generateResults(FileBean f);

}
