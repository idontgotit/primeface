package com.example.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@javax.enterprise.context.SessionScoped
public class MenuView implements Serializable {

	/**
	 * 
	 */
	@Inject
	private Bean bean;

	private static final long serialVersionUID = -5664919980412857962L;

	public void save() {

		addMessage("Success", "Data loaded");
		bean.setShowImg(true);

	}

	public void update() {
		addMessage("Success", "Hidding");
		bean.setShowImg(false);
	}

	public void delete() {
		bean.switchToPage("media");
		addMessage("Success", "Data deleted");
	}

	public void showImg() {
		bean.switchToPage("imgData");
		addMessage("Success", "ImgLoaded");
	}

	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
}