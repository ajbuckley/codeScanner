package beans;

/**
 * @author Drew Buckley
 * 
 * Bean for holding file info
 *
 */
public class FileBean {
	//The name of the file
	private String fileName;
	//The unique id for the file
	private int UUID;
	//The current path of the file
	private String filePath;
	//The id of the repo the file belongs to
	private int repoID;
	//The search session the file belongs to
	private int sessionID;
	
	/**
	 * Blank constructor. Used for beans whose properties should be
	 * initialized at a later time.
	 */
	public FileBean() { }
	
	/**
	 * ID constructor. Used for beans who only need an ID defined.
	 * 
	 * @param id The unique ID value of the file
	 */
	public FileBean(int id){
		this.setId(id);
	}
	
	/**
	 * Default constructor. Initializes everything but UUID, which is
	 * assigned by the database when uploaded.
	 * 
	 * @param name The name of the file
	 * @param pathName The location of the file
	 * @param repoID The ID of the repository this file belongs to
	 * @param sessionID The search session this file is associated with
	 */
	public FileBean(String name, String pathName, int repoID, int sessionID) {
		this.setFileName(name);
		this.setFilePath(pathName);
		this.setRepoID(repoID);
		this.setSessionID(sessionID);
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
	 * @return the repoID
	 */
	public int getRepoID() {
		return repoID;
	}

	/**
	 * @param repoID the repoID to set
	 */
	public void setRepoID(int repoID) {
		this.repoID = repoID;
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

}