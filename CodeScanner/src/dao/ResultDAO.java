/**
 * 
 */
package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import beans.ResultBean;
import java.math.BigInteger;

/**
 * @author Drew Buckley
 * @author Will Hammond
 * 
 * Database handler for Results
 *
 */
public class ResultDAO {
	
	private JdbcTemplate jdbcTemplate;

	public ResultDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Gets the ResultBean
	 * 
	 * @param resultID
	 * @return bean: the ResultBean at the given id
	 * @throws SQLException
	 */
	public ResultBean get(int resultID) throws SQLException {

		final String query = "SELECT * FROM results WHERE UUID = ?";
		return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(ResultBean.class), resultID);

	}

	/**
	 * Returns all the results from a specific file.
	 * 
	 * @param fileID
	 *            : The ID of the desired file
	 * @return beanList: The list of results
	 * @throws SQLException
	 * 
	 */
	public ArrayList<ResultBean> getByFile(int fileID) throws SQLException {
		
		final String query = "SELECT * FROM results WHERE fileID = ?";
		final ArrayList<ResultBean> results = new ArrayList<ResultBean>();
		final List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, fileID);
		
		// Iterate through retrieved rows and populate beans with data
		for (Map<String, Object> row: rows) {
			ResultBean result = new ResultBean(((BigInteger)row.get("UUID")).intValue());
			result.setMatchRating((float)row.get("matchRating"));
			result.setSessionID(((Long)row.get("sessionID")).intValue());
			result.setSourceFileID(((Long)row.get("sourceFileID")).intValue());
			result.setSourceStartLine((Integer)row.get("sourceStartLine"));
			result.setLength((Integer)row.get("length"));
			result.setCandidateStartLine((Integer)row.get("candidateStartLine"));
			result.setCandidateID(((Long)row.get("candidateID")).intValue());
			result.setIsOkay((boolean)row.get("isOkay"));
			result.setSourceFileName((String)row.get("sourceFileName"));
			result.setCandidateFileName((String)row.get("candidateFileName"));
			result.setCandidateUrl((String)row.get("candidateUrl"));
			results.add(result);
		}
		
		return results;
		
	}

	/**
	 * Returns all the results from a specific repo.
	 * 
	 * @param repoID
	 *            : The ID of the desired repo
	 * @return beanList: The list of results in the desired repo
	 * @throws SQLException
	 */
	public ArrayList<ResultBean> getBySession(int sessionID) {

		final String query = "SELECT * FROM results WHERE sessionID = ?";
		final ArrayList<ResultBean> results = new ArrayList<ResultBean>();
		final List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, sessionID);
		
		// Iterate through retrieved rows and populate beans with data
		for (Map<String, Object> row: rows) {
			ResultBean result = new ResultBean(((BigInteger)row.get("UUID")).intValue());
			result.setMatchRating((float)row.get("matchRating")); //FIXME Fix this garbage
			result.setSessionID(((Long)row.get("sessionID")).intValue());
			result.setSourceFileID(((Long)row.get("sourceFileID")).intValue());
			result.setSourceStartLine((Integer)row.get("sourceStartLine"));
			result.setLength((Integer)row.get("length"));
			result.setCandidateStartLine((Integer)row.get("candidateStartLine"));
			result.setCandidateID(((Long)row.get("candidateID")).intValue());
			result.setIsOkay((boolean)row.get("isOkay"));
			result.setSourceFileName((String)row.get("sourceFileName"));
			result.setCandidateFileName((String)row.get("candidateFileName"));
			result.setCandidateUrl((String)row.get("candidateUrl"));
			results.add(result);
		}
		
		return results;

	}

	/**
	 * Updates the ResultBean at the input ResultBean's id with the input
	 * ResultBean
	 * 
	 * @param input
	 *            : The updated bean
	 * @throws SQLException 
	 *
	 */
	public void update(ResultBean input) throws SQLException {
		
		final String query = "UPDATE results SET sessionID=?,sourceFileID=?,candidateID=?," +
				"matchRating=?,sourceStartLine=?,candidateStartLine=?,length=?,isOkay=?,sourceFileName=?,candidateFileName=?,candidateUrl=? WHERE UUID = ?";
		jdbcTemplate.update(query, input.getSessionID(), input.getSourceFileID(), input.getCandidateID(),
				input.getMatchRating(), input.getSourceStartLine(), input.getCandidateStartLine(),
				input.getLength(), input.getIsOkay(),input.getSourceFileName(), input.getCandidateFileName(), input.getCandidateUrl(), input.getUUID());

	}

	/**
	 * Adds the given ResultBean to the database
	 * 
	 * @param input
	 *            : The ResultBean to be added
	 * @throws SQLException 
	 */
	public void add(ResultBean input) throws SQLException {
		
		/*
		System.out.println(input.getSessionID());
		System.out.println(input.getSourceFileID());
		System.out.println(input.getCandidateID());
		System.out.println(input.getMatchRating()); 
		System.out.println(input.getSourceStartLine());
		System.out.println(input.getCandidateStartLine());
		System.out.println(input.getLength());
		System.out.println(input.getIsOkay());
		*/
		
		final String query = "INSERT INTO results SET sessionID=?,sourceFileID=?,candidateID=?," +
				"matchRating=?,sourceStartLine=?,candidateStartLine=?,length=?,isOkay=?,sourceFileName=?,candidateFileName=?,candidateUrl=?";
		jdbcTemplate.update(query, input.getSessionID(), input.getSourceFileID(), input.getCandidateID(),
				input.getMatchRating(), input.getSourceStartLine(), input.getCandidateStartLine(),
				input.getLength(), input.getIsOkay(), input.getSourceFileName(), input.getCandidateFileName(), input.getCandidateUrl());
		
	}

}
