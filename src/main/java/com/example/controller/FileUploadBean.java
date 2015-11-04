package com.example.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.example.entity.UserPhoto;
import com.example.entity.Users;
import com.example.service.User_photoService;
import com.example.service.UsersService;

@Named(value = "fileUploadBean")
@RequestScoped

public class FileUploadBean implements Serializable {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	@Inject
	private User_photoService user_photoService;

	@Inject
	UsersService userService;

	@Inject
	Login loGin;

	@Inject
	ViewImageDB viewImageDB;

	@Inject
	SelectOneMenuView oneMenuView;

	private String name;
	private UploadedFile resume;
	UserPhoto user_photo = new UserPhoto();
	private List<UserPhoto> listAdd;
	Users user = new Users();

	public UploadedFile getResume() {
		return resume;
	}

	public void setResume(UploadedFile resume) {
		this.resume = resume;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;

	}

	public List<UserPhoto> getListAdd() {
		return listAdd;
	}

	public void setListAdd(List<UserPhoto> listAdd) {
		this.listAdd = listAdd;
	}

	public void uploadPhoto(FileUploadEvent e) throws IOException {

		UploadedFile uploadedPhoto = e.getFile();

		String filePath = "/home/ngocpt/test/";
		byte[] bytes = null;

		if (null != uploadedPhoto) {
			bytes = uploadedPhoto.getContents();
			String filename = FilenameUtils.getName(uploadedPhoto.getFileName());
			// user_photo.setPhoto_name(filename);
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath + filename)));
			stream.write(bytes);
			// user_photo.setPhoto_data(bytes);
			user_photoService.addUser_photo(user_photo, filename, bytes);
			listAdd = user_photoService.findByFileName(filename);
			FacesContext context = FacesContext.getCurrentInstance();
			user = (Users) context.getExternalContext().getSessionMap().get("user");
			// user = userService.findById(1);
			for (int i = 0; i < listAdd.size(); i++) {

				user.setUserPhoto(listAdd.get(i));
				userService.updateUser(user);
				loGin.setListUsers(null);
				viewImageDB.setListUsersPhoto(null);
				oneMenuView.setLists(null);
			}

			stream.close();
		}

		FacesContext.getCurrentInstance().addMessage("messages",
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Your Photo (File Name " + uploadedPhoto.getFileName()
						+ " with size " + uploadedPhoto.getSize() + ")  Uploaded Successfully", ""));
	}

}
