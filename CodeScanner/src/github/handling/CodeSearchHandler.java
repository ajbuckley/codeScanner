/**
 * 
 */
package github.handling;

import java.util.ArrayList;
import java.util.Base64; //Java 8 only
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

import dao.CandidateDAO;
import application.CodeScannerRestApi;

/**
 * @author Drew, Ethan Swartzentruber
 *
 * Class that handles the code searching for github.
 * 
 */
public class CodeSearchHandler {

	private static final String USER_AGENT = "Mozilla/5.0";
	private static String auth = "codescannerauth:team_13_2016";
	FilePuller fp;
	public CodeSearchHandler(){
		this.auth = CodeScannerRestApi.authString;
		
	}

	/**
	//Just a main method that I used for some testing in early development, feel free to disregard
	public static void main(String[] args){
		ArrayList<String> k = new ArrayList<String>();

		k.add("dirChange");
		k.add("draw");
		k.add("setup");

		CodeSearchHandler cs = new CodeSearchHandler();

		try{
			cs.startSearch(k, 0, 0);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	*/


	/**
	 * Runs the github code search api adds results to the database. Downloads those files.
	 * 
	 * @param keywords
	 * @throws IOException 
	 */
	public void startSearch(ArrayList<String> keywords, int fileID, int sessionID) throws IOException {
		
		fp = new FilePuller(fileID, sessionID, CodeScannerRestApi.programPath);
		
		//reencode auth string into base 64
		//This isn't how this should be done but I don't know how to declare it in base 64
		byte[] authEncBytes = Base64.getEncoder().encode(auth.getBytes());
		String auth = new String(authEncBytes);
		
		//Form URL by taking base api URL and adding keywords produced by algorithm.
		String u = "https://api.github.com/search/code?q=";
		//FIXME The keywords also need to be encoded into percent encoding.
		if(keywords.size() > 0){
			u = u + keywords.get(0);
		}
		for(int i = 1; i < keywords.size(); i++){
			u = u + "+" + keywords.get(i);
		}
		URL url = new URL(u); // finish creating our URL.
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header fields
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Authorization", "Basic " + auth);
		BufferedReader in = null;
		boolean flag = false;
		while(!flag){
			int responseCode = con.getResponseCode();
			if(responseCode == 200){
				System.out.println("Received response code " + responseCode + " from GitHub API.");
				//System.out.println("\nSending 'GET' request to URL : " + url);
				//System.out.println("Response Code : " + responseCode);
		
				
				//I'm not extremely sure whether newlines as java defines them will play nicely with
				//Whatever I get in JSON.
				//So, I just read everything into a response string and go through it myself.
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				flag = true;
			}else if(responseCode == 403){ //If we have hit the rate limit and are waiting for more
				try{
					System.out.println("Retrying for file " + fileID);
					Thread.sleep(60000); //sleep for 60 seconds and try again
					//In theory, this should never happen, because of the way we pace ourselves from the get-go.
					
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}else{
				System.out.println("ERROR CONNECTING TO GITHUB API. PLEASE RESTART SERVER AND CHECK INTERNET CONNECTION.");
			}
		}
		
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();

		String response = responseBuffer.toString();

		//System.out.println("---RESPONSE---");
		//System.out.println(response);
		//System.out.println("---END_RESPONSE---");
		
		//Decided to just parse through the JSON as a string. Might come back to haunt me later,
		//but what I'm doing right now isn't difficult at all, so here we go.
		//I parse out the top 3 download paths returned by the search.
		
		
		int paths = 0;
		int fromIdx = 0; //used to skip paths we have already found
		String dl;
		String p;
		String r;
		
		//TODO Reconcile with Drew's new syntax
		FilePuller fp = new FilePuller(fileID, sessionID, CodeScannerRestApi.programPath);
		
		
		//8 and 13 here are the lengths of the string property names so that I can skip over them and read the property value.
		while(response.contains("\"path\":") && paths < 3){
			dl = "https://raw.githubusercontent.com/";
			
			fromIdx = response.indexOf("\"path\":") + 8;
			//System.out.println(fromIdx);
			p = response.substring(fromIdx, response.indexOf("\"", fromIdx)); //read everything until the next quote
			p = URLEncoder.encode(p, "UTF-8");
			p = p.replace("+", "%20");
			fromIdx = response.indexOf("\"full_name\":") + 13;
			r = response.substring(fromIdx, response.indexOf("\"", fromIdx)); //read everything until the next quote
			r = URLEncoder.encode(r, "UTF-8");
			r = r.replace("+", "%20");
			
			if(p.substring(0, 1).equals("/")){
				dl = dl + r +"/master" + p;
			}else{
				dl = dl + r +"/master/" + p;
			}
			 
			//TODO Is this correct format for Drew?
			System.out.println("Candidate found for file " + fileID + ". Download URL: "+ dl);
			
			try{
				fp.download(dl);
			}catch(IOException e){
				System.out.println("Failed to download a candidate of file " + fileID);
			}
			
			response = response.substring(fromIdx); //cut off the part we have already done
			//System.out.println("URL " + paths + ":");
			//System.out.println(dl);
			//System.out.println();
			paths++;
		}
		
		
		
		//print result
		//System.out.println(response.toString());
		
	}

}

