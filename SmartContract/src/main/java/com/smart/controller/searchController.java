package com.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dao.ContactRepositary;
import com.smart.dao.UserRepository;
import com.smart.entity.Contact;
import com.smart.entity.User;

@RestController
public class searchController {
	
	@Autowired
	private UserRepository userRep;
	
	private ContactRepositary contactRep;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query,Principal principal){
		User user=this.userRep.getUserByUserName(principal.getName());
		
		List<Contact> contacts=this.contactRep.findByNameContainingAndUser(query, user);
		
		return ResponseEntity.ok(contacts);
	}

}
