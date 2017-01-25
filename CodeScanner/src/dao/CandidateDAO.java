/**
 * 
 */
package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import beans.CandidateBean;
import java.math.BigInteger;

/**
 * @author Drew Buckley, Will Hammond
 * 
 * Database handler for candidate files
 *
 */
public class CandidateDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public CandidateDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * @throws SQLException 
	 * Fetches a single candidate which matches a specified UUID.
	 * 
	 * @param id The UUID of the desired candidates
	 * @return A candidate bean matching the specified UUID
	 */
	public CandidateBean get(int id) throws SQLException{
		
		final String query = "select * from candidates where UUID = ?";
		return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(CandidateBean.class), id);
		
	}
	
	/**
	 * @throws SQLException 
	 * Fetches all candidates which match the specified session ID.
	 * 
	 * @param id The session ID of the desired candidates  
	 * @return cand The Candidate Bean
	 * 
	 */
	public ArrayList<CandidateBean> getBySession(int id) throws SQLException{
		
		final String query = "SELECT * FROM candidates WHERE sessionID = ?";
		final ArrayList<CandidateBean> candidates = new ArrayList<CandidateBean>();
		final List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, id);
		
		// Iterate through retrieved rows and populate beans with data
		for (Map<String, Object> row: rows) {
			CandidateBean candidate = new CandidateBean(((BigInteger)row.get("UUID")).intValue());
			candidate.setFileName((String)row.get("fileName"));
			candidate.setSessionID(((Long)row.get("sessionID")).intValue());
			candidate.setLicense((String)row.get("license"));
			candidate.setRepoURL((String)row.get("repoURL"));
			candidate.setFilePath((String)row.get("filePath"));
			candidate.setAssociatedFile(((Long)row.get("associatedFile")).intValue());
			candidates.add(candidate);
		}
		
		return candidates;
	}
	
	/**
	 * Updates the given CandidateBean at the id of the input CandidateBean
	 * with the given CandidateBean.
	 * 
	 * @param input: The Updated Candidate Bean
	 * @throws SQLException 
	 */

	public void update(CandidateBean input) throws SQLException{
		
		final String query = "UPDATE candidates SET fileName=?,filePath=?,repoURL=?,sessionID=?,license=?,associatedFile=? WHERE UUID = ?";
		jdbcTemplate.update(query, input.getFileName(), input.getFilePath(), input.getRepoURL(), input.getSessionID(), input.getLicense(), input.getAssociatedFile(), input.getId());
		
	}
	
	/**
	 * Adds the given CandidateBean to the database
	 * 
	 * @param input : The CandidateBean to be added
	 * @return id : The ID of the file
	 * @throws SQLException 
	 */
	public void add(CandidateBean input) throws SQLException{
		final String query = "INSERT INTO candidates SET fileName=?,filePath=?,repoURL=?,sessionID=?,license=?,associatedFile=?";
		jdbcTemplate.update(query, input.getFileName(), input.getFilePath(), input.getRepoURL(), input.getSessionID(), input.getLicense(), input.getAssociatedFile());
		
		// TODO: Figure out how to determine ID of source file for referencing later
	}
	
	/**
	 * Returns a list of all candidates for a given file
	 * 
	 * @return list : The list of candidates
	 * @throws SQLException 
	 */
	public ArrayList<CandidateBean> getByFile(int fileID) throws SQLException {
		
		final String query = "SELECT * FROM candidates WHERE associatedFile = ?";
		final ArrayList<CandidateBean> candidates = new ArrayList<CandidateBean>();
		final List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, fileID);
		
		// Iterate through retrieved rows and populate beans with data

		for (Map<String, Object> row: rows) {
			CandidateBean candidate = new CandidateBean(((BigInteger)row.get("UUID")).intValue());
			candidate.setFileName((String)row.get("fileName"));
			candidate.setSessionID(((Long)row.get("sessionID")).intValue());
			candidate.setLicense((String)row.get("license"));
			candidate.setRepoURL((String)row.get("repoURL"));
			candidate.setFilePath((String)row.get("filePath"));
			candidate.setAssociatedFile(((Long)row.get("associatedFile")).intValue());
			candidates.add(candidate);
		}
		
		return candidates;
		
	}
	
}
