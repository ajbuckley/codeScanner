package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import beans.ResultBean;
import beans.SessionBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Properties;

import dao.CandidateDAO;
import dao.FileDAO;
//import dao.RepositoryDAO;
import dao.ResultDAO;
import dao.SessionDAO;
import response.ResultsResponse;
import response.StartResponse;
import response.UpdateResponse;
import response.SnippetResponse;



/**
 * Controller for the REST API hosted on the server. Responsible
 * for responding to page requests and populating page with
 * appropriate data.
 * 
 * I have no idea whether we need the EnableAutoConfiguration annotation.
 * 
 * @author Drew, Will, Ethan
 */
@Controller
@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class CodeScannerRestApi {
	
	public static CandidateDAO candidateDAO;
	public static FileDAO fileDAO;
	//public static RepositoryDAO repositoryDAO;
	public static ResultDAO resultDAO;
	public static SessionDAO sessionDAO;
	public static long waitTime;
	public static String authString;
	
	//private static int nextSessionID = 0;
	private static ExecutorService executor;
	
	public static String programPath;
	
	
	
	/**
	 * Initializes the search when accessed with a path string for the uploaded file.
	 * 
	 * @param archivePath URL provided by the user pointing to their source repo.
	 * 
	 */
	@PostMapping("/api/start/submitRepoUrl")
	@ResponseBody
	@CrossOrigin
	public StartResponse startSearchUrl(@RequestParam(value="searchUrl") String searchUrl) { 
		//Set up and run the search
		//TODO Use the session DB
		
		//Initialize a session.
		int id = -1;
		SessionBean b = new SessionBean();
		String startTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date(System.currentTimeMillis()));
		b.setStartTime(startTime);
		b.setFilesComplete(0); 
		b.setTotalFiles(0);//For now. The repo downloader will set this to its real value.
		try{
			sessionDAO.add(b);
			id = sessionDAO.getByStartTime(startTime).getUUID();
			System.out.println("ID: " + id);
		}catch(SQLException e){
			return new StartResponse(-1, false, e.getMessage());
		}
		
		
		SessionHandler handler = new SessionHandler(executor, searchUrl, id, false);
		Thread handlerThread = new Thread(handler, "handlerThread" + id); //The string is the thread's name for if it crashes
		handlerThread.start();
		
		
		
		//Then, return. Things have been kicked off on the server, and SessionHandler has taken the wheel. Now we redirect them to the results page, and just pray.
		return new StartResponse(id, true, "None"); //the default "success"
	}
	
	@GetMapping("/api/results/getResults")
	@ResponseBody
	@CrossOrigin
	public ResultsResponse getResult(@RequestParam(value="sessionID") int sessionID){
		
		ArrayList<ResultBean> results = this.resultDAO.getBySession(sessionID); //Get out results for this session
		ResultsResponse ret = new ResultsResponse(true, results.size(), results); //Load into the response
		
		return ret;
		
	}
	
	@PutMapping("/api/results/updateResults")
	@ResponseBody
	@CrossOrigin
	public UpdateResponse updateResponse(@RequestParam(value="resultID") int resultID){
		try{
			ResultBean u = resultDAO.get(resultID);
			//u.setUUID(resultID); //FIXME This value SHOULD come out of the DB itself but this is a dirty workaround
			u.setIsOkay(!(u.getIsOkay())); //toggle its flag
			resultDAO.update(u);
		}catch(SQLException e){
			return new UpdateResponse(false, e.getMessage());
		}
		return new UpdateResponse(true, "None");
	}
	
	@GetMapping("/api/results/getSnippet")
	@ResponseBody
	@CrossOrigin
	public SnippetResponse getSnippet(@RequestParam(value="resultID") int resultID){
		try{
			ResultBean r = resultDAO.get(resultID);
			//System.out.println(r == null);
			//System.out.println(r.getSourceFileID());
			String srcSnippet = "";
			String candSnippet = "";
			//Open up the source file
			Scanner src = new Scanner(new File(fileDAO.get(r.getSourceFileID()).getFilePath()));
			Scanner cand = new Scanner(new File(candidateDAO.get(r.getCandidateID()).getFilePath()));
			
			
			//The following loop to the right place in the file, then add the the lines we want to the snippet strings so that they can be returned.
			//In theory, the hasNextLine condition should never be meaningful unless someone has been messing with the underlying filesystem manually.
			
			for(int i = 0; i < r.getSourceStartLine() && src.hasNextLine(); i++){
				src.nextLine();
			}
			
			for(int i = 0; i < r.getLength() && src.hasNextLine(); i++){
				srcSnippet += src.nextLine();
				srcSnippet += "&#13;&#10;"; //I think this is an html line break?
			}
			
			for(int i = 0; i < r.getCandidateStartLine() && cand.hasNextLine(); i++){
				cand.nextLine();
			}
			
			for(int i = 0; i < r.getLength()  && cand.hasNextLine(); i++){
				candSnippet += cand.nextLine();
				candSnippet += "&#13;&#10;"; //I think this is an html line break?
			}
			
			return new SnippetResponse(true, "None", srcSnippet, candSnippet);
			
		}catch(SQLException | FileNotFoundException e){
			return new SnippetResponse(false, e.getMessage(),"", "");
		}
		
		
	}
	
	
	
	public static void main(String[] args) throws Exception{
		
		Properties prop = new Properties();
		if(args.length == 0){
			InputStream input = new FileInputStream("config.properties"); // the default props file
			prop.load(input);
		}else{
			InputStream input = new FileInputStream(args[0]);
			prop.load(input);
		}
		authString = prop.getProperty("auth_string");
		
		//TODO - Instantiate DataSource
		DriverManagerDataSource ds = new DriverManagerDataSource();
		//ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setDriverClassName("org.apache.commons.dbcp.BasicDataSource");
		ds.setUrl("jdbc:mysql://" + prop.getProperty("db_host") + "/codescanner"); //TODO Ask Will, this is the localhost//whatever where MySQL lives
		ds.setUsername(prop.getProperty("db_user"));
		//if(prop.getProperty("db_pass").equals("null")){
			//ds.setPassword(null);
		//}else{
			ds.setPassword(prop.getProperty("db_pass"));
		//}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		
		candidateDAO = new CandidateDAO(jdbcTemplate);
		fileDAO = new FileDAO(jdbcTemplate);
		//repositoryDAO = new RepositoryDAO(jdbcTemplate);
		resultDAO = new ResultDAO(jdbcTemplate);
		sessionDAO = new SessionDAO(jdbcTemplate);
		
		int numThreads = Integer.parseInt(prop.getProperty("num_threads")); //FIXME this shouldn't be hardcoded, it's supposed to be an argument, or config file or something
		
		if(numThreads < 1){
			System.out.println("Please enter a valid number of worker threads in the config file.");
			System.exit(-1);
		}
		
		executor = Executors.newFixedThreadPool(numThreads);
		
		
		//This calculation is a calculation of how many milliseconds each thread should wait after completing a search on a file.
		//This is to pace ourselves and make sure we do not hit the rate limit.
		waitTime = ((60 / (30 / (numThreads)))* 1000) + 1000;
		System.out.println("Wait time per file: " + waitTime);
		
		programPath = prop.getProperty("download_path"); //FIXME this shouldn't be hardcoded
		
		
		SpringApplication.run(CodeScannerRestApi.class, args);
	}
	
	
	
	
}
