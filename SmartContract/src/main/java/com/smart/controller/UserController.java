package com.smart.controller;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.GetMapping;?
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepositary;
import com.smart.dao.UserRepository;
import com.smart.entity.Contact;
import com.smart.entity.User;
import com.smart.helper.Message;
@Controller
@RequestMapping(value="/user")
public class UserController {
	
	
	
	@Autowired
	private UserRepository userRep;
	// Adding data to response in other method
	
	@Autowired
	private ContactRepositary contactRep;
	
	
	
	@ModelAttribute
	public void addCommonData(Model model,Principal principal) {
		String userName = principal.getName();
		System.out.println(userName);
		User user = userRep.getUserByUserName(userName);
		model.addAttribute("user", user);
	}
	
	//dashboard method 
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal) {
		model.addAttribute("title","dashboard");
		return "normal/user_dashboard";
	}
	
	//open add form handler
	@RequestMapping("/addContact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact
			,@RequestParam("profileImage") MultipartFile file 
			,Principal principal
			,HttpSession session
			) {
	
	try {	
		System.out.println(contact);
		String name = principal.getName();
		User user = this.userRep.getUserByUserName(name);
		
		if(file.isEmpty()) {
			System.out.println("File is empty");
			contact.setImage("default.jpg");
		}else {
			contact.setImage(file.getOriginalFilename());
			File saveFile = new ClassPathResource("static/image").getFile();
		Path path=	Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING );
		System.out.println("image is added");
		}
		
		contact.setUser(user);
		user.getContacts().add(contact);
		this.userRep.save(user);
		System.out.println("added the database");
		
		session.setAttribute("message", new com.smart.helper.Message("your contact is added !! Add new ONe","success"));
		
		return "normal/add_contact_form";}catch(Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new com.smart.helper.Message("Something went wrong","danger"));
			
			return "normal/add_contact_form";
		}
	
	
	}

	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page,Model m, Principal principal) {
		m.addAttribute("title", "showContacts");
		String userName=principal.getName();
		User user=this.userRep.getUserByUserName(userName);
		Pageable pageable=PageRequest.of(page, 3);
	Page<Contact> contact=	this.contactRep.findContactByUser(user.getId(),pageable);
	m.addAttribute("contacts", contact);
	m.addAttribute("currentPage", page);
	m.addAttribute("totalPages", contact.getTotalPages());
	
	return "normal/show_contacts";
	
	}
	
	@RequestMapping("/{cid}/contact")
	public String showContactDetail(@PathVariable("cid") Integer cid, Model model,Principal principal) {
		System.out.println(cid);
		Optional<Contact> contactOptional=this.contactRep.findById(cid);
		Contact contact = contactOptional.get();
		
		String userName = principal.getName();
		User user = this.userRep.getUserByUserName(userName);
		if(user.getId() ==  contact.getUser().getId()) {
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		}
		
		return "normal/contact_details";
	}
	
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Principal principal, HttpSession session) {
		Optional<Contact> contactOptional = this.contactRep.findById(cid);
		Contact contact = contactOptional.get();
		String userName = principal.getName();
		User user = this.userRep.getUserByUserName(userName);
		user.setContacts(null);
//		System.out.println(contact);
			contact.setUser(null);
		this.contactRep.delete(contact);
		System.out.println("contact delete");
		session.setAttribute("message", new Message("Contact deleted SuccessFully....","success"));

		return "redirect:/user/show-contacts/0";
	}
	
	
	@RequestMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") String cid, Model m) {
	    try {
	        Integer contactId = Integer.parseInt(cid);
	        m.addAttribute("title", "Update Contact");
	        Contact contact = this.contactRep.findById(contactId).orElse(null);
	        m.addAttribute("contact", contact);
	        return "normal/update_form";
	    } catch (NumberFormatException e) {
	        // Handle the case where the path variable 'cid' is not a valid integer
	        // For example, you can redirect to an error page or return an error message.
	        
	    	return "normal/update_form";
	    }
	}
	
	
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,Model model
			,HttpSession session , Principal principal) {
		
		try {
			 Contact oldContact = this.contactRep.findById(contact.getCid()).get();
			
			if(!file.isEmpty()) {
				File deleteFile = new ClassPathResource("static/image").getFile();
				File file1 = new File(deleteFile,oldContact.getImage());
				file1.delete();
				
				File saveFile = new ClassPathResource("static/image").getFile();
				Path path=	Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
					Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING );

				contact.setImage(file.getOriginalFilename());
				
				
			}else {
				contact.setImage(oldContact.getImage());
			}
			
			User user = this.userRep.getUserByUserName(principal.getName());
			
			contact.setUser(user);
			this.contactRep.save(contact);
			session.setAttribute("message", new Message("updated contact","success"));
			
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("contact"+contact.getName());
		return "normal/user_dashboard";
	}
	
	@GetMapping("/profile")
	public String yourProfile(Model m,Principal principal) {
		User user=this.userRep.getUserByUserName(principal.getName());
		m.addAttribute("title", "Profile Page");
		m.addAttribute("user",user);
		return "normal/profile";
	}
	
	
	
}


