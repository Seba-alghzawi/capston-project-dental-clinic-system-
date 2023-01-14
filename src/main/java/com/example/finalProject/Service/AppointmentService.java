package com.example.finalProject.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.query.criteria.internal.expression.function.CurrentDateFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.finalProject.Entity.AppointmentEntity;
import com.example.finalProject.Entity.DoctorsEntity;
import com.example.finalProject.Entity.PatientEntity;
import com.example.finalProject.POGO.Result;
import com.example.finalProject.Repository.AppointmentRepository;
import com.example.finalProject.Repository.DoctorsRepository;
import com.example.finalProject.Repository.PatientRepository;

@Service
public class AppointmentService {

	 @Autowired
	 private AppointmentRepository repo;
	 
	 @Autowired
	 private PatientRepository patientRepo;
	 
	 @Autowired
	 private DoctorsRepository DrRepo;
	 
	 public List<AppointmentEntity> findAppointment( Integer DrId)
	 {
//		 DateTimeFormatter df=DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return ( repo.findAppointmentOfDr(DrId,LocalDate.now()));
	 }
	 
	 
	   public Result AddAppointment (AppointmentEntity appointment) {
    	 Result result = new Result();
 		Map<String, Object> mapResult = new HashMap<>();
 		
 		if (appointment.getAppointmentDate()== null || appointment.getAppointmentTime()==null||appointment.getPatientObj()==null) {
 			result.setStatusCode("1");
 			result.setStatusDescription("Failed");
			mapResult.put("time or date", "time or date Cannot be Empty");
			result.setResultMap(mapResult);
			return result;
 		}
 		if(checkExistanceOfAppointment(appointment.getDoctorObj().getId(),appointment.getAppointmentDate(),appointment.getAppointmentTime())==0)
 		{
 			result.setStatusCode("1");
 			result.setStatusDescription("Failed");
			mapResult.put("appointment", "there is an appointment at this time");
			result.setResultMap(mapResult);
			return result;
 		}
 		if(appointment.getAppointmentTime().getMinute()>0)
 		{
 			result.setStatusCode("1");
 			result.setStatusDescription("Failed");
			mapResult.put("error", "cant specify minutes please select hour");
			result.setResultMap(mapResult);
			return result;
 		}
 		if(appointment.getAppointmentTime().getHour()>17||appointment.getAppointmentTime().getHour()<8)
 		{
 			result.setStatusCode("1");
 			result.setStatusDescription("Failed");
			mapResult.put("error", "doctor not available at this time");
			result.setResultMap(mapResult);
			return result;
 		}
 			repo.save(appointment);
 			result.setStatusCode("0");
			result.setStatusDescription("succ");
		mapResult.put("appointment added succ", appointment);
		result.setResultMap(mapResult);
		return result;
// 		if(appointment.getPatientObj().getId()<0)
// 		{
// 			result.setStatusCode("1");
// 			result.setStatusDescription("Failed");
//			mapResult.put("Patient id", "can't be negative");
//			result.setResultMap(mapResult);
//			return result;
// 		}
 		
 		
 		
 		}
	   
	   
	   public List<LocalTime> availableTime(Integer DrId)
	   {
		   List<LocalTime>AllTimes=new ArrayList<>();
		   for(int i=8;i<17;i++)
		  {
			   LocalTime x=LocalTime.of(i, 0);
			   AllTimes.add(x);
		  }
		  if( DrRepo.findById(DrId).isPresent())
		  {
			  List<AppointmentEntity>bookedApp= repo.findAvailableTime(DrId, LocalDate.now());
//				 .stream().map(x->(x.getAppointmentTime())).forEach(x->timesBooked.add(x));
				   //.stream().forEach((x)->timesBooked.add(x.getAppointmentTime()));
				   for(int i=0;i<bookedApp.size();i++)
					  {
					   LocalTime bookedTime=bookedApp.get(i).getAppointmentTime();
					   AllTimes.remove(bookedTime);
					  }
				  
				 return AllTimes;
				   
		  }
		  else
		  {
			  return AllTimes;
		  }
		
	   }
	   
	   
	   
	   public Result updateStatusOfAppointment(Integer appId,Integer status)
	   {
		   Result result =new Result();
		   Map<String, Object> mapResult = new HashMap<>();
		   if(appId==null||appId<0)
	  		{
	  			result.setStatusCode("1");
	 			result.setStatusDescription("Failed");
	  			mapResult.put("Id", "Incorrect Id");
	  			result.setResultMap(mapResult);
	  			return result;
	  		}
		   if(repo.findById(appId).isPresent())
		   {
			   AppointmentEntity updatedAppointment= repo.findById(appId).get();
			   updatedAppointment.setAppointmentStatus(status);
			   repo.save(updatedAppointment);
			   result.setStatusCode("0");
	 			result.setStatusDescription("Succ");
	  			mapResult.put("updated Appointment", updatedAppointment);
	  			result.setResultMap(mapResult);
	  			return result;
		   }
		   else
	  		{
	  			result.setStatusCode("1");
	 			result.setStatusDescription("Failed");
	  			mapResult.put("No Appointment", new AppointmentEntity());
	  			result.setResultMap(mapResult);
	  			return result;
	  		}
	   }
	   
	   
	   public Result  patientVisitedCount(Integer drId,Integer patientId)
	   {
		   Result result =new Result();
		   Map<String, Object> mapResult = new HashMap<>();
		   if(drId==null||drId<0)
	  		{
	  			result.setStatusCode("1");
	 			result.setStatusDescription("Failed");
	  			mapResult.put("Id", "Incorrect Id");
	  			result.setResultMap(mapResult);
	  			return result;
	  		}
		   if( patientId==null||patientId<0)
		   {
			   result.setStatusCode("1");
	 			result.setStatusDescription("Failed");
	  			mapResult.put("Id", "Incorrect Id");
	  			result.setResultMap(mapResult);
	  			return result;
		   }
		   if(DrRepo.findById(drId).isPresent()&&patientRepo.findById(patientId).isPresent())
		   {
			   Integer visitedCount=repo.countPatientVisited(drId, patientId);
			   result.setStatusCode("0");
	 			result.setStatusDescription("Succ");
	  			mapResult.put("the times patient visited the doctor", visitedCount);
	  			result.setResultMap(mapResult);
	  			return result;
		   }
		   else
	  		{
	  			result.setStatusCode("1");
	 			result.setStatusDescription("Failed");
	  			mapResult.put("error in ids", "No patient or doctor with these ids ");
	  			result.setResultMap(mapResult);
	  			return result;
	  		}
	   }
	   
	   
	   
