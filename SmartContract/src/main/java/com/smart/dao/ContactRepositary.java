package com.smart.dao;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entity.Contact;
import com.smart.entity.User;



public interface ContactRepositary extends JpaRepository<Contact,Integer> {
		
	@Query("FROM Contact AS c WHERE c.user.id = :userId")
	public Page<Contact> findContactByUser(@Param("userId") int userId,org.springframework.data.domain.Pageable pePageable);

	public List<Contact> findByNameContainingAndUser(String name,User user);
	
}
