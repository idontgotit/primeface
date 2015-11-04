package com.example.repository;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import com.example.entity.Users;

@Repository(forEntity = Users.class)
public interface UsersRepository extends EntityRepository<Users, Integer> {

	public List<Users> findByUserName(String name);

	public Users findByUsernameAndPassword(String username, String password);

	public List<Users> findByUserNameLike(String userName);

}
