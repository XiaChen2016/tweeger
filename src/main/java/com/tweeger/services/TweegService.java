package com.tweeger.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweeger.repositories.tweegs.MongoTweegRepository;
import com.tweeger.domain.users.User;
import com.tweeger.tweegs.Tweeg;

@Service
public class TweegService {

	@Autowired
	private MongoTweegRepository tweegRepository;
	
	public List<Tweeg> findByOwner(User owner) {
		return tweegRepository.findByOwnerId( owner.getId() );
	}
	public Tweeg findById(String tid) {
		return tweegRepository.findOne(tid);
	}

	
	public boolean save( Tweeg t ) {
		tweegRepository.save(t);
		return true;
	}

	public void delete(String tid) {
		tweegRepository.delete(tid);
	}

	public  List<Tweeg> findAll() {
		
		return tweegRepository.findAll();
		}
	
}
