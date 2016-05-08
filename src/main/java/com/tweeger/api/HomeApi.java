package com.tweeger.api;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tweeger.domain.users.User;
@Controller
public class HomeApi {
	@RequestMapping( value="/home", method=RequestMethod.GET )
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public String home(@AuthenticationPrincipal User user) {
		if( user.hasRole("ROLE_ADMIN") ) {
			return "redirect:/users";
		} else {
			return "redirect:/tweegs";
		}
	}
	
	@RequestMapping( value="/", method=RequestMethod.GET )
	public String welcome(@RequestParam(required=false, defaultValue="false") Boolean error, Model model) {
		model.addAttribute("error", error);
		return "login";
	}
}
