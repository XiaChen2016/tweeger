package com.tweeger.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tweeger.domain.users.Role;
import com.tweeger.domain.users.User;
import com.tweeger.services.UsersService;

@Controller
@RequestMapping("/users")
public class AdminApi {
	@Autowired
	private UsersService userService;
		
	private void addUsers( Model model ) {
		model.addAttribute("users", userService.getUsers() );
	}
	
	@RequestMapping(value="", method=RequestMethod.POST, consumes={"application/x-www-form-urlencoded"})
	@Secured("ROLE_ADMIN")
	public String createUser( @RequestBody MultiValueMap<String, String> userData,
			                  Model model, @AuthenticationPrincipal User currentUser ,HttpServletResponse response) throws IOException {
		if( userData.getFirst("username").equals("Bilbo")  ){
			response.sendError(400,"Can't create anoter user named Bilbo!");
			model.addAttribute("userInfor", currentUser);
			addUsers( model );	
			return "/users/users";
		}
		List<Role> roles;
		User user = new User();
		System.out.printf("Username: %s, password: %s\n", userData.getFirst("username"), userData.getFirst("password"));
		if( userData.containsKey("admininput") && userData.get("admininput").get(0).equals("on") ) {
			roles = Arrays.asList( new Role[] { new Role("ROLE_ADMIN"),new Role("ROLE_USER") } );
			user.setIsAdmin(true);
		}
		else{
			roles = Arrays.asList( new Role[] { new Role("ROLE_USER") } );
			user.setIsAdmin(false);
		}
		user.setPassword( userData.get("password").get(0) );
		user.setUsername( userData.get("username").get(0) );
		user.setRoles(roles);
		userService.save( user );
		model.addAttribute("userInfor", currentUser);
		addUsers( model );		
		return "/users/users";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String createUser( Model model ) {
		model.addAttribute("user", new User());
		return "users/create";
	}	
	
	
	@RequestMapping(value="/delete", method=RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String deleteUser( Model model , @RequestParam String uid, 
							@AuthenticationPrincipal User user,
							HttpServletResponse response) throws IOException {
		if( userService.findOne(uid)==null){
			response.sendError(400,"User doesn't exist!");
			model.addAttribute("userInfor", user);
			addUsers( model );
			return "users/users";
		}
		userService.delete(uid);
		if( uid.equals("0")){
			response.sendError(403,"Can't delete admin Bilbo!");
		} else{
			userService.delete(uid);
		}
		model.addAttribute("userInfor", user);
		addUsers( model );
		return "users/users";
	}	
	
	@RequestMapping(value="", method=RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public String getUsers( Model model, @AuthenticationPrincipal User user ) {
		addUsers( model );
		model.addAttribute("userInfor", user);
		return "users/users";
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String updateUser( Model model,
								@RequestBody MultiValueMap<String, String> userData,
								@RequestParam String uid, HttpServletResponse response,
								@AuthenticationPrincipal User currentUser ) throws IOException {	
		if( uid.equals("0") ||  userData.get("username").get(0) .equals("Bilbo"))
		{
			response.sendError(400,"Can't edit admin Bilbo!");
			addUsers( model );
			model.addAttribute("userInfor", currentUser );
			return "users/users";
		} 
		
		List<Role> roles;

		User user = userService.findOne(uid);
		user.setUsername( userData.get("username").get(0) );
		user.setPassword( userData.get("passwordinput").get(0) );
		
		if( userData.containsKey("admininput") && userData.get("admininput").get(0).equals("on")){
			roles = Arrays.asList( new Role[] { new Role("ROLE_ADMIN"),new Role("ROLE_USER") } );
			user.setRoles(roles);
			user.setIsAdmin(true);
		} else{
			roles = Arrays.asList( new Role[] { new Role("ROLE_USER") } );
			user.setRoles(roles);
			user.setIsAdmin(false);
		}
		userService.update(user) ;
		model.addAttribute("userInfor", currentUser);
		addUsers( model );	
		return "users/users";
	}
}
