package beans;

/**
 * @author Drew Buckley
 * 
 * Bean for holding result info
 *
 */
public class ResultBean {
	//The Rating for the match
	private float matchRating;
	//The ID of the file
	private int sourceFileID;
	//The start line of the match in the file
	private int sourceStartLine;
	//The number of lines in the match
	private int length;
	//The start of the match in the candidate file
	private int candidateStartLine;
	//The id of the candidate file
	private int candidateID;
	//The id of this result
	private int UUID;
	//The search session id
	private int sessionID;
	//Name of the source file, for display
	private String sourceFileName;
	//Name of the candidate file, for display
	private String candidateFileName;
	//URL to candidate file for viewing by user
	private String candidateUrl;
	//Whether the match is acceptable or not
	private boolean isOkay;
	
	/**
	 * Blank constructor. Used for beans whose properties should be
	 * initialized at a later time.
	 */
	public ResultBean() { }
	
	/**
	 * ID constructor. Used for beans who only need an ID defined.
	 * 
	 * @param id
	 */
	public ResultBean(int id){
		this.setUUID(id);
	}
	
	/**
	 * Default constructor. Initializes everything but UUID, which is
	 * assigned by the database when uploaded.
	 * 
	 * @param matchRating
	 * @param fileID
	 * @param startLn
	 * @param length
	 * @param candidateStartLn
	 * @param candidateID
	 * @param sessionID The search session this result is associated with
	 * @param isOkay Whether the match is acceptable or not
	 */
	public ResultBean(float matchRating, int fileID, int startLn, int length, int candidateStartLn,
			int candidateID, int sessionID, boolean isOkay) {
		this.setMatchRating(matchRating);
		this.setSourceFileID(fileID);
		this.setSourceStartLine(startLn);
		this.setLength(length);
		this.setCandidateStartLine(candidateStartLn);
		this.setCandidateID(candidateID);
		this.setSessionID(sessionID);
		this.setIsOkay(isOkay);
	}
	
	/**
	 * @return the candidateID
	 */
	public int getCandidateID() {
		return candidateID;
	}
	/**
	 * @param candidateID the candidateID to set
	 */
	public void setCandidateID(int candidateID) {
		this.candidateID = candidateID;
	}
	/**
	 * @return the candidateStartln
	 */
	public int getCandidateStartLine() {
		return candidateStartLine;
	}
	/**
	 * @param candidateStartln the candidateStartln to set
	 */
	public void setCandidateStartLine(int candidateStartln) {
		this.candidateStartLine = candidateStartln;
	}
	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}
	/**
	 * @return the startLn
	 */
	public int getSourceStartLine() {
		return sourceStartLine;
	}
	/**
	 * @param startLn the startLn to set
	 */
	public void setSourceStartLine(int startLn) {
		this.sourceStartLine = startLn;
	}
	/**
	 * @return the fileID
	 */
	public int getSourceFileID() {
		return sourceFileID;
	}
	/**
	 * @param fileID the fileID to set
	 */
	public void setSourceFileID(int fileID) {
		this.sourceFileID = fileID;
	}
	/**
	 * @return the matchRating
	 */
	public float getMatchRating() {
		return matchRating;
	}
	/**
	 * @param f the matchRating to set
	 */
	public void setMatchRating(float matchRating) {
		this.matchRating = matchRating;
	}

	/**
	 * @return the sessionID
	 */
	public int getSessionID() {
		return sessionID;
	}

	/**
	 * @param sessionID the sessionID to set
	 */
	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}

	/**
	 * @return the id
	 */
	public int getUUID() {
		return UUID;
	}

	/**
	 * @param id the id to set
	 */
	public void setUUID(int id) {
		this.UUID = id;
	}
	
	/**
	 * @return the id
	 */
	public boolean getIsOkay() {
		return isOkay;
	}

	/**
	 * @param UUID the id to set
	 */
	public void setIsOkay(boolean isOkay) {
		this.isOkay = isOkay;
	}

	public String getSourceFileName() {
		return sourceFileName;
	}

	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	public String getCandidateFileName() {
		return candidateFileName;
	}

	public void setCandidateFileName(String candidateFileName) {
		this.candidateFileName = candidateFileName;
	}

	public String getCandidateUrl() {
		return candidateUrl;
	}

	public void setCandidateUrl(String candidateUrl) {
		this.candidateUrl = candidateUrl;
	}
	
	
}