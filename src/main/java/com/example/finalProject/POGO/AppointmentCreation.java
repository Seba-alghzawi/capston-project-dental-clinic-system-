package com.example.finalProject.POGO;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;


import lombok.Data;

@Data
public class AppointmentCreation {

	
	private Integer doctorId;
	private Integer patientId;

	
	// @DateTimeFormat( pattern = "HH:mm")
	
	// @JsonFormat( pattern = "HH:mm" ,shape = JsonFormat.Shape.STRING)
	 @DateTimeFormat( pattern = "HH:mm")
	 private String appointmentTime;
	 
	
	 //@DateTimeFormat(pattern = "dd/MM/yyyy")
	 
	// @JsonFormat(pattern = "dd/MM/yyyy" ,shape = JsonFormat.Shape.STRING) 
	 @DateTimeFormat(pattern = "dd/MM/yyyy")
	 private String appointmentDate;

//	 @DateTimeFormat( pattern = "HH:mm")
//	 @DateTimeFormat(pattern = "dd/MM/yyyy")
}
