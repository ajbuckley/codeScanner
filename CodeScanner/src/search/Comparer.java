package search;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import beans.CandidateBean;
import beans.FileBean;
import beans.ResultBean;

/**
 * @author Drew
 *
 * Class for making all the necessary comparisons between files and lines.
 * Also creates results and adds them to the database. 
 */
public class Comparer {

	public static final float MIN_THRESH = (float) 0.20;

	public Comparer(){
		
	}
	
	/** @author Ethan Swartzentruber
	 * Arbitrarily decided that file1 is the file we are given, and file2 is the
	 * candidate.
	 * Finds the longest identically matching section in the file. Percentage is
	 * the average of how much of the files that that section takes up.
	 * Prints the start and length, I'm not sure of how this is supposed to work
	 * since it is set to return a float. Didn't want to mess with that.
	 * 
	 * @param source
	 * @param candidate
	 * @return
	 * @throws IOException
	 */
	public ArrayList<ResultBean> compare(FileBean source, CandidateBean candidate) throws IOException {
		
		ArrayList<ResultBean> results = new ArrayList<ResultBean>();
		
		File file1 = new File(source.getFilePath());
		
		File file2 = new File(candidate.getFilePath());
		
		// Start in file1 for longest matching section
		//int start1 = -1;

		// Start in file2 for longest matching section
		//int start2 = -1;

		// Length (in lines) of longest matching section
		//int length = -1;

		// file 1 start for section currently being considered
		int currentStart1 = -1;

		// file 2 start for section currently being considered
		int currentStart2 = -1;

		// Length in lines of section currently being considered
		int currentLength = 0;
		
		//score of the current block
		float score = (float) 0.0;

		// Used to track redundant matches
		boolean flag;

		// fucky as this is, we are storing pairs in these. Sorry fam
		ArrayList<Integer> p1 = new ArrayList<Integer>();
		ArrayList<Integer> p2 = new ArrayList<Integer>();
		
		ArrayList<Integer> starts = new ArrayList<Integer>();
		ArrayList<Integer> lengths = new ArrayList<Integer>();
		List<String> lines1;
		List<String> lines2;
		try{
		lines1 = Files.readAllLines(file1.toPath(),
				StandardCharsets.UTF_8);
		lines2 = Files.readAllLines(file2.toPath(),
				StandardCharsets.UTF_8);
		}catch(MalformedInputException e){
			//If characters cannot be encoded, we return no results.
			System.out.println("Could not encode file " + source.getFileName() + " or one of its candidates.");
			return results;
		}
		for (int i = 0; i < lines1.size(); i++) {
			for (int j = 0; j < lines2.size(); j++) {
				if (compareLine(lines1.get(i),lines2.get(j)) >= MIN_THRESH){
					if(!lines1.get(i).trim().equals("") && !lines2.get(j).trim().equals("")){
						p1.add(i); //We add the pair after checking that they are close and that neither is blank
						p2.add(j);
					}
				}
			}
		}

		for (int k = 0; k < p1.size(); k++) {
			score = (float) 0.0;
			currentStart1 = p1.get(k);
			currentStart2 = p2.get(k);
			currentLength = 0;
			flag = true;
			
			//first we check if the current match is going to be redundant.
			for(int i = 0; i < starts.size() && flag;i++){
				if(currentStart1 > starts.get(i) && currentStart1 <= starts.get(i) + lengths.get(i)){
					flag = false;
					//System.out.println("Flagged block as redundant to block " + i);
				}
				
			}

			// this is gross for loop syntax, we use variables that already
			// exist for it
			for (; currentStart1 + currentLength < lines1.size() && currentStart2 + currentLength < lines2.size() && flag; currentLength++) {
				float comp = compareLine(lines1.get(currentStart1 + currentLength),lines2.get(currentStart2 + currentLength));
				if(lines1.get(currentStart1 + currentLength).trim().equals("") || lines2.get(currentStart2 + currentLength).trim().equals("")){
					comp = (float)0.0;
				}
				if (comp < MIN_THRESH) {
					break;
				}else{
					score += comp;
				}
			}
			score /= ((float) currentLength);
			if(flag && currentLength > 1) { //if the match is not redundant to another match and has greater than 1 length
				ResultBean r = new ResultBean();
				
				r.setCandidateID(candidate.getId());
				r.setCandidateStartLine(currentStart2);
				r.setSourceFileID(source.getId());
				r.setLength(currentLength);
				r.setMatchRating(score);
				r.setSessionID(source.getSessionID());
				r.setSourceStartLine(currentStart1);
				r.setIsOkay(false); //this is the default for isOkay field
				r.setSourceFileName(source.getFileName());
				r.setCandidateFileName(candidate.getFileName());
				r.setCandidateUrl(candidate.getRepoURL());
				results.add(r);
				
				/*System.out.println("-------------------------");
				System.out.println("Startline1: " + currentStart1);
				System.out.println("Startline2: " + currentStart2);
				System.out.println("Length: " + currentLength);
				System.out.println("Score: " + score);*/
				
				starts.add(currentStart1);
				lengths.add(currentLength);
			}
			
		}

		

		return results;
	}
	
	/**
	 * 
	 * 
	 * @param line1
	 * @param line2
	 * @return
	 */
	private float compareLine(String line1, String line2){
			//The score
			float score = 1;
			
			//Trivially accept strings that are equal
			if(line1.equals(line2)){
				return score;
			}
			
			//Remove all whitespace
			line1.trim();
			line2.trim();
			line1.replace(" ", "");
			line2.replace(" ", "");
			
			//If the same after whitespace removal return .99
			if(line1.equals(line2)){
				score *= .99; 
				return score;
			}
			//Get levenstein and normalize, then return score
			else{
				float levDist = levenshteinDistance(line1, line2);
				int max = Integer.max(line1.length(), line2.length());
				score = 1 - (levDist/max);
				return score;
			}
						
		}
	/**
	 * 
	 * @author Wikipedia: https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Kotlin
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	private int levenshteinDistance(CharSequence lhs, CharSequence rhs) {
		int len0 = lhs.length() + 1;
		int len1 = rhs.length() + 1;

		// the array of distances
		int[] cost = new int[len0];
		int[] newcost = new int[len0];

		// initial cost of skipping prefix in String s0
		for (int i = 0; i < len0; i++)
			cost[i] = i;

		// dynamically computing the array of distances

		// transformation cost for each letter in s1
		for (int j = 1; j < len1; j++) {
			// initial cost of skipping prefix in String s1
			newcost[0] = j;

			// transformation cost for each letter in s0
			for (int i = 1; i < len0; i++) {
				// matching current letters in both strings
				int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

				// computing cost for each transformation
				int cost_replace = cost[i - 1] + match;
				int cost_insert = cost[i] + 1;
				int cost_delete = newcost[i - 1] + 1;

				// keep minimum cost
				newcost[i] = Math.min(Math.min(cost_insert, cost_delete),
						cost_replace);
			}

			// swap cost/newcost arrays
			int[] swap = cost;
			cost = newcost;
			newcost = swap;
		}

		// the distance is the cost for transforming all letters in both strings
		return cost[len0 - 1];
	}
}