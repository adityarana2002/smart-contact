package com.smart.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entites.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("title", "Home-Smart Contact Manager");
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About-Smart Contact Manager");
		return "about";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register-Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}

	// Handler for regestring User.
	@PostMapping("/do-register")
	public String registerUser( @Valid @ModelAttribute("user") User user,BindingResult result1,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement ,Model model,
			HttpSession session) {

		try {

			if (!agreement) {
				System.out.println("You have not agreed terms and conditions");
				throw new Exception("You have not agreed terms and conditions");
			}
			 
			if(result1.hasErrors()) {
				System.out.println("ERROR"+result1.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			user.setRole("Role_User");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			System.out.println("agreement" + agreement);
			System.out.println("User" + user);
			User result = this.userRepository.save(user);
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully Registered !!", "alert-success"));
			return "signup";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something Went Wrong !!" + e.getMessage(), "alert-danger"));
			return "signup";
		}

	}

}
