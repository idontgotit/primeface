package com.example.serviceImpl;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.example.entity.UserPhoto;
import com.example.repository.User_photoRepository;
import com.example.service.User_photoService;

public class User_photoServiceImpl implements Serializable, User_photoService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2717105637115266771L;

	@Inject
	private User_photoRepository user_photoRepository;

	public UserPhoto addUser_photo(UserPhoto user, String photo_name, byte[] photo_data) {
		user.setPhotoName(photo_name);
		user.setPhotoData(photo_data);
		return user_photoRepository.saveAndFlush(user);
	}

	public List<UserPhoto> viewListUser_photo() {
		return this.user_photoRepository.findAll();
	}

	public UserPhoto getUserByKey(Integer primaryKey) {
		return this.user_photoRepository.findBy(primaryKey);
	}

	@Override
	public List<UserPhoto> findByFileName(String filename) {
		// TODO Auto-generated method stub
		return this.user_photoRepository.findByPhotoName(filename);
	}

}
