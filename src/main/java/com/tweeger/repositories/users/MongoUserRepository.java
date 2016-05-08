package com.tweeger.repositories.users;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tweeger.domain.users.User;

public interface MongoUserRepository extends MongoRepository<User, String>, UpdateableUserRepository {
	public User findByUsername(String username);
}
