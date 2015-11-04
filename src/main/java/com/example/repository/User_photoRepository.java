package com.example.repository;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import com.example.entity.UserPhoto;

@Repository(forEntity = UserPhoto.class)
public interface User_photoRepository extends EntityRepository<UserPhoto, Integer> {

	public List<UserPhoto> findByPhotoName(String name);

}
