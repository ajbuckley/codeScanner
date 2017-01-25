package response;

/**
 * Class representing JSON response for startSearch.
 * @author Ethan Swartzentruber
 *
 */
public class StartResponse{
	private final int sessionID;
	private final boolean success;
	private final String errorMsg;
	
	public StartResponse(int sessionID, boolean success, String errorMsg){
		this.sessionID = sessionID;
		this.success = success;
		this.errorMsg = errorMsg;
	}
	
	public int getSessionID(){
		return this.sessionID;
	}
	
	public boolean getSuccess(){
		return this.success;
	}
	
	public String getErrorMsg(){
		return this.errorMsg;
	}
	
}
