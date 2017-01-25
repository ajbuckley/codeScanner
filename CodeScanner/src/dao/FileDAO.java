/**
 * 
 */
package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigInteger;
import java.sql.*;

import beans.FileBean;

/**
 * @author Drew Buckley 
 * @author Will Hammond
 * 
 * Database handler for files
 */
public class FileDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public FileDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * Used to get fileBeans from the Database.
	 * 
	 * @param fileID: The ID of the file you wish to see
	 * @return file: The file bean with the given ID
	 * 
	 * @throws SQLException
	 */
	public FileBean get(int fileID) throws SQLException {
		
		final String query = "select * from files where UUID = ?";
		return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(FileBean.class), fileID);
					
	}
	
	/**
	 * Updates the file in the database at 'file's ID with 'file'
	 * 
	 * @param file: The new file bean
	 * @throws SQLException 
	 * 
	 */
	public void update(FileBean file) throws SQLException{
		
		final String query = "UPDATE files SET sessionID=?,repoID=?,fileName=?,filePath=? WHERE UUID=?";
		jdbcTemplate.update(query, file.getSessionID(), file.getRepoID(), file.getFileName(), file.getFilePath(), file.getId());
		
	}
	
	
	/**
	 * Adds the given file bean to the database
	 * 
	 * @param file: The FileBean to be added
	 * @return id: The id of the FileBean once added
	 * @throws SQLException 
	 */
	/*This is a problem... FileBeans need to be initialized with a fileID, 
	* however they don't get an id until they are added to the DB.
	* 
	* I'm going to add some constructors to fileBean. 
	* 
	* I'm also having to query the DB again to get the UUID so I can return it.
	* There's probably a better way...
	*
	*/
	public int add(FileBean file) throws SQLException{
		
		// For the moment, this assumes the constructor described above
		final String query = "INSERT INTO files SET sessionID=?,repoID=?,fileName=?,filePath=?";
		return jdbcTemplate.update(query, file.getSessionID(), file.getRepoID(), file.getFileName(), file.getFilePath());
		
	}
	
	/**
	 * Returns all the files in a specified repo.
	 * 
	 * @param repoID: The ID of the desired repo
	 * @return beanList: The list of FileBeans in the desired repo
	 * @throws SQLException 
	 * 
	 */
	public ArrayList<FileBean> getBySession(int sessionID) throws SQLException{
		final String query = "SELECT * FROM files WHERE sessionID = ?";
		final ArrayList<FileBean> files = new ArrayList<FileBean>();
		final List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, sessionID);
		
		// Iterate through retrieved rows and populate beans with data
		for (Map<String, Object> row: rows) {
			FileBean file = new FileBean(((BigInteger) row.get("UUID")).intValue());
			file.setFileName((String) row.get("fileName"));
			file.setFilePath((String) row.get("filePath"));
			file.setRepoID(((Long) row.get("repoID")).intValue());
			file.setSessionID(((Long) row.get("sessionID")).intValue());
			files.add(file);
		}
		
		return files;
		
	}
}
