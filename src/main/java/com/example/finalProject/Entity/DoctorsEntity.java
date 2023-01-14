package com.example.finalProject.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name="Doctors")
@Data
public class DoctorsEntity {

	@Column (name = "id")
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 Integer id;

	 @Column (name = "username")
	 @NotNull(message="username can not be empty")
	 String username;

	 @Column (name = "password")
	 @NotNull(message="password can not be empty")
	 String password;
	 
	 @Column (name = "name")
	 @NotNull(message="name can not be empty")
	 String name; 
	 
	 @Column (name = "nationalID")
	 @NotNull(message="nationalID can not be empty or duplicated")
	 String nationalID; 

	 @Column (name = "specialty")
	 @NotNull(message="specialty can not be empty")
	 String specialty; 
	 
	 
	 @Column (name = "phoneNumber")
	 @NotNull(message="phoneNumber can not be empty")
	 String phoneNumber; 
	 
	
}
