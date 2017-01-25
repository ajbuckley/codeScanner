package edu.ncsu.codescanner.git;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import github.handling.FilePuller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcAccessor;

import beans.CandidateBean;
import dao.CandidateDAO;

/**
 * 
 * @author Drew
 *
 *Class for testing FilePuller
 */
public class FilePullerTest {

	private CandidateDAO dao;
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
		this.dao = new CandidateDAO(jdbcTemplate);
	}
	
	@Test
	public void testDownload(){
		FilePuller fp = new FilePuller(100, 100, "C:\\");
		fp.setDAO(this.dao);
		try {
			fp.download("https://raw.githubusercontent.com/ajbuckley/gameEngine/master/GameEngine/src/gameObject/Location.java");
		
			CandidateBean test = dao.getByFile(100).get(0);
			assertTrue(test.getFileName().equalsIgnoreCase("Location.java"));
			File f = new File("" + test.getFilePath());
			FileInputStream fip = new FileInputStream(f);
			Scanner scan = new Scanner(fip);
			assertTrue(scan.next().equals("/**"));
		
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
