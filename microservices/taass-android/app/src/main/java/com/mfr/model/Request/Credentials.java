package com.mfr.model.Request;

import java.io.Serializable;

/**
 *
 * @author Luca
 */
public class Credentials implements Serializable{

	private String username;
	private String password;
	private String email;

	public Credentials() {}

	public Credentials(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
