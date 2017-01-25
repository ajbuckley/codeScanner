package beans;

/**
 * @author Drew Buckley
 * 
 * Bean for holding candidate file info
 *
 */
public class CandidateBean {
	//The unique id of the candidate file
	private int UUID;
	//The name of the candidate file
	private String fileName;
	//The current path name of the candidate file
	private String filePath;
	//The url for the repo of the candidate file
	private String repoURL;
	//The license of the candidate file
	private String license;
	//The search session the file belongs to
	private int sessionID;
	//The source file id
	private int associatedFile;
	
	/**
	 * Blank constructor. Used for beans whose properties should be
	 * initialized at a later time.
	 */
	public CandidateBean() { }
	
	/**
	 * ID constructor. Used for beans who only need an ID defined.
	 * 
	 * @param id The unique ID value of the candidate
	 */
	public CandidateBean(int id){
		this.setId(id);
	}
	
	/**
	 * Default constructor. Initializes everything but UUID, which is
	 * assigned by the database when uploaded.
	 * 
	 * @param name The name of the candidate file
	 * @param pathName The location of the candidate file on the server
	 * @param repoURL The repository the candidate file is associated with
	 * @param license License information relevant to the candidate file
	 * @param sessionID The search session this candidate is associated with
	 */
	public CandidateBean(String name, String pathName, String repoURL, String license, int sessionID) {
		this.setFileName(name);
		this.setFilePath(pathName);
		this.setRepoURL(repoURL);
		this.setLicense(license);
		this.setSessionID(sessionID);
	}

	/**
	 * @return the license
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * @param license the license to set
	 */
	public void setLicense(String license) {
		this.license = license;
	}

	/**
	 * @return the repoURL
	 */
	public String getRepoURL() {
		return repoURL;
	}

	/**
	 * @param repoURL the repoURL to set
	 */
	public void setRepoURL(String repoURL) {
		this.repoURL = repoURL;
	}

	/**
	 * @return the pathName
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param pathName the pathName to set
	 */
	public void setFilePath(String pathName) {
		this.filePath = pathName;
	}

	/**
	 * @return the name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param name the name to set
	 */
	public void setFileName(String name) {
		this.fileName = name;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return UUID;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.UUID = id;
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
	 * @return the associatedFile
	 */
	public int getAssociatedFile() {
		return associatedFile;
	}

	/**
	 * @param associatedFile the associatedFile to set
	 */
	public void setAssociatedFile(int associatedFile) {
		this.associatedFile = associatedFile;
	}

}