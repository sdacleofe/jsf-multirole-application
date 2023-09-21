package com.multirole.app.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

@ManagedBean(name="navigationController")
public class NavigationController implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	public String redirectToRegisterPage() {
		return "registration.xhtml?faces-redirect=true";
	}
	public String redirectToLoginPage() {
		return "login.xhtml?faces-redirect=true";
	}
	public String redirectToHomePage() {
		return "buttons.xhtml?faces-redirect=true";
	}
	public String redirectToActivityPage() {
		return "activity.xhtml?faces-redirect=true";
	}
}
