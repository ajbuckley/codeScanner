package edu.ncsu.codescanner.dao;



import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;

import dao.CandidateDAO;
import beans.CandidateBean;

public class CandidateDAOTest {
	
	private static CandidateDAO dao;
	private static JdbcTemplate jdbcTemplate;
	private static final String sqlScriptPath = "clearDB.sql";
	
	@BeforeClass
	// Annotation not working for some reason. The tests will pass when I get this fixed.
	// Essentially, this runs the SQL script specified in the string above.
	//@Sql({sqlScriptPath})
	public static void setUp() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.apache.commons.dbcp.BasicDataSource");
		dataSource.setUrl("jdbc:mysql://localhost/codescanner");
		dataSource.setUsername("root");
		dataSource.setPassword("");
		jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		Resource resource = new ClassPathResource(sqlScriptPath);
		try {
			ScriptUtils.executeSqlScript(dataSource.getConnection(), resource);
		} catch (ScriptException e) {
			fail(e.getMessage());
		} catch (SQLException e) {
			fail(e.getMessage());
		}
		dao = new CandidateDAO(jdbcTemplate);
	}
	
	@Test
	public void testAdd() {
		try {
			CandidateBean bean = new CandidateBean();
			bean.setFileName("File4.java");
			bean.setId(1);
			bean.setRepoURL("www.hello.com");
			bean.setSessionID(1);
			bean.setFilePath("searches/1/files/src/");
			bean.setLicense("lisence");
			bean.setAssociatedFile(1);
			
			dao.add(bean);
			
			//final String query = "select * from files where UUID = ?";
			CandidateBean test = dao.get(1);
			
			assertTrue(bean.getFileName().equals(test.getFileName()));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGet() {
		try {
			CandidateBean bean = new CandidateBean();
			bean.setFileName("File1.java");
			bean.setId(2);
			bean.setRepoURL("www.hello.com");
			bean.setSessionID(1);
			bean.setFilePath("searches/1/files/src/");
			bean.setLicense("lisence");
			dao.add(bean);
			CandidateBean test = dao.get(2);
			assertTrue(bean.getFileName().equals(test.getFileName()));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
			
	}
	
	@Test
	public void testUpdate() {
		try {
			CandidateBean bean = new CandidateBean();
			bean.setFileName("UpdateFile.java");
			bean.setId(3);
			bean.setRepoURL("www.hello.com");
			bean.setSessionID(1);
			bean.setFilePath("/testpath");
			bean.setLicense("lisence");
			dao.add(bean);
			
			bean.setFileName("DifferentName.java");
			dao.update(bean);
			
			
			CandidateBean test = dao.get(3);
			
			assertTrue(bean.getFileName().equals(test.getFileName()));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}