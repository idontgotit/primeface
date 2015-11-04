package com.example.service;

import java.util.List;

import com.example.entity.Users;

public interface UsersService {

	public Users checkLogin(String username, String password);

	public Users addUser(Users user, String username, String password, String sex);

	public List<Users> viewListUsers();

	public Users updateUser(Users user);

	public void deleteUser(Users user);

	public Users findById(int id);

	public Users findByUsernameAndPassword(String username, String password);

	public List<Users> findByUserName(String name);

	public List<Users> findByUserNameLike(String userName);
}
