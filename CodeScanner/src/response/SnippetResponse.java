package response;

public class SnippetResponse {
	
	
	private boolean success;
	private String errorMsg;
	private String sourceSnippet;
	private String candidateSnippet;
	
	
	public SnippetResponse(boolean success, String errorMsg, String sourceSnippet, String candidateSnippet) {
		this.success = success;
		this.errorMsg = errorMsg;
		this.sourceSnippet = sourceSnippet;
		this.candidateSnippet = candidateSnippet;
	}


	public boolean isSuccess() {
		return success;
	}


	public void setSuccess(boolean success) {
		this.success = success;
	}


	public String getErrorMsg() {
		return errorMsg;
	}


	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


	public String getSourceSnippet() {
		return sourceSnippet;
	}


	public void setSourceSnippet(String sourceSnippet) {
		this.sourceSnippet = sourceSnippet;
	}


	public String getCandidateSnippet() {
		return candidateSnippet;
	}


	public void setCandidateSnippet(String candidateSnippet) {
		this.candidateSnippet = candidateSnippet;
	}
	
	
	

}
