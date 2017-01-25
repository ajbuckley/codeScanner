/**
 * 
 */
package search;

import github.handling.CodeSearchHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import language.ExclusionList;
import language.JavaExclusionList;
import application.CodeScannerRestApi;
import beans.CandidateBean;
import beans.FileBean;
import beans.ResultBean;
import dao.CandidateDAO;
import dao.FileDAO;
import dao.ResultDAO;

/**
 * @author Drew Buckley, Ethan Swartzentruber
 *
 *	A Runnable object that controls the entire task of producing results from a given file.
 */
public class Searcher implements SearchHandler, Runnable {
	
	private FileBean file;
	CandidateDAO cDAO;
	ResultDAO rDAO;
	CodeSearchHandler codeSearcher;
	
	public Searcher(FileBean file){
		this.file = file;
		cDAO = CodeScannerRestApi.candidateDAO;
		rDAO = CodeScannerRestApi.resultDAO;
		codeSearcher = new CodeSearchHandler();
	}

	@Override
	public void run() {
		//System.out.println("Beginning search on file " + file.getFileName());
		generateCandidates(file);
		//System.out.println("Finished generating candidates for file " + file.getFileName());
		generateResults(file);
		System.out.println("Completed search for file " + file.getFileName());
		try{
		Thread.sleep(CodeScannerRestApi.waitTime);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	/**
	 * Generates search results for the file given
	 * 
	 * @param fileID : The id of the file to make results for
	 * @throws SQLException 
	 */
	public void generateResults(FileBean f) {
		
		Comparer compare = new Comparer();

		
		
		ArrayList<CandidateBean> candidates = null;
		
		try {
			candidates = cDAO.getByFile(f.getId());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		 
		 for(CandidateBean c: candidates){
		 	try {
				//Generate Results
		 		ArrayList<ResultBean> results = compare.compare(f, c);
		 		//System.out.println("" + results.size() + " results found for session number " + f.getSessionID());
				
		 		//Add results to DB
				for(ResultBean r: results){
					try {
						//System.out.println("Adding result for session number " + f.getSessionID());
						rDAO.add(r); //don't fix this, you'll get merge conflict. Fix later with will
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("ERROR: Exception for file " + f.getId() + " and candidate " + c.getId());
				e.printStackTrace();
			}
		 }
		 

	}

	@Override
	public void generateCandidates(FileBean f) {
		//The number of keywords we want to find
		int numKeywords = 4;
		
		
		
		
		File source = new File(f.getFilePath());
		
		ArrayList<String> keywords = new ArrayList<String>();
		ArrayList<Keyword> mostOften = new ArrayList<Keyword>();
		ExclusionList exclude = new JavaExclusionList(); 

		try{
		Scanner scan = new Scanner(source);
		//TODO This approach is stupid, just grabbing the first token from the first 3 lines.
		//Extremely preliminary dummy function used to just test the overall architecture.
		
		//
		while(scan.hasNextLine()){
			String line = scan.nextLine();
			line = line.trim();
			if(! exclude.isComment(line)){
				Scanner s = new Scanner(line);
				s.useDelimiter("[\\s|.]+");
				while(s.hasNext("[A-Z|a-z|;]+")){
					
					String token = s.next("[A-Z|a-z|;]+");
					token = token.replace(";", "");
					System.out.println("token: " + token);
					if(!exclude.containsKeyword(token)){
						boolean contained = false;
						for(Keyword key : mostOften){
							if(key.getWord().equalsIgnoreCase(token)){
								key.increment();
								contained = true;
							}
						}
						
						if(!contained){
							Keyword key = new Keyword(token);
							mostOften.add(key);
						}
						
					}
				}
				s.close();
			}
		}
		scan.close();
		//pick the most often keywords
		for(int i = 0; i < numKeywords || keywords.size() == mostOften.size(); i++){
			int index = -1;
			int most = -1;
			String keyword = null;
			//find the most frequent keyword
			for(Keyword key: mostOften){
				if(key.getFrequency() > most){
					most = key.getFrequency();
					keyword = key.getWord();
					index = mostOften.indexOf(key);
				}
				
			}
			keywords.add(keyword);
			if(index > -1){
				mostOften.remove(index);
			}
		}
		
		
		
		
		if(keywords.size() != 0){
			codeSearcher.startSearch(keywords, f.getId(), f.getSessionID()) ;
		}
		for(String k : keywords){
			System.out.println("keyword: " + k);
		}
		
		}catch(IOException e){
		System.out.println(e.getMessage());	
		}
		
	}
	
	/**
	 * 
	 * @author Drew
	 *
	 *Local class for keywords. holds the word and the frequency its used.
	 */
	class Keyword{
		//The frequecy the word is used
		private int frequency;
		//The word
		private String word;
		
		public Keyword(String word, int frequency){
			this.word  = word;
			this.frequency = frequency; 
		}
		
		public Keyword(String word){
			this.word  = word;
			this.frequency = 0; 
		}
		/**
		 * Increments the frequency of the keyword
		 */
		public void increment(){
			frequency ++;
		}
		
		/**
		 * @return the frequency
		 */
		public int getFrequency() {
			return frequency;
		}
		/**
		 * @param frequency the frequency to set
		 */
		public void setFrequency(int frequency) {
			this.frequency = frequency;
		}
		/**
		 * @return the word
		 */
		public String getWord() {
			return word;
		}
		/**
		 * @param word the word to set
		 */
		public void setWord(String word) {
			this.word = word;
		}
		
		
		
	}
	
}
