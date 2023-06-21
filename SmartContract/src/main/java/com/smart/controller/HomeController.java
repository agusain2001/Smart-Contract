package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entity.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;


@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRep;
	
	  @GetMapping("/removeMessage")
	    public String removeMessage(HttpSession session) {
	        session.removeAttribute("message");
	        return "redirect:/signup";
	    }
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title","Home - Smart Contact Manager ");
		return "home";
	}
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","Home - Smart Contact Manager ");
		return "about";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title","Register - Smart Contact Manager ");
		model.addAttribute("user",new User());
		return "signup";
	}
	
	@PostMapping("/do_register")
	public String registerUser(@ModelAttribute("user") User user,@RequestParam(value="agreement", defaultValue = "false") Boolean agreement, Model model ,HttpSession session) {
		try {
		
		if(!agreement) {
			System.out.println("you don't agree terms and condition");
	throw	new Exception("you don't agree terms and condition");
		}
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setImageUrl("default.png");
		
		this.userRep.save(user);
		model.addAttribute("user", new User()); 
		session.setAttribute("message", new Message("SuccessFully Registered","alert-success"));
		
		return "signup";
		}
		
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something Went Wrong "+e.getMessage(),"alert-danger"));
			

			return "signup";			
		}

	}

}
