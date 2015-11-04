package com.example.serviceImpl;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.example.entity.Users;
import com.example.repository.UsersRepository;
import com.example.service.UsersService;

@Named
@SessionScoped
public class UsersServiceImpl implements UsersService, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 12L;
	@Inject
	private UsersRepository usersRepository;

	public Users checkLogin(String username, String password) {
		List<com.example.entity.Users> listuser = usersRepository.findAll();
		for (int i = 0; i < listuser.size(); i++) {

			if (listuser.get(i).getUserName().equals(username) && listuser.get(i).getPassword().equals(password))
				return listuser.get(i);

		}
		return null;
	}

	public Users addUser(Users user, String username, String password, String sex) {

		user.setUserName(username);
		user.setPassword(password);
		user.setSex(sex);
		return usersRepository.save(user);

	}

	// public List<Users> getListUsers() {
	// if (this.listUsers == null) {
	// this.listUsers = this.UsersRepository.findAll();
	// }
	// return listUsers;
	// }
	//
	// public void setListUsers(List<Users> listUsers) {
	// this.listUsers = listUsers;
	// }

	public List<Users> viewListUsers() {
		return this.usersRepository.findAll();
	}

	public Users updateUser(Users user) {
		return usersRepository.saveAndFlush(user);
	}

	public void deleteUser(Users user) {
		usersRepository.attachAndRemove(user);
	}

	public Users findById(int id) {
		return usersRepository.findBy(id);

	}

	@Override
	public Users findByUsernameAndPassword(String username, String password) {
		// TODO Auto-generated method stub
		return usersRepository.findByUsernameAndPassword(username, password);
	}

	@Override
	public List<Users> findByUserName(String name) {
		// TODO Auto-generated method stub
		return usersRepository.findByUserName(name);
	}

	@Override
	public List<Users> findByUserNameLike(String userName) {
		// TODO Auto-generated method stub
		return usersRepository.findByUserNameLike(userName);
	}

}
