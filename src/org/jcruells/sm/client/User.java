package org.jcruells.sm.client;





import java.util.Date;

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
	private String name;
	private String lastName;
	private Date birthDate;
	private Integer recordNumber;	
	private String password;
	private String role;

	public User() {
	}

	public User(String username, String name, String password, String role) {
		super();
		this.username = username;
		this.name = name;
		this.role = password;
		this.role = role;		
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Integer getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(Integer recordNumber) {
		this.recordNumber = recordNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Two Videos will generate the same hashcode if they have exactly the same
	 * values for their userId.
	 * 
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(username);
	}

	/**
	 * Two Videos are considered equal if they have exactly the same values for
	 * their name, url, and duration.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User other = (User) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(username, other.username);
		} else {
			return false;
		}
	}

}
