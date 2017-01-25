package response;

import java.util.ArrayList;

import beans.ResultBean;

/**
 * A class used for JSON Responses when queried for results.
 * @author Ethan Swartzentruber
 *
 */
public class ResultsResponse {
	
	//whether operation was successful
	private boolean success;
	
	//how many results there are
	private int numResults;
	
	//the results being returned
	private ArrayList<ResultBean> results;
	
	public ResultsResponse(boolean success, int numResults, ArrayList<ResultBean> results) {
		this.success = success;
		this.numResults = numResults;
		this.results = results;
	}
	
	public boolean getSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public int getNumResults() {
		return numResults;
	}
	public void setNumResults(int numResults) {
		this.numResults = numResults;
	}
	public ArrayList<ResultBean> getResults() {
		return results;
	}
	public void setResults(ArrayList<ResultBean> results) {
		this.results = results;
	}
	
}
