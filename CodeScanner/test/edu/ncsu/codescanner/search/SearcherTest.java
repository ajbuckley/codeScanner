package edu.ncsu.codescanner.search;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import beans.FileBean;
import beans.ResultBean;
import search.Searcher;
import application.CodeScannerRestApi;
import dao.CandidateDAO;
import dao.FileDAO;
import dao.ResultDAO;
import dao.SessionDAO;

public class SearcherTest {
	
	private static FileDAO dao;
	private static JdbcTemplate jdbcTemplate;
	private static final String sqlScriptPath = "clearDB.sql";
	private ExecutorService executor;
	
	
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
		CodeScannerRestApi.candidateDAO = new CandidateDAO(jdbcTemplate);
		CodeScannerRestApi.fileDAO = new FileDAO(jdbcTemplate);
		CodeScannerRestApi.resultDAO = new ResultDAO(jdbcTemplate);
		CodeScannerRestApi.sessionDAO = new SessionDAO(jdbcTemplate);
		CodeScannerRestApi.authString = "codescannerauth:team_13_2016";
		CodeScannerRestApi.programPath = "C:\\scannerfilesystem";
		CodeScannerRestApi.waitTime = 9000;
		ExecutorService executor = Executors.newFixedThreadPool(1);
	}
	
	@Test
	public void testSearcher(){
		File f = new File("C:\\codescannertest\\");
		if(!f.exists()){
			f.mkdir();
		}
		try {
			f = new File("C:\\codescannertest\\drew.txt");
			f.createNewFile();
			FileOutputStream fo = new FileOutputStream(f);
			fo.write("hello my name is Drew".getBytes());
			
		
		
		
		
			FileBean bean = new FileBean();
			bean.setFileName("drew.txt");
			bean.setId(1);
			bean.setRepoID(1);
			bean.setSessionID(1);
			bean.setFilePath("C:\\codescannertest\\drew.txt");
			try {
				CodeScannerRestApi.fileDAO.add(bean);
			} catch (SQLException e) {
				fail(e.getMessage());
				e.printStackTrace();
			}
			
			Searcher s = new Searcher(bean);
			s.run();
			
			ArrayList<ResultBean> results = CodeScannerRestApi.resultDAO.getByFile(1);
			assertTrue(results.get(0) != null);
			
		} catch (IOException | SQLException e1) {
			e1.printStackTrace();
			fail(e1.getMessage());

		}
		
	}
}
