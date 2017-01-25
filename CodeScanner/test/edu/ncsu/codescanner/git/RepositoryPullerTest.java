package edu.ncsu.codescanner.git;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;

import github.handling.RepositoryPuller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import beans.FileBean;
import dao.CandidateDAO;
import dao.FileDAO;

public class RepositoryPullerTest {

	private FileDAO dao;
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost/codescanner");
		dataSource.setUsername("root");
		dataSource.setPassword("");
		this.jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		this.dao = new FileDAO(jdbcTemplate);
	}
	
	@Test
	public void testDownload(){
		
		RepositoryPuller rp = new RepositoryPuller(12, "C:\\");
		rp.setDAO(dao);
		
		
		try {
			rp.download("https://github.com/ajbuckley/gameEngine");
			FileBean fb = dao.getBySession(12).get(1);
			assertTrue(fb.getFileName().equalsIgnoreCase("gameEngine-master/GameEngine/.classpath"));
		
		
		} catch (IOException | SQLException e) {
		
			e.printStackTrace();
		}
		
		
		
	}
}
