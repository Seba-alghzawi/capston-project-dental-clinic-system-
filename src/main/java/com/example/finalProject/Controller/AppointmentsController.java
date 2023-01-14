package com.example.finalProject.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.finalProject.Entity.AppointmentEntity;
import com.example.finalProject.Entity.DoctorsEntity;
import com.example.finalProject.Entity.PatientEntity;
import com.example.finalProject.POGO.AppointmentCreation;
import com.example.finalProject.POGO.PatientInfoBookedTime;
import com.example.finalProject.POGO.Result;
import com.example.finalProject.Repository.AppointmentRepository;
import com.example.finalProject.Repository.DoctorsRepository;
import com.example.finalProject.Repository.PatientRepository;
import com.example.finalProject.Service.AppointmentService;
import com.example.finalProject.Service.PatientsService;
import com.example.finalProject.Service.doctorsService;

@RestController
@RequestMapping("/Appointment")
public class AppointmentsController {

	
	@Autowired
	private AppointmentService service;
	
	@Autowired
	private PatientsService patientService;
	
	@Autowired
	private DoctorsRepository DoctorsRepo;
	
	@Autowired
	private PatientRepository PatientRepo;
	
	@Autowired
	private doctorsService doctorService;
	/*
	@GetMapping("/findBookedTimeToday")
	public Result BookedTimesOfDr(@RequestParam Integer DrId ) {
		 Result result =new Result();
		 Map<String, Object> mapResult = new HashMap<>();
		List<AppointmentEntity> app= service.findAppointment(DrId) ;
		if(app==null||app.size()==0)
		{
			result.setStatusCode("0");
	 		result.setStatusDescription("Succ");
	 		mapResult.put("Booked time today ", "no booked time today");
	 		result.setResultMap(mapResult);
			return result;
		}
		else
		{
			List<PatientInfoBookedTime> BookedList=new ArrayList<PatientInfoBookedTime>();
			for(int i=0;i<app.size();i++)
			{
				PatientInfoBookedTime p=new PatientInfoBookedTime();
				p.setPatientID(app.get(i).getId());
				p.setPatientName(app.get(i).getPatientObj().getName());
				p.setTime(app.get(i).getAppointmentTime());
				BookedList.add(p);
			}
			result.setStatusCode("0");
	 		result.setStatusDescription("Succ");
	 		mapResult.put("Booked time today", BookedList);
	 		result.setResultMap(mapResult);
			return result;
		}
	}
	
	
	 @RequestParam Integer doctorId,@RequestParam Integer patientId,
				@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy")LocalDate Date,
				@RequestParam @DateTimeFormat(pattern = "HH:mm")LocalTime time
	
	  @PostMapping ("/addAppointment")
		public Result addAppointment (HttpServletRequest request, HttpServletResponse response,
				@Valid @RequestBody AppointmentCreation appointment) {
		 Result result =new Result();
		 Map<String, Object> mapResult = new HashMap<>();
		String dateInString=appointment.getAppointmentDate();
		String timeInString=appointment.getAppointmentTime();
		 DateTimeFormatter df=DateTimeFormatter.ofPattern("dd/MM/yyyy");
		 DateTimeFormatter tf=DateTimeFormatter.ofPattern("HH:mm");
		 LocalDate date = LocalDate.parse(dateInString, df);
		 LocalTime time1=LocalTime.parse(timeInString, tf);
		 PatientEntity p=PatientRepo.findById(appointment.getPatientId()).orElseThrow(() ->
         new IllegalArgumentException("Patient with id " + appointment.getPatientId() + "could not be found."));
		 
		 DoctorsEntity dr=DoctorsRepo.findById(appointment.getDoctorId()).orElseThrow(() ->
         new IllegalArgumentException("Doctor with id " + appointment.getDoctorId() + "could not be found."));
		 
		 
		 //DoctorsEntity dr=doctorService.findDoctorById(appointment.getDoctorId());
		 AppointmentEntity appointmentSave=new AppointmentEntity();
		 appointmentSave.setAppointmentStatus(1);
		 appointmentSave.setAppointmentDate(date);
		 appointmentSave.setAppointmentTime(time1);
		 appointmentSave.setPatientObj(p);
		 appointmentSave.setDoctorObj(dr);
		 Result resultv= service.AddAppointment(appointmentSave);
			if (resultv.getStatusCode()=="0") {
				result.setStatusCode("0");
		 		result.setStatusDescription("Succ");
		 		mapResult.put(" added Succ", "the appointment added Succ");
		 		result.setResultMap(mapResult);
				return result;
			} else {
				result.setStatusCode("1");
	 			result.setStatusDescription("Failed");
	 			mapResult.put("failed while adding", resultv.getResultMap());
	 			result.setResultMap(mapResult);
				return result;
			}
			
			
		}*/
	 

}
