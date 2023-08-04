package com.smart.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@javax.persistence.Entity
@Table(name="CONTACT")
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cid;
	private String name;
	private String secondName;
	private String work;
	private String email;
	private String image;
	@Column(length = 1000)
	private String description;
	private String phone;
	
	@JsonIgnore
	@ManyToOne
	private User user;
	
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}


	@Override
	public String toString() {
		return "Contact [cid=" + cid + ", name=" + name + ", secondName=" + secondName + ", work=" + work + ", email="
				+ email + ", image=" + image + ", description=" + description + ", phone=" + phone + ", user=" + user
				+ "]";
	}
	
	
	
	
	

}
