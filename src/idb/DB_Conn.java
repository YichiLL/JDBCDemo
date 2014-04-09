package idb;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import oracle.jdbc.driver.*;



public class DB_Conn {
	private String database="";
	private String username="tx2135";
	private String password="Coms4111";
	
	public Connection getConnection() throws SQLException,
	ClassNotFoundException
	{
		//Load the Oracle JDBC Driver
		DriverManager.registerDriver
				(new oracle.jdbc.driver.OracleDriver());
		
		//Connect through driver
		Connection conn=DriverManager.getConnection
				("jdbc:oracle:thin:tx2135/Coms4111@//w4111b.cs.columbia.edu:1521/ADB");
		return conn;		
	}
	
	public String getDatabase()
	{
		return database;
	}
	public void setDatabase(String database)
	{
		this.database=database;
	}
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username=username;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password=password;
	}
	

}
