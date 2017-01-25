package github.handling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;

import dao.CandidateDAO;
import application.CodeScannerRestApi;
import beans.CandidateBean;

/**
 * @author Drew Buckley
 * 
 * Class that handles pulling files down from gitHub.
 *
 */
public class FilePuller {

	private int sessionID;
	
	private String programPath;
	
	private int sourceFileID;
	
	private int candidateNumber = 0;
	
	CandidateDAO cd;
	
	public FilePuller(int sourceFileID, int sessionID, String programPath){
		this.sessionID = sessionID;
		this.programPath = programPath;
		this.sourceFileID = sourceFileID;
		cd = CodeScannerRestApi.candidateDAO;
		
	}
	
	/**
	 * 
	 * @param url
	 */
	public void download(String url) throws IOException{
		
		
		int fileNameStart = url.lastIndexOf('/') + 1;
		String fileName = url.substring(fileNameStart);
		
		String filePath = programPath + "\\searches\\" + sessionID + "\\candidates\\" + sourceFileID;
		
		//Create folders that aren't made
		File folder = new File(filePath);
    	if(!folder.exists()){
    		if(folder.mkdirs()){
    			//System.out.println("created");
    		}
    	}
		filePath = filePath.concat("\\" + candidateNumber++);
		/*
		int pathStart = url.indexOf(".com/") + 5;
		String rawURL = "https://raw.githubusercontent.com/" + url.substring(pathStart);
		rawURL = rawURL.replace("/blob/master", "/master");
		*/
		

			OutputStream out = new FileOutputStream(filePath);
			URL link = new URL(url);
	        URLConnection conn = link.openConnection();
	        conn.connect();
	        InputStream in = conn.getInputStream();
	        this.copy(in, out);
		
		
		
		CandidateBean cand = new CandidateBean();
		cand.setFilePath(filePath);
		cand.setRepoURL(url);
		cand.setSessionID(sessionID);
		cand.setLicense("Unknown"); //FIXME Obviously this should actually be the license

		cand.setFileName(fileName);
		cand.setAssociatedFile(sourceFileID);
		
		
		try {
			cd.add(cand);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	/**
	 * Copies the data from an inputStream to an OutputStream
	 * 
	 * @param from
	 * @param to
	 * @throws IOException
	 */
	private void copy(InputStream from, OutputStream to) throws IOException {
       
		byte[] buffer = new byte[4096];
        while (true) {
            int numBytes = from.read(buffer);
            if (numBytes == -1) {
                break;
            }
            to.write(buffer, 0, numBytes);
        }
	}

	public void setDAO(CandidateDAO dao) {
		this.cd = dao;
		
	}       
}