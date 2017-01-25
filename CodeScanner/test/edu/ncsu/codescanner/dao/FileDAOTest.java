package edu.ncsu.codescanner.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.LineNumberReader;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import dao.FileDAO;
import beans.FileBean;

public class FileDAOTest {
	
	private FileDAO dao;
	private JdbcTemplate jdbcTemplate;
	private final String sqlScriptPath = "../../../sql/generateTestData.sql";
	
	@Before
	// Annotation not working for some reason. The tests will pass when I get this fixed.
	// Essentially, this runs the SQL script specified in the string above.
	//@Sql({sqlScriptPath})
	public void setUp() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.apache.commons.dbcp.BasicDataSource");
		dataSource.setUrl("jdbc:mysql://localhost/codescanner");
		dataSource.setUsername("root");
		dataSource.setPassword("");
		
		this.jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, "sessions", "candidates", "files", "results", "repositories");
		this.dao = new FileDAO(jdbcTemplate);
	}
	
	@Test
	public void testAdd() {
		try {
			FileBean bean = new FileBean();
			bean.setFileName("File4.java");
			bean.setId(4);
			bean.setRepoID(1);
			bean.setSessionID(1);
			bean.setFilePath("searches/1/files/src/");
			dao.add(bean);
			
			final String query = "select * from files where UUID = ?";
			FileBean test = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(FileBean.class), 1);
			
			assertTrue(bean.getFileName().equals(test.getFileName()));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGet() {
		try {
			FileBean bean = new FileBean();
			bean.setFileName("File1.java");
			bean.setId(2);
			bean.setRepoID(1);
			bean.setSessionID(1);
			bean.setFilePath("searches/1/files/src/");
			dao.add(bean);
			
			FileBean test = dao.get(2);
			System.out.println(test.getFileName());
			assertTrue(bean.getFileName().equals(test.getFileName()));
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testUpdate() {
		try {
			FileBean bean = new FileBean();
			bean.setFileName("UpdateFile.java");
			bean.setId(3);
			bean.setRepoID(1);
			bean.setSessionID(1);
			bean.setFilePath("/testpath");
			dao.add(bean);
			
			bean.setFileName("DifferentName.java");
			dao.update(bean);
			
			FileBean test = dao.get(3);
			assertTrue(bean.getFileName().equals(test.getFileName()));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
