package com.example.controller;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.example.entity.UserPhoto;
import com.example.service.User_photoService;

@Named
@javax.enterprise.context.ApplicationScoped
public class ViewImageDB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	@Inject
	User_photoService user_photoService;

	private List<UserPhoto> listUsersPhoto;

	private int photo_id;
	private String photo_name;
	private byte[] photo_data;

	@PostConstruct
	public void init() {
		listUsersPhoto = user_photoService.viewListUser_photo();
	}

	public List<UserPhoto> getListUsersPhoto() {
		if (listUsersPhoto == null)
			listUsersPhoto = user_photoService.viewListUser_photo();

		return listUsersPhoto;
	}

	public void setListUsersPhoto(List<UserPhoto> listUsersPhoto) {
		this.listUsersPhoto = listUsersPhoto;
	}

	public int getPhoto_id() {
		return photo_id;
	}

	public void setPhoto_id(int photo_id) {
		this.photo_id = photo_id;
	}

	public String getPhoto_name() {
		return photo_name;
	}

	public void setPhoto_name(String photo_name) {
		this.photo_name = photo_name;
	}

	public byte[] getPhoto_data() {
		return photo_data;
	}

	public void setPhoto_data(byte[] photo_data) {
		this.photo_data = photo_data;
	}

	public StreamedContent methodConvertToStream() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {

			return new DefaultStreamedContent();
		} else {
			// So, browser is requesting the image. Return a real
			// StreamedContent with the image bytes.
			String imageId = context.getExternalContext().getRequestParameterMap().get("id");

			UserPhoto phto = user_photoService.getUserByKey(Integer.parseInt(imageId));

			return new DefaultStreamedContent(new ByteArrayInputStream(phto.getPhotoData()));

		}

	}
}
