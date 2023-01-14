package com.example.finalProject.Entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;




import lombok.Data;

@Entity
@Table(name="appointment")
@Data
public class AppointmentEntity {

 @Column (name = "id")
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 Integer id;
	
 @Column (name = "appointmentStatus" )
 @NotNull(message="appointmentStatus can not be empty")
 Integer appointmentStatus=1;
 
 @ManyToOne
 @JoinColumn(name="doctorId")
 DoctorsEntity doctorObj;

 @ManyToOne
 @JoinColumn(name="patientId")
 PatientEntity patientObj;


// @DateTimeFormat( pattern = "HH:mm")
 @JsonFormat( pattern = "HH:mm",shape = JsonFormat.Shape.STRING)
 @NotNull(message="appointmentTime can not be empty")
 @DateTimeFormat( pattern = "HH:mm")
 @Column (name = "appointmentTime")
 LocalTime appointmentTime;
 

 
 //@DateTimeFormat(pattern = "dd/MM/yyyy")
 @JsonFormat( pattern = "dd/MM/yyyy" ,shape = JsonFormat.Shape.STRING)
 @NotNull(message="appointmentDate can not be empty")
 @DateTimeFormat(pattern = "dd/MM/yyyy")
 @Column (name = "appointmentDate")
 LocalDate appointmentDate;
 
}

 