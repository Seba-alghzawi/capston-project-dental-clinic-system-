package com.example.finalProject.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;



import lombok.Data;

@Entity
@Table(name="Patients")
@Data
public class PatientEntity {

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
	 
	 @Column (name = "phoneNumber")
	 @NotNull(message="phoneNumber can not be empty")
	 String phoneNumber; 
	 
	 @Column (name = "age")
	 Integer age; 
	 
	 
	 @Column (name = "gender")
	 String gender;
 
}
