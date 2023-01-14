package com.example.finalProject.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.finalProject.Entity.AppointmentEntity;
import com.example.finalProject.Entity.DoctorsEntity;
import com.example.finalProject.Entity.PatientEntity;
import com.example.finalProject.POGO.AppointmentCreation;
import com.example.finalProject.POGO.AvailableDoctorsInfo;
import com.example.finalProject.POGO.Login;
import com.example.finalProject.POGO.Result;
import com.example.finalProject.POGO.TokenUtility;
import com.example.finalProject.POGO.reportInfo;
import com.example.finalProject.Repository.PatientRepository;


@Service
public class PatientsService {

	  @Autowired
	  private PatientRepository repo;
	  
	  @Autowired
		private AppointmentService AppService;
	  
	  @Autowired
	  private doctorsService drService;
	  
	  
	  @Autowired
	 	private TokenUtility tokenUtility;

		
	     public Result addPatient (PatientEntity patient) {
    	 Result result = new Result();
 		Map<String, Object> mapResult = new HashMap<>();
 		String userPW=patient.getPassword();
 		String userName=patient.getUsername();
 		if (patient.getName()== null || patient.getName().isEmpty()) {
 			result.setStatusCode("1");
 			result.setStatusDescription("Failed");
			mapResult.put("NameEntity", "Cannot Send Name Entity Empty");
			result.setResultMap(mapResult);
			return result;
 		}
 		if (patient.getId() != null) {
			if (patient.getId() < 0)
			result.setStatusCode("1");
 			result.setStatusDescription("Failed");
			mapResult.put("Id", "Cannot Send Id negative");
			result.setResultMap(mapResult);
			return result;
		}
 		if(userName!=null)
		{
			if(checkPatientByUserName(userName)!=0)
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
 			if(checkPatientByPW(userPW)!=0)
	 		{
	 			result.setStatusCode("1");
	 			result.setStatusDescription("Failed");
				mapResult.put("UserPassword", "this userPW is exist cannot be duplicated users");
				result.setResultMap(mapResult);
				return result;
	 		}
	 	
 		}
 		repo.save(patient);
 		result.setStatusCode("0");
 		result.setStatusDescription("Succ");
 		mapResult.put("added", "patient add successfully");
		result.setResultMap(mapResult);
		return result;
		
 	}
	     
     public Integer checkPatientByUserName(String Username) {
    	 if (Username== null || Username.isEmpty()) {
 			return 0;
 		}
    	 else
    	 {
    		 if(repo.findPatientByUserName(Username)==null) {
    			 return 0; 
    		 }else
    		 return repo.findPatientByUserName(Username).getId();
    	 }
     }
	     
     
     public Integer checkPatientByPW(String userPW) {
    	 if (userPW== null || userPW.isEmpty()) {
 			return 0;
 		}
    	 else
    	 {
    		 if(repo.findPatientByPassword(userPW)==null)
    		 {
    			 return 0; 
    		 }
    		 else
    		 return repo.findPatientByPassword(userPW).getId();
    	 }
     }

     
     
     public PatientEntity findPatientById(Integer Id)
     {
    	
    	 if( Id<0 || Id==null)
    	 {
    		
   			return new PatientEntity();
   			
    	 }
    	 else
    	 {
    		 return( repo.findById(Id).get());
    		 
    	 }
     }
     
     public Result updatePatient(PatientEntity patient) {
    	 Result result = new Result();
  		Map<String, Object> mapResult = new HashMap<>();
  		Integer idOfPatient=patient.getId();
  		if(idOfPatient==null||idOfPatient<0)
  		{
  			result.setStatusCode("1");
 			result.setStatusDescription("Failed");
  			mapResult.put("Id", "Incorrect Id");
  			result.setResultMap(mapResult);
  			return result;
  		}
  		if(repo.findById(idOfPatient).isPresent())
  		{
  			PatientEntity patient1=repo.findById(idOfPatient).get();
  			patient1=patient;
  			patient1.setUsername(patient1.getUsername());
  			repo.save(patient1);
  			result.setStatusCode("0");
 			result.setStatusDescription("Succ");
  			mapResult.put("updatedPatient", patient1);
  			result.setResultMap(mapResult);
  			return result;
  		}
  		else
  		{
  			result.setStatusCode("1");
 			result.setStatusDescription("Failed");
  			mapResult.put("No patient", new PatientEntity());
  			result.setResultMap(mapResult);
  			return result;
  		}
     }
     
     
     
