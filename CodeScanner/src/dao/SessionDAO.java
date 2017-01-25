package dao;

import java.sql.SQLException;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import beans.ResultBean;
import beans.SessionBean;

public class SessionDAO {
	private JdbcTemplate jdbcTemplate;

	public SessionDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Gets the ResultBean
	 * 
	 * @param resultID
	 * @return bean: the ResultBean at the given id
	 * @throws SQLException
	 */
	public SessionBean get(int sessionID) throws SQLException {

		final String query = "SELECT * FROM sessions WHERE UUID = ?";
		return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(SessionBean.class), sessionID);

	}
	
	public SessionBean getByStartTime(String startTime) throws SQLException{
		final String query = "SELECT * FROM sessions WHERE startTime = ?";
		return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(SessionBean.class), startTime);

	}
	
	public void update(SessionBean input) throws SQLException {
			
			final String query = "UPDATE sessions SET startTime=?,totalFiles=?,filesComplete=? WHERE UUID = ?";
			jdbcTemplate.update(query, input.getStartTime(),input.getTotalFiles(),input.getFilesComplete(),input.getUUID());
	
	}
	
	public void add(SessionBean input) throws SQLException {
		
		final String query = "INSERT INTO sessions SET startTime=?,totalFiles=?,filesComplete=?";
		jdbcTemplate.update(query, input.getStartTime(),input.getTotalFiles(),input.getFilesComplete());
		
	}
}
