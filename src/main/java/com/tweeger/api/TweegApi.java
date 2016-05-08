package com.tweeger.api;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
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

import com.tweeger.tweegs.Tweeg;
import com.tweeger.domain.users.User;
import com.tweeger.services.TweegService;


@Controller
@RequestMapping("/tweegs")
public class TweegApi {
	@Autowired
	private TweegService tweegService;
	
	Logger log = LoggerFactory.getLogger(TweegApi.class);
	private void addTweegs(Model model, User owner) {
		model.addAttribute("tweegs", tweegService.findAll() );
	}
	
	@RequestMapping(value="", method=RequestMethod.POST, consumes={"application/x-www-form-urlencoded"})
	@Secured("ROLE_USER")
	public String createTask( @RequestBody MultiValueMap<String, String> userData,
							  @AuthenticationPrincipal User owner,
			                  Model model ) throws ParseException {

		Date date = new Date("Sat Dec 01 00:00:00 GMT 2012");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String format = formatter.format(date);
		
		Tweeg tweeg = new Tweeg();
		tweeg.setText( userData.get("text").get(0) );
		tweeg.setOwnerId( owner.getId() );
		tweeg.setOwnerName( owner.getUsername());
		tweeg.setPosted ( format );
		if (userData.containsKey("isPublic") && userData.get("isPublic").get(0).equals("on") ){
			tweeg.setPublic(true);
		}else{
			tweeg.setPublic(false);
		}
		
		tweegService.save( tweeg );
		addTweegs( model, owner );
		model.addAttribute("userInfor", owner);
		return "redirect:/tweegs";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.GET)
	@Secured("ROLE_USER")
	public String getCreateForm( Model model, @AuthenticationPrincipal User owner ) {
		model.addAttribute("tweeg", new Tweeg());
		model.addAttribute("userInfor", owner);
		return "tweegs/create";
	}	
	
	@RequestMapping(value="/{tid}/delete", method=RequestMethod.GET)
	@Secured("ROLE_USER")
	public String deleteTask( @PathVariable String tid, Model model, 
							@AuthenticationPrincipal User owner, HttpServletResponse response ) throws IOException {
		if( tweegService.findById(tid)==null ){
			response.sendError(400,"Tweeg doesn't exist!");
			addTweegs( model, owner );
			model.addAttribute("userInfor", owner);
			return "tweegs/tweegs";
		}
		String tidOwnerId = tweegService.findById(tid).getOwnerId();
		
		if( !tidOwnerId.equals(owner.getId())){
			response.sendError(403,"Can't delete other people's tweegs!");
		}else{
			tweegService.delete( tid );
		}
		addTweegs( model, owner );
		model.addAttribute("userInfor", owner);
		return "tweegs/tweegs";
	}	
	
	@RequestMapping(value="", method=RequestMethod.GET)
	@Secured("ROLE_USER")
	public String getTasks( Model model,
							@AuthenticationPrincipal User owner ) {		
		addTweegs( model, owner );
		model.addAttribute("userInfor", owner);
		return "tweegs/tweegs";
	}
	
}