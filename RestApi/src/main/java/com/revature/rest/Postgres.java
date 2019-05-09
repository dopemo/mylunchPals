package com.revature.rest;
import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;




public class Postgres {

	
	private static final String CONNECTION_USERNAME = "epysrdhz";
	private static final String CONNECTION_PASSWORD = "1PWcU3hxITnBFDwkWrK2p8VDq2GPPnqa";
	private static final String URL = "jdbc:postgresql://hansken.db.elephantsql.com:5432/epysrdhz";
	
	
	
	private static Connection connection;
	private static PlayerImp playerImp;
	public static synchronized PlayerImp getPlayerImp(){

		if (playerImp == null) {
			playerImp = new PlayerImp();
		}
		return playerImp;
	}
	

	
	static synchronized Connection getConnection() throws SQLException {
		if (connection == null) {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				System.out.println("Could not register driver!");
				e.printStackTrace();
			}
			connection = DriverManager.getConnection(URL, CONNECTION_USERNAME, CONNECTION_PASSWORD);
		}
		
		//If connection was closed then retrieve a new connection
		if (connection.isClosed()){
			
			connection = DriverManager.getConnection(URL, CONNECTION_USERNAME, CONNECTION_PASSWORD);
		}
		return connection;
	}
}
