package org.jcruells.sm.client.data;

import com.google.common.base.Objects;

/**
 * A simple object to represent a user in the SymptomManagement Application.
 * 
 * @author jordi
 * 
 */

public class User {

	private long id;
	private String username;
	private String password;
	private Integer recordNumber;	
	private String role;
	
	public User() {}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public Integer getRecordNumber() {
		return recordNumber;
	}
	public void setRecordNumber(Integer recordNumber) {
		this.recordNumber = recordNumber;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}
