package com.multirole.app.utils;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class DataConnect {
	
	// driverManager class - creation of the connection to the database server
	public DataConnect() throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException("Problem with loading JDBC driver.");
		}
	}

	// connecting MYSQL using JDBC DriverManager interface
	public static Connection getConnection() throws Exception {
		try {
			return (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/multiroleapp_connection",
					"root", "");
		} catch (SQLException e) {
			throw new ClassNotFoundException("Cannot connect with database.");
		}
	}
}
