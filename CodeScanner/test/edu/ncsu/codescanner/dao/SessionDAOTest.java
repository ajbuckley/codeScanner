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

import beans.SessionBean;
import dao.SessionDAO;

public class SessionDAOTest {

		
		private static SessionDAO dao;
		private static JdbcTemplate jdbcTemplate;
		private static final String sqlScriptPath = "generateTestData.sql";
		
		
		//@Sql({"generateTestData.sql"})
		@BeforeClass
		public static void setUp() {
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
			dao = new SessionDAO(jdbcTemplate);
		}
		
		@Test
		public void testAdd() {
			try {
				SessionBean bean = new SessionBean();
				bean.setFilesComplete(0);
				bean.setStartTime("today");
				bean.setTotalFiles(0);
				bean.setUUID(1);
				dao.add(bean);
				
				//final String query = "select * from sessions where UUID = ?";
				//SessionBean test = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(SessionBean.class), 1);
				SessionBean test = dao.get(1);
				System.out.println(test.getFilesComplete());
				System.out.println(test.getStartTime());
				System.out.println(test.getTotalFiles());
				System.out.println(test.getUUID());
				assertTrue(bean.equals(test));
			} catch (Exception e) {
				fail(e.getMessage());
			}
		}
		
		@Test
		public void testGet() {
			try {
				SessionBean bean = new SessionBean();
				bean.setFilesComplete(0);
				bean.setStartTime("today");
				bean.setTotalFiles(0);
				bean.setUUID(2);
				
				dao.add(bean);
				
				SessionBean test = dao.get(2);
				assertTrue(bean.equals(test));
			} catch (Exception e) {
				fail(e.getMessage());
			}
		}
		
		@Test
		public void testUpdate() {
			try {
				SessionBean bean = new SessionBean();
				bean.setFilesComplete(0);
				bean.setStartTime("today");
				bean.setTotalFiles(0);
				bean.setUUID(3);
				dao.add(bean);
				
				bean.setTotalFiles(3);
				dao.update(bean);
				
				SessionBean test = dao.get(3);
				assertTrue(bean.equals(test));
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		}

}
