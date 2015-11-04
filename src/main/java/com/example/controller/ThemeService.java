package com.example.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import com.example.entity.UserPhoto;
import com.example.service.User_photoService;

@ManagedBean(name = "themeService")
@ApplicationScoped
public class ThemeService {

	@Inject
	User_photoService user_photoService;

	private List<UserPhoto> lists;

	@PostConstruct
	public void init() {
		lists = user_photoService.viewListUser_photo();
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