package com.tweeger.repositories.tweegs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.tweeger.tweegs.Tweeg;

public class MongoTweegRepositoryImpl implements UpdateableTweegRepository {

	@Autowired
	private MongoOperations mongo;
		
	private Update getUpdate(Tweeg x, Tweeg y) {
		Update update = new Update();
		update.set("isPublic", y.getIsPublic() );
//		update.set("description",));
//		update.set("color", y.getColor());
//		update.set("completed", y.getCompleted());
//		update.set("due", y.getDue());

		return update;
	}
	
	
	@Override
	public void update(Tweeg task) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(task.getId() ) );
		Tweeg old = mongo.findOne(query,  Tweeg.class);		
		mongo.updateFirst(query, getUpdate(old, task), Tweeg.class);		
	}

}
