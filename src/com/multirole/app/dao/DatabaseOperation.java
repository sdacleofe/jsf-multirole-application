package com.multirole.app.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;
import com.multirole.app.bean.NavigationController;
import com.multirole.app.bean.UserBean;
import com.multirole.app.utils.DataConnect;

public class DatabaseOperation extends DataConnect {
	public DatabaseOperation() throws ClassNotFoundException {
		super();
	}

	/* validate email and password */
	public static boolean validateLogin(String emailAddress, String password) throws Exception {
		
		// string value of query statement
		String findQuery = "SELECT email_address, password, user_role FROM user WHERE email_address ='" + emailAddress
				+ "' AND password ='" + password + "'";
		try (Connection conn = getConnection()) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(findQuery);
			while (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			throw new SQLException("Problem while querying data from database.");
		}
		return false;
	}

	/* fetch users data from database */
	public static ArrayList<String> getUsers(String emailAddress, String password) throws Exception {
		String query = "SELECT * FROM user WHERE email_address = '" + emailAddress + "' AND password = '" + password
				+ "'";
		ArrayList<String> userInfoList = new ArrayList<>();
		try (Connection conn = getConnection()) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				userInfoList.add(rs.getString("user_id"));
				userInfoList.add(rs.getString("first_name"));
				userInfoList.add(rs.getString("last_name"));
				userInfoList.add(rs.getString("email_address"));
				userInfoList.add(rs.getString("account_created"));
				userInfoList.add(rs.getString("password"));
				userInfoList.add(rs.getString("user_role"));
			}
			conn.close();
			return userInfoList;
		} catch (SQLException e) {
			throw new SQLException("Problem while querying data from database.");
		}
	}

	// fetch all data user activities
	public static ArrayList<UserBean> getActivity(String r1, String r2, String r3, String sortName, String sortKeyword)
			throws Exception {
		String query = "SELECT * FROM logger WHERE role = '" + r1 + "' OR role = '" + r2 + "' OR role = '" + r3
				+ "' ORDER BY " + sortName + " " + sortKeyword + "";
		ArrayList<UserBean> logsList = new ArrayList<UserBean>();
		try (Connection conn = getConnection()) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				UserBean logger = new UserBean();
				logger.setLogID(rs.getString("log_id"));
				logger.setLogCreated(rs.getString("log_created"));
				logger.setLogRole(rs.getString("role"));
				logger.setLogMessage(rs.getString("message"));
				logsList.add(logger);
			}
			conn.close();
			return logsList;
		} catch (SQLException e) {
			throw new SQLException("Problem while querying data from database.");
		}
	}
	
	/* insert new user record data */
	public static void insertUserRecordDB(String firstName, String lastName, String emailAddress, String password,
		String user_role) throws Exception {
		String insertQuery = "INSERT INTO user (user_id, first_name, last_name, email_address, password, user_role) VALUES (user_id, '"
				+ firstName + "', '" + lastName + "', '" + emailAddress + "', '" + password + "', '" + user_role
				+ "' )";
		try (Connection conn = getConnection()) {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(insertQuery);
			conn.close();
		} catch (SQLException e) {
			throw new SQLException("Problem while querying data from database.");
		}
	}
	
	/* update user data */
	public static void updateUserRecordDB(String userID, String firstName, String lastName, String emailAddress) throws Exception {
		String updateQuery = "Update user SET first_name = '" +firstName+ "', last_name = '" +lastName+ "', email_address ='" +emailAddress+ "' WHERE user_id = " +Integer.valueOf(userID)+ "";
		try (Connection conn = getConnection()) {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(updateQuery);
			conn.close();
		} catch (SQLException e) {
			throw new SQLException("Problem while querying data from database.");
		}
	}
	
	/* insert new user activity */
	public static void insertUserActivityDB(String userRole, String logMessage) throws Exception {
		String insertQuery = "INSERT INTO logger (log_id, log_created, role, message) VALUES (log_id, CURRENT_TIMESTAMP(), '"
				+ userRole + "', '" + logMessage + "' )";
		try (Connection conn = getConnection()) {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(insertQuery);
			conn.close();
		} catch (SQLException e) {
			throw new SQLException("Problem while querying data from database.");
		}
	}
	
	/* remove all activity from logs */
	public static String clearLogsDB(String userRole, String firstName) throws Exception {
		NavigationController navigation = new NavigationController();
		String navigationResult = navigation.redirectToActivityPage();
		String deleteQuery = "DELETE FROM logger";
		try (Connection conn = getConnection()) {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(deleteQuery);
			DatabaseOperation.insertUserActivityDB(userRole, "[" + firstName + "] clears the log.");
			conn.close();
		} catch (SQLException e) {
			throw new SQLException("Problem while querying data from database.");
		}
		return navigationResult;
	}

}