package com.example.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;

import com.example.entity.Users;
import com.example.service.UsersService;

@Named
@SessionScoped

public class Login implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;

	private String pwd;
	private String msg;
	private String user;
	private String sex;
	private int id;
	private Users current_user;
	Users newUser = new Users();

	@Inject
	private UsersService UsersService;
	@Inject
	private Bean bean;

	private List<Users> listUsers;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getSex() {

		return sex;
	}

	public void setSex(String sex) {

		this.sex = sex;
	}

	public void isAdmin(ComponentSystemEvent event) {

		FacesContext fc = FacesContext.getCurrentInstance();

		Users _user = (Users) fc.getExternalContext().getSessionMap().get("user");

		String t = _user.getUserName();

		if (!"admin".equals(t))
			System.out.println("you dont have permission");

		bean.switchToPage("login");

	}

	public void login() {

		Users current = new Users();
		current = UsersService.checkLogin(user, pwd);
		if (current == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect Username and Password", ""));
			bean.setLogin(false);
			return;

		} else {

			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getSessionMap().put("user", current);
			current_user = (Users) context.getExternalContext().getSessionMap().get("user");
			String t = current.getUserName();
			if ("admin".equals(t))
				bean.setAdmin(true);

			bean.setLogin(true);
			bean.switchToPage("1");
		}

	}

	public void logOut() {

		Users currentUser = (Users) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

		bean.logOut("login");
		bean.setLogin(false);
	}

	public void addUser() {

		UsersService.addUser(newUser, user, pwd, sex);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Add Success", ""));
		this.listUsers = null;

		bean.switchToPage("home");

	}

	public List<Users> getListUsers() {

		if (this.listUsers == null) {
			this.listUsers = UsersService.viewListUsers();
		}

		return listUsers;
	}

	public void setListUsers(List<Users> listUsers) {
		this.listUsers = listUsers;
	}

	public void deleteUser(Users user) {
		UsersService.deleteUser(user);
		listUsers = UsersService.viewListUsers();

	}

	public void search() {

		listUsers = UsersService.findByUserNameLike("%" + msg + "%");
		bean.switchHomeTab("home");

	}

	public Users get_user() {
		return current_user;
	}

	public void set_user(Users _user) {
		this.current_user = _user;
	}

}
