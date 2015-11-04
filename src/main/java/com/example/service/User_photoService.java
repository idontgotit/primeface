package com.example.service;

import java.util.List;

import com.example.entity.UserPhoto;

public interface User_photoService {

	public UserPhoto addUser_photo(UserPhoto user, String photo_name, byte[] photo_data);

	public List<UserPhoto> viewListUser_photo();

	public UserPhoto getUserByKey(Integer primaryKey);

	public List<UserPhoto> findByFileName(String filename);

}
