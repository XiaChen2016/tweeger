package com.tweeger.services;


import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tweeger.domain.users.Role;
import com.tweeger.domain.users.User;
import com.tweeger.repositories.users.MongoUserRepository;

@Service
public class UsersService implements UserDetailsService {
	@Autowired
	private MongoUserRepository userRepository;
		
	public User loadUserByUsername(String username) throws UsernameNotFoundException {		
		User user = userRepository.findByUsername(username);		
		if(user == null) throw new UsernameNotFoundException("username");
		return user;
	}
	
	
	public List<User> getUsers() {
		return userRepository.findAll();
	}
	
	public User findOne( String uid){
		return userRepository.findOne( uid );
	}
	public boolean save( User user ) {
		User exists = userRepository.findByUsername(user.getUsername());
		if( exists != null) return false;
		
		userRepository.save( user );
		return true;
	}
	
	public boolean update( User user ) {
		userRepository.update( user );
		return true;
	}
	
	public void delete( String uid  ){
		userRepository.delete(uid);
	}
	
	@PostConstruct
	private void initDatabase() {
	//if(true) return;
	List<Role> roles = Arrays.asList( new Role[] { new Role("ROLE_ADMIN") ,new Role("ROLE_USER") } );
	
	User user = new User.Builder()
			.username("Bilbo")
			.password("Baggins")
			.id("0")
			.isAdmin(true)
			.roles( roles)
			.build();
	
		User temp = userRepository.findByUsername( "Bilbo") ;
		if( userRepository.findByUsername("Bilbo") == null ){
			userRepository.save(user);
		}
		if(  !temp.getPassword().equals("Baggins") ||  !temp.getId().equals("0")) {
			userRepository.delete(temp.getId());
			userRepository.save(user);
		} 
		
	}
}