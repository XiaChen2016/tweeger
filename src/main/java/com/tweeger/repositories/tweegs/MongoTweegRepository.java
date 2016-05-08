package com.tweeger.repositories.tweegs;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tweeger.tweegs.Tweeg;

public interface MongoTweegRepository extends MongoRepository< Tweeg, String>, UpdateableTweegRepository {
	List<Tweeg> findByOwnerId(String id);
}
