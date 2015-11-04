package com.example.entity;

public class SearchUser {

	private int id;
	private String userName;
	private String password;
	private String sex;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public SearchUser(int id, String userName, String password, String sex) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.sex = sex;
	}

}