     public Result seeAllPatients()
     {
    	 Result result = new Result();
   		Map<String, Object> mapResult = new HashMap<>();
   		result.setStatusCode("0");
		result.setStatusDescription("Succ");
		mapResult.put("All patients", repo.findAll());
		result.setResultMap(mapResult);
		return result;
     }
     
     
     public Result getAvailableDoctors(String AppDate, String AppTime)
     {
    	 Result result = new Result();
		 Map<String, Object> mapResult = new HashMap<>();
    	 List<DoctorsEntity>allDoctors=drService.SeeAllDoctors();
    	 List<AppointmentEntity> AppOfBookedDr=AppService.bookedDoctors(AppDate,AppTime);
    	 List<DoctorsEntity>bookedDoctors=new ArrayList<>();
    	 for(int i=0;i<AppOfBookedDr.size();i++)
    	 {
    		 bookedDoctors.add(AppOfBookedDr.get(i).getDoctorObj());
    	 }
    	 for(int i=0;i<bookedDoctors.size();i++)
    	 {
    		 DoctorsEntity bookedDr=bookedDoctors.get(i);
    		 allDoctors.remove(bookedDr);
    	 }
    	 List<AvailableDoctorsInfo>availableDrs=new ArrayList<>();
    	 for(int i=0;i<allDoctors.size();i++)
    	 {
    		 AvailableDoctorsInfo obj=new AvailableDoctorsInfo();
    		 Integer Id=allDoctors.get(i).getId();
    		 String Name=allDoctors.get(i).getName();
    		 obj.setDrID(Id);
    		 obj.setDrName(Name);
    		 availableDrs.add(obj);
    	 }
		    result.setStatusCode("0");
	 		result.setStatusDescription("Succ");
	 		mapResult.put("All available drs",availableDrs);
	 		result.setResultMap(mapResult);
	 		return result;
    	  
    	 
     }
     
     
     
      public Result login(Login login)
     {
    	 Result result=new Result();
    	 Map<String,Object>mapResult=new HashMap<>();
    	 PatientEntity user=repo.findPatientByUserName(login.getUsername());
    	 if(user==null)
    	 {
    		 mapResult.put("user","user not found");
    		 result.setStatusCode("1");
 			result.setStatusDescription("Failed");
    		 result.setResultMap(mapResult);
    		 return result;
    	 }
    	 if (!user.getPassword().equalsIgnoreCase(login.getPassword())) {

 			mapResult.put("passward", "Inncorect Password :(");
 			result.setStatusCode("1");
 			result.setStatusDescription("Failed");
 			result.setResultMap(mapResult);
 			return result;

 		}
 		String token = tokenUtility.generateToken(login.getUsername());
 		mapResult.put("token", token);
 		result.setStatusCode("0");
 		result.setStatusDescription("Succ");
 		result.setResultMap(mapResult);
 		return result;
    	 
    	
     }
      
      
      
      public Result reportInfoPatient(reportInfo info)
      {
    	  Result result=new Result();
     	 Map<String,Object>mapResult=new HashMap<>();
     	Integer pId=info.getPatientId();
     	String fromD=info.getFromDate();
     	String toD=info.getToDate();
     	
  		if(pId==null||pId<0)
  		{
  			result.setStatusCode("1");
 			result.setStatusDescription("Failed");
  			mapResult.put("Id", "Incorrect Id");
  			result.setResultMap(mapResult);
  			return result;
  		}
  		if(repo.findById(pId).isPresent())
  		{
  			
  			 List<AppointmentEntity> AppointmentInfo=AppService.reportInfoOfPatient(pId,fromD ,toD);
  			 List<AppointmentCreation>ReportList=new ArrayList<>();
  			 for(int i=0;i<AppointmentInfo.size();i++)
  			 {
  				 Integer PatientId=AppointmentInfo.get(i).getPatientObj().getId();
  				 Integer DrId=AppointmentInfo.get(i).getDoctorObj().getId();
  				 DateTimeFormatter df=DateTimeFormatter.ofPattern("dd/MM/yyyy");
  				 String appointmentD1=df.format(AppointmentInfo.get(i).getAppointmentDate());
  				 DateTimeFormatter tf=DateTimeFormatter.ofPattern("HH:mm");
  				 String appointmentT=tf.format(AppointmentInfo.get(i).getAppointmentTime());
  				 AppointmentCreation obj=new AppointmentCreation();
  				 obj.setPatientId(PatientId);
  				 obj.setDoctorId(DrId);
  				 obj.setAppointmentDate(appointmentD1);
  				 obj.setAppointmentTime(appointmentT);
  				 ReportList.add(obj);
  			 }
  			 result.setStatusCode("0");
  		 	 result.setStatusDescription("Succ");
  		 	 mapResult.put("report info", ReportList);
  		 	result.setResultMap(mapResult);
  	 		return result;
  	      }
  		else
  		{
  			result.setStatusCode("1");
 			result.setStatusDescription("Failed");
  			mapResult.put("No patient","No patient with id you entered");
  			result.setResultMap(mapResult);
  			return result;
  		}
  		
      }
     	
     	
     
     
	  
}
