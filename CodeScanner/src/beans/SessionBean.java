package beans;

public class SessionBean {
	//unique ID
	private int UUID;
	
	//time the serach was started, as string because Date object wasn't behaving with SQL. Sorry
	private String startTime;
	
	//How many files have been completed for this session
	private int filesComplete;
	
	//Total files for this session
	private int totalFiles;
	
	
	public SessionBean() {
		
	}
	
	
	//GETTERS/SETTERS
	
	public int getUUID() {
		return UUID;
	}
	public void setUUID(int uUID) {
		UUID = uUID;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public int getFilesComplete() {
		return filesComplete;
	}
	public void setFilesComplete(int filesComplete) {
		this.filesComplete = filesComplete;
	}
	public int getTotalFiles() {
		return totalFiles;
	}
	public void setTotalFiles(int totalFiles) {
		this.totalFiles = totalFiles;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + UUID;
		result = prime * result + filesComplete;
		result = prime * result
				+ ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + totalFiles;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SessionBean other = (SessionBean) obj;
		if (UUID != other.UUID)
			return false;
		if (filesComplete != other.filesComplete)
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (totalFiles != other.totalFiles)
			return false;
		return true;
	}
	
	
}
