package application;

import github.handling.RepositoryPuller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import search.Searcher;
import beans.FileBean;
import dao.FileDAO;

/**
 * A runnable which handles everything to do with a single session. Does not exit until the session's
 * results are in the DB. Needed so that CodeScannerRestApi start methods can return immediately.
 * @author Ethan Swartzentruber
 *
 */
public class SessionHandler implements Runnable{

	//threadpools used for searching and matching, respectively
	private ExecutorService pool;
	
	//External github URL of repository given by the user
	private String repoURL;
	
	//sessionID for this search job.
	private int sessionID;
	
	//true is by zip upload, false is by URL
	private boolean zipUpload;
	
	public SessionHandler(ExecutorService pool, String repoURL, int sessionID, boolean zipUpload){
		this.pool = pool;
		this.repoURL = repoURL;
		this.sessionID = sessionID;
		this.zipUpload = zipUpload;
	}
	
	//TODO I might need to synchronize these on the DAO so that if it's putting results into the DAO when
	//someone wants to get something out, we don't break everything. Unsure whether this is necessary,
	//it depends on how SQL works under the hood.
	public void run() {
		
		if(zipUpload){ //if this session was started by zip upload
			//TODO Handle this. Not sure how, ask Will
			
		}else{ //if this session was started by URL request
			//First, go download the files in the repository. When these statements return, files are in the DB.
			//TODO Reconcile syntax with Drew's new stuff, not yet merged into master
			RepositoryPuller repoPuller = new RepositoryPuller(sessionID, CodeScannerRestApi.programPath);
			try {
				repoPuller.download(repoURL);
			} catch (IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		//TODO Queue all files into the searchPool. The runnable for that will queue them into the matchPool.
		FileDAO fDAO = CodeScannerRestApi.fileDAO;
		ArrayList<FileBean> fbs = null;
		
		try {
			fbs = fDAO.getBySession(sessionID);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		System.out.println("Files: " + fbs.size());
		for(FileBean f: fbs){
			//Queue the filebean into the pool to have its candidates generated and results found.
			//System.out.println("Queueing file " + f.getFileName() + " into pool.");
			pool.execute(new Searcher(f));
		}
		
	
	}

}
