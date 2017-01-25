/**
 * 
 */
package github.handling;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import dao.FileDAO;
import application.CodeScannerRestApi;
import beans.FileBean;

/**
 * @author Drew Buckley
 * 
 * Class that handles pulling a repository from github in the form of a zip.
 * Passes the repository to the zip handler that puts the repository in the database. 
 *
 */
public class RepositoryPuller {
	private FileDAO fd;
	private int searchSession;
	private String programPath;
	public RepositoryPuller(int sessionID, String programPath){
		this.searchSession = sessionID; 
		this.programPath = programPath;
		fd = CodeScannerRestApi.fileDAO;
	}
	
	/**
	 * Downloads and unzips the Repo from the given github URL.
	 * 
	 * @param url
	 * @throws IOException
	 * @throws SQLException
	 */
	public void download(String url) throws IOException, SQLException{
		
		System.out.println(url);
		//Create the download URL
		String downloadURL = "https://github.com/";
		String trimURL = url.substring(url.indexOf(".com/") + 5);
		if(trimURL.indexOf("/", trimURL.indexOf("/") + 1) != -1){
			trimURL = trimURL.substring(0, trimURL.indexOf("/", trimURL.indexOf("/") + 1));
		}
		downloadURL = downloadURL.concat(trimURL + "/archive/master.zip");
		
		System.out.println(downloadURL);//TODO rm 
		
		//Create inputStream
		 URL repoURL = new URL(downloadURL);
	     HttpURLConnection httpConn = (HttpURLConnection) repoURL.openConnection();
	     int responseCode = httpConn.getResponseCode();
	     String zipName = "master.zip";
	     
	     
	     if (responseCode == HttpURLConnection.HTTP_OK) {
	    	 String disposition = httpConn.getHeaderField("Content-Disposition");
	    	 if (disposition != null) {
	                // extracts file name from header field
	                int index = disposition.indexOf("filename=");
	                if (index > 0) {
	                    zipName = disposition.substring(index + 9,
	                            disposition.length() );
	                    System.out.println(zipName);
	                }
	                
	    	 }
	    	 
	    
	    	 
	    	 String saveFilePath = programPath + "\\searches\\" + searchSession + "\\download\\";
	    	 //Create path if it doesn't exist
	    	 File folder = new File(saveFilePath);
	     	 if(!folder.exists()){
	     		folder.mkdirs();
	     	 }
	    	 saveFilePath = saveFilePath.concat(zipName);
	     	 
	    	 InputStream inputStream = httpConn.getInputStream();
	    	 FileOutputStream outputStream = new FileOutputStream(saveFilePath);
	    	 
	    	 int bytes;
             byte[] buffer = new byte[4096];
             while ((bytes = inputStream.read(buffer)) > 0) {
                 outputStream.write(buffer, 0, bytes);
             }
 
             outputStream.close();
             inputStream.close();
	    	 
	    	 
	    	 ZipInputStream zi = new ZipInputStream(new FileInputStream(saveFilePath)); 
	    	 
	    	 zipName=  zipName.replaceAll(".", "");
	    	 unzip(zi, zipName);
	    	 
	    	 System.out.println("Finished downloading.");
	    	 
	     }else{
	    	 System.out.println("HTTP FAILURE ON REPO DOWNLOAD.");
	     }
	}
	
	/**
	 * Unzips from the given zipInputStream.
	 * 
	 * @param zipIn : The ZipInputStream
	 * @param name : The Repository name
	 * @throws IOException
	 * @throws SQLException
	 */
	public void unzip(ZipInputStream zipIn, String name)throws IOException, SQLException{
		
		byte[] buffer = new byte[4096];
		
		String outputPath = programPath + "\\searches\\" + searchSession + "\\" + name +"ID="+ searchSession +"\\";
		
		
		//Create the directory if it doesn't exist
		File folder = new File(outputPath);
    	if(!folder.exists()){
    		folder.mkdirs();
    	}
		
		
		ZipEntry entry = zipIn.getNextEntry();
    	
    	
    	while(entry != null){

    	   String fileName = entry.getName();
           File newFile = new File(outputPath + "\\" + fileName);
           if (entry.isDirectory()){
        	   newFile.mkdirs();
           }
           else{

	            //Unpack the file
	            BufferedOutputStream zipOut = new BufferedOutputStream (new FileOutputStream(newFile));
	
	            int bytes;
	            
	            while ((bytes = zipIn.read(buffer)) != -1) {
	            	zipOut.write(buffer, 0, bytes);
	            }
	            //Add the fileBean to the dataBase
	            
	            FileBean f = new FileBean();
	            f.setFileName(fileName.substring(fileName.lastIndexOf("\\") + 1 ));
	            f.setFilePath(outputPath + "\\" + fileName);
	            f.setSessionID(searchSession);
	            fd.add(f);
	            zipOut.close();
           }
            entry = zipIn.getNextEntry();
    	}

        zipIn.closeEntry();
    	zipIn.close();

	}

	public void setDAO(FileDAO dao) {
		this.fd = dao;
		
	}
}
