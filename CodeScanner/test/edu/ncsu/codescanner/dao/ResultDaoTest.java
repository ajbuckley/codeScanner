/**
 * 
 */
package edu.ncsu.codescanner.dao;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;

import beans.ResultBean;
import dao.FileDAO;
import dao.ResultDAO;

/**
 * @author Drew
 *
 */
public class ResultDaoTest {

	private static ResultDAO dao;
	private static JdbcTemplate jdbcTemplate;
	private static final String sqlScriptPath = "clearDB.sql";
	
	@BeforeClass
	//@Sql({sqlScriptPath})
	public static void setup() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.apache.commons.dbcp.BasicDataSource");
		dataSource.setUrl("jdbc:mysql://localhost/codescanner");
		dataSource.setUsername("root");
		dataSource.setPassword("");
		jdbcTemplate = new JdbcTemplate();
		Resource resource = new ClassPathResource(sqlScriptPath);
		try {
			ScriptUtils.executeSqlScript(dataSource.getConnection(), resource);
		} catch (ScriptException e) {
			fail(e.getMessage());
		} catch (SQLException e) {
			fail(e.getMessage());
		}
		jdbcTemplate.setDataSource(dataSource);
		dao = new ResultDAO(jdbcTemplate);
	}
	
	@Test
	public void testAdd() {
		try {
			ResultBean bean = new ResultBean(1,2, 3, 4, 5, 6, 7, false);
			bean.setUUID(1);
			bean.setCandidateFileName("candidate");
			bean.setCandidateUrl("url");
			bean.setSourceFileName("source");
		
			dao.add(bean);
			
			//final String query = "select * from files where UUID = ?";
			//ResultBean test = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(ResultBean.class), 1);
			
			ResultBean test = dao.get(1);
			
			assertTrue(bean.getCandidateUrl().equals(test.getCandidateUrl()));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGet() {
		try {
			ResultBean bean = new ResultBean(1,2, 3, 4, 5, 6, 7, false);
			bean.setUUID(1);
			bean.setCandidateFileName("candidate");
			bean.setCandidateUrl("url");
			bean.setSourceFileName("source");
			dao.add(bean);
			ResultBean test = dao.get(1);
			assertTrue(bean.getCandidateUrl().equals(test.getCandidateUrl()));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
			
	}
	
	@Test
	public void testUpdate() {
		try {
			ResultBean bean = new ResultBean(1,2, 3, 4, 5, 6, 7, false);
			bean.setUUID(1);
			bean.setCandidateFileName("candidate");
			bean.setCandidateUrl("url");
			bean.setSourceFileName("source");
			
			
			dao.add(bean);
			
			bean.setIsOkay(true);
			dao.update(bean);
		
			ResultBean test = dao.get(1);
			
			assertTrue(bean.getCandidateUrl().equals(test.getCandidateUrl()));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	

}