	   public Result DeleteAppointment(Integer appId)
	   {
		   Result result =new Result();
		   Map<String, Object> mapResult = new HashMap<>();
		   if(appId==null||appId<0)
	  		{
	  			result.setStatusCode("1");
	 			result.setStatusDescription("Failed");
	  			mapResult.put("Id", "Incorrect Id");
	  			result.setResultMap(mapResult);
	  			return result;
	  		}
		   if(repo.findById(appId).isPresent())
		   {
			   repo.deleteById(appId);
			   result.setStatusCode("0");
	 			result.setStatusDescription("Succ");
	  			mapResult.put("deleted", "the appointment deleted succ");
	  			result.setResultMap(mapResult);
	  			return result;
		   }
		   else
	  		{
	  			result.setStatusCode("1");
	 			result.setStatusDescription("Failed");
	  			mapResult.put("No Appointment", new AppointmentEntity());
	  			result.setResultMap(mapResult);
	  			return result;
	  		}
	   }
	   
	   
	   
	   public List<AppointmentEntity> bookedDoctors(String appointmentD,String appointmentT)
	   {
		   		Result result =new Result();
			 Map<String, Object> mapResult = new HashMap<>();
			 String dateInString=appointmentD;
			 String timeInString=appointmentT;
			 DateTimeFormatter df=DateTimeFormatter.ofPattern("dd/MM/yyyy");
			 DateTimeFormatter tf=DateTimeFormatter.ofPattern("HH:mm");
			 LocalDate date = LocalDate.parse(dateInString, df);
			 LocalTime time1=LocalTime.parse(timeInString, tf);
			 return repo.bookedDoctors(date, time1);
	   }
	   
	   
	   
	   public List<AppointmentEntity> reportInfoOfPatient(Integer patientId,String appointmentD1,String appointmentD2)
	   {
//		   	String dateInString1=appointmentD1;
//			 String dateInString2=appointmentD2;
			 DateTimeFormatter df=DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			 DateTimeFormatter tf=DateTimeFormatter.ofPattern("HH:mm");
			 LocalDate date1 = LocalDate.parse(appointmentD1, df);
			 LocalDate date2 = LocalDate.parse(appointmentD2, df);
		   return repo.ReportInfoOfPatient(patientId, date1, date2);
	   }
	   
	   
	   public List<AppointmentEntity> reportInfoOfDr(Integer drId,String appointmentD1,String appointmentD2)
	   {
		   DateTimeFormatter df=DateTimeFormatter.ofPattern("dd/MM/yyyy");
		   LocalDate date1 = LocalDate.parse(appointmentD1, df);
			 LocalDate date2 = LocalDate.parse(appointmentD2, df);
		   return repo.ReportInfoOfDr(drId, date1, date2);
	   }
	   
	   public Integer checkExistanceOfAppointment(Integer DrId,LocalDate date,LocalTime time)
	   {
		   
			   if(repo.CheckIfThereIsAppointment(DrId,date, time)!=null)
			   {
				   return 0;
			   }
			   else
				   return 1;
		   
	   }
	   
	   
	   
	   
	   
	   
	   /*
 		if (dr.getId() != null) {
			if (dr.getId() < 0)
			result.setStatusCode("1");
 			result.setStatusDescription("Failed");
			mapResult.put("Id", "Cannot Send Id negative");
			result.setResultMap(mapResult);
			return result;
		}
 		if(userName!=null)
		{
			if(checkDoctorByUserName(userName)!=0)
	 		{
	 			result.setStatusCode("1");
	 			result.setStatusDescription("Failed");
				mapResult.put("username", "this username is exist cannot be duplicated users");
				result.setResultMap(mapResult);
				return result;
	 		}
	 	
		}
 		if(userPW!=null)
 		{
 			if(checkDoctorByPW(userPW)!=0)
	 		{
	 			result.setStatusCode("1");
	 			result.setStatusDescription("Failed");
				mapResult.put("UserPassword", "this userPW is exist cannot be duplicated users");
				result.setResultMap(mapResult);
				return result;
	 		}
 			
	 	
 		}
 		if(nationalID!=null)
 		{
 			if(checkDoctorByNationalID(nationalID)!=0)
	 		{
	 			result.setStatusCode("1");
	 			result.setStatusDescription("Failed");
				mapResult.put("nationalID", "this nationalID is exist cannot be duplicated users");
				result.setResultMap(mapResult);
				return result;
	 		}
 		}
 		repo.save(dr);
 		result.setStatusCode("0");
 		result.setStatusDescription("Succ");
 		mapResult.put("added", "dr added successfully");
		result.setResultMap(mapResult);
		return result;
		
 	}
	  */
	 
}
