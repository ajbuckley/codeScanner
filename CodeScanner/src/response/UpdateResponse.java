package response;

public class UpdateResponse {
	private final boolean success;
	private final String errorMsg;
	
	
	public UpdateResponse(boolean success, String errorMsg) {
		super();
		this.success = success;
		this.errorMsg = errorMsg;
	}


	public boolean getSuccess() {
		return success;
	}


	public String getErrorMsg() {
		return errorMsg;
	}
	
	
	
}
