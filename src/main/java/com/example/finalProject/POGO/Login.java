package com.example.finalProject.POGO;

import javax.validation.constraints.NotBlank;

import lombok.Data;


@Data
public class Login {

	@NotBlank(message="username can't be empty or null")
	public String username;
	@NotBlank(message="password can't be empty or null")
	public String password;

}
