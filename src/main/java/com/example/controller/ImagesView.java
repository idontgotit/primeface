package com.example.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class ImagesView implements Serializable {

	/**
	 * 
	 */

	@Inject
	ViewImageDB viewImageDB;

	private static final long serialVersionUID = 1L;
	private List<Integer> images = new ArrayList<Integer>();

	@PostConstruct
	public void init() {

	}

	public List<Integer> getImages() {
		for (int i = 0; i < viewImageDB.getListUsersPhoto().size(); i++) {
			images.add(i);
		}
		return images;
	}
}