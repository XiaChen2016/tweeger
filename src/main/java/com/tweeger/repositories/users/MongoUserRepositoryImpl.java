package com.tweeger.repositories.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.tweeger.domain.users.User;

public class MongoUserRepositoryImpl implements UpdateableUserRepository {
	@Autowired
//	@Qualifier("defaultMongoTemplate")
	private MongoOperations mongo;
		
	private Update getUpdate(User x, User y) {
		Update update = new Update();
		update.set("roles", y.getRoles());
		update.set("isAdmin", y.getIsAdmin());
		update.set("username", y.getUsername() );
		update.set("password", y.getPassword());
		return update;
	}
	
	@Override
	public void update(User user) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(user.getId()));
		User old = mongo.findOne(query,  User.class);		
		mongo.updateFirst(query, getUpdate(old, user), User.class);
	}
}
