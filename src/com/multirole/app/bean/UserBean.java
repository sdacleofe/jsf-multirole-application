package com.multirole.app.bean;

import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import com.multirole.app.dao.DatabaseOperation;

@ManagedBean(name = "userBean")
@SessionScoped

public class UserBean implements ActionListener {

	/* accessors and mutators */
	private String userID;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String accountCreated;
	private String password;
	private String userRole;

	private String logID;
	private String logCreated;
	private String logMessage;
	private String logRole;

	private boolean disabledAdmin;
	private boolean disabledRegular;
	private boolean disabledReadOnly;
	private boolean disabledClearLogButton;
	private boolean disabledUpdateButton;
	
	private String sortName = "log_id";
	private String orderByKeyword = "ASC";

	private ArrayList<?> usersListDB;
	private ArrayList<?> logsListDB;

	private NavigationController navigation = new NavigationController();

	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public ArrayList<?> userList() throws Exception {
		return usersListDB;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getSortName() {
		return sortName;
	}
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}
	public String getOrderByKeyword() {
		return orderByKeyword;
	}
	public void setOrderByKeyword(String orderByKeyword) {
		this.orderByKeyword = orderByKeyword;
	}

	public String getAccountCreated() {
		return accountCreated;
	}
	public void setAccountCreated(String accountCreated) {
		this.accountCreated = accountCreated;
	}
	public String getLogID() {
		return logID;
	}
	public void setLogID(String logID) {
		this.logID = logID;
	}
	public String getLogCreated() {
		return logCreated;
	}
	public void setLogCreated(String logCreated) {
		this.logCreated = logCreated;
	}
	public String getLogMessage() {
		return logMessage;
	}
	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	} 
	public String getLogRole() {
		return logRole;
	}
	public void setLogRole(String logRole) {
		this.logRole = logRole;
	}
	public boolean isDisabledAdmin() {
		return disabledAdmin;
	}
	public void setDisabledAdmin(boolean disabledAdmin) {
		this.disabledAdmin = disabledAdmin;
	}
	public boolean isDisabledRegular() {
		return disabledRegular;
	}
	public void setDisabledRegular(boolean disabledRegular) {
		this.disabledRegular = disabledRegular;
	}
	public boolean isDisabledReadOnly() {
		return disabledReadOnly;
	}
	public void setDisabledReadOnly(boolean disabledReadOnly) {
		this.disabledReadOnly = disabledReadOnly;
	} 
	public boolean isDisabledClearLogButton() {
		return disabledClearLogButton;
	}
	public void setDisabledClearLogButton(boolean disabledClearLogButton) {
		this.disabledClearLogButton = disabledClearLogButton;
	}
	public boolean isDisabledUpdateButton() {
		return disabledUpdateButton;
	}
	public void setDisabledUpdateButton(boolean disabledUpdateButton) {
		this.disabledUpdateButton = disabledUpdateButton;
	}
	
	public String validateUserLogin() throws Exception {
		// check login credentials if true or false
		boolean validDB = DatabaseOperation.validateLogin(emailAddress, password);
		
		// populate user data in ArrayList
		usersListDB = DatabaseOperation.getUsers(emailAddress, password);

		// [user session] - populate the set method with user information of the current active user
		if (!usersListDB.isEmpty()) {
			setUserID(userList().get(0).toString());
			setFirstName(userList().get(1).toString());
			setLastName(userList().get(2).toString());
			setAccountCreated(userList().get(3).toString());
			setUserRole(userList().get(6).toString());
			setLogRole(userList().get(6).toString());
		}

		// [user role] - toggle for disabled buttons
		if (validDB) {
			String navigationResult = navigation.redirectToHomePage();
			if (userRole.contentEquals("Admin")) {
				disabledAdmin = false;
				disabledRegular = true;
				disabledReadOnly = true;
				disabledClearLogButton = false;
				disabledUpdateButton = false;
			} else if (userRole.contentEquals("Regular")) {
				disabledAdmin = true;
				disabledRegular = false;
				disabledReadOnly = true;
				disabledClearLogButton = true;
				disabledUpdateButton = false;
			} else if (userRole.contentEquals("Read Only")) {
				disabledAdmin = true;
				disabledRegular = true;
				disabledReadOnly = false;
				disabledClearLogButton = true;
				disabledUpdateButton = true;
			}
			DatabaseOperation.insertUserActivityDB(userRole, "[" + firstName + "] logged in successfully.");
			return navigationResult;
		} else {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Invalid username or password. Please try again"));
			return null;
		}
	}
	
	
	public String registerUserRecord() throws Exception {
		String firstName = getFirstName();
		String lastName = getLastName();
		String emailAddress = this.getEmailAddress();
		String password = this.getPassword();
		String userRole = this.getUserRole();
		
		if ((firstName != null) || (lastName != null) || (emailAddress != null) || (password != null)) {
			String navigationResult = navigation.redirectToLoginPage();
			DatabaseOperation.insertUserRecordDB(firstName, lastName, emailAddress, password, userRole);
			DatabaseOperation.insertUserActivityDB(userRole, "[" + firstName + "] new user has been registered.");
			return navigationResult;
		} else {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Please fill in all required fields"));
	        return null;
		}
	}
	
	public String updateUserRecord() throws Exception {
		DatabaseOperation.updateUserRecordDB(userID, firstName, lastName, emailAddress);
		DatabaseOperation.insertUserActivityDB(userRole, "[" + firstName + "] profile information has been updated.");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "New user data has been updated."));
		return null;
		
	}

	public ArrayList<?> logList() throws Exception {
		String keyword = getOrderByKeyword().toString();
		if (userRole.equals("Admin")) {
			logsListDB = DatabaseOperation.getActivity("Admin", "Regular", "Read Only", getSortName().toString(),
					keyword);
		} else if (userRole.equals("Regular")) {
			logsListDB = DatabaseOperation.getActivity("", "Regular", "Read Only", getSortName().toString(), keyword);
		} else if (userRole.equals("Read Only")) {
			logsListDB = DatabaseOperation.getActivity("", "", "Read Only", getSortName().toString(), keyword);
		}
		return logsListDB;
	}

	public String sortList(String sortName) {
		String navigationResult = navigation.redirectToActivityPage();
		boolean ascending = getOrderByKeyword().equals("ASC");
		if (ascending) {
			setOrderByKeyword("DESC");
			setSortName(sortName);
		} else {
			setOrderByKeyword("ASC");
			setSortName(sortName);
		}
		return navigationResult;
	}

	@Override
	public void processAction(ActionEvent event) throws AbortProcessingException {
		String id = event.getComponent().getId();

		if (id.equals("sortLogID")) {
			sortList("log_id");
		}

		if (id.equals("sortLogCreated")) {
			sortList("log_created");
		}

		if (id.equals("sortRole")) {
			sortList("role");
		}

		if (id.equals("sortMessage")) {
			sortList("message");
		}
	}

	public String activityActionProcess() throws Exception {
		String navigationResult = navigation.redirectToActivityPage();
		DatabaseOperation.insertUserActivityDB(userRole, "[" + firstName + "] redirects to activity page.");
		return navigationResult;
	}

	public String homeActionProcess() throws Exception {
		String navigationResult = navigation.redirectToHomePage();
		DatabaseOperation.insertUserActivityDB(userRole, "[" + firstName + "] redirects to home page.");
		return navigationResult;
	}

	public void adminActionProcess() throws Exception {
		DatabaseOperation.insertUserActivityDB(userRole, "[" + firstName + "] clicks the admin button.");
	}

	public void regularActionProcess() throws Exception {
		DatabaseOperation.insertUserActivityDB(userRole, "[" + firstName + "] clicks the regular button.");
	}

	public void readOnlyActionProcess() throws Exception {
		DatabaseOperation.insertUserActivityDB(userRole, "[" + firstName + "] clicks the read only button.");
	}

	public String clearLog() throws Exception {
		return DatabaseOperation.clearLogsDB(userRole, firstName);
	}

	public String logout() throws Exception {
		String navigationResult = navigation.redirectToLoginPage();

		// invalidate session when logout method is executed.
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		DatabaseOperation.insertUserActivityDB(userRole, "[" + firstName + "] account has been sign out.");

		return navigationResult;
	}
}
