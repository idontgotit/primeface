package com.example.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.example.entity.UserPhoto;
import com.example.service.User_photoService;

@Named
@javax.enterprise.context.ApplicationScoped
public class SelectOneMenuView {

	private UserPhoto user;
	private List<UserPhoto> lists;

	@Inject
	User_photoService user_photoService;

	@PostConstruct
	public void init() {
		lists = user_photoService.viewListUser_photo();
	}

	public UserPhoto getUser() {
		return user;
	}

	public void setUser(UserPhoto user) {
		this.user = user;
	}

	public List<UserPhoto> getLists() {
		if (lists == null)
			lists = user_photoService.viewListUser_photo();
		return lists;
	}

	public void setLists(List<UserPhoto> lists) {
		this.lists = lists;
	}

}
