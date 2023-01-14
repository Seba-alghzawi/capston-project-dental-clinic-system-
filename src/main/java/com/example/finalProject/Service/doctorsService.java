package com.example.finalProject.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.finalProject.Entity.AppointmentEntity;
import com.example.finalProject.Entity.DoctorsEntity;
import com.example.finalProject.Entity.PatientEntity;
import com.example.finalProject.POGO.AppointmentCreation;
import com.example.finalProject.POGO.Login;
import com.example.finalProject.POGO.Result;
import com.example.finalProject.POGO.TokenUtility;
import com.example.finalProject.POGO.drReport;
import com.example.finalProject.POGO.reportInfo;
import com.example.finalProject.Repository.DoctorsRepository;


@Service
public class doctorsService {

	

  @Autowired
	  private DoctorsRepository repo;
  
  
  @Autowired
  private AppointmentService AppService;
  
  
  @Autowired
 	private TokenUtility tokenUtility;

	  
	  
     public Result addDoctor (DoctorsEntity dr)
     {
    	 Result result = new Result();
 		Map<String, Object> mapResult = new HashMap<>();
 		String userPW=dr.getPassword();
 		String userName=dr.getUsername();
 		String nationalID=dr.getNationalID();
 		if (dr.getName()== null || dr.getName().isEmpty()) {
 			result.setStatusCode("1");
 			result.setStatusDescription("Failed");
			mapResult.put("NameEntity", "Cannot Send Name Entity Empty");
			result.setResultMap(mapResult);
			return result;
 		}
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
	     
     public Integer checkDoctorByUserName(String Username) {
    	 if (Username== null || Username.isEmpty()) {
 			return 0;
 		}
    	 else
    	 {
    		 if(repo.findDoctorByUserName(Username)==null) {
    			 return 0; 
    		 }else
    		 return repo.findDoctorByUserName(Username).getId();
    	 }
     }
	     
     
     public Integer checkDoctorByPW(String userPW) {
    	 if (userPW== null || userPW.isEmpty()) {
 			return 0;
 		}
    	 else
    	 {
    		 if(repo.findDoctorByPassword(userPW)==null)
    		 {
    			 return 0; 
    		 }
    		 else
    		 return repo.findDoctorByPassword(userPW).getId();
    	 }
     }
     
     public Integer checkDoctorByNationalID(String nationalID) {
    	 if (nationalID== null || nationalID.isEmpty()) {
 			return 0;
 		}
    	 else
    	 {
    		 if(repo.findDoctorByNationalID(nationalID)==null)
    		 {
    			 return 0; 
    		 }
    		 else
    		 return repo.findDoctorByNationalID(nationalID).getId();
    	 }
     }
     
     
     public DoctorsEntity findDoctorById(Integer Id)
     {
    	 if( Id>0 && Id!=null)
    	 return repo.findById(Id).orElse(new DoctorsEntity());
    	 else
    		 return new DoctorsEntity();
     }
     
     
     public Result updateDoctor(DoctorsEntity dr) {
    	 Result result = new Result();
  		Map<String, Object> mapResult = new HashMap<>();
  		Integer idOfDr=dr.getId();
  		if(idOfDr==null||idOfDr<0)
  		{
  			result.setStatusCode("1");
 			result.setStatusDescription("Failed");
  			mapResult.put("Id", "Incorrect Id");
  			result.setResultMap(mapResult);
  			return result;
  		}
  		if(repo.findById(idOfDr).isPresent())
  		{
  			DoctorsEntity dr1=repo.findById(idOfDr).get();
  			dr1=dr;
  			repo.save(dr1);
  			result.setStatusCode("0");
 			result.setStatusDescription("Succ");
  			mapResult.put("updatedPatient", dr1);
  			result.setResultMap(mapResult);
  			return result;
  		}
  		else
  		{
  			result.setStatusCode("1");
 			result.setStatusDescription("Failed");
  			mapResult.put("No Doctor", new DoctorsEntity());
  			result.setResultMap(mapResult);
  			return result;
  		}
     }
 
 
     
     public List<DoctorsEntity> SeeAllDoctors() {
    	 	
 			return repo.findAll();
     }
     
     
    public Result login(Login login)
     {
    	 Result result=new Result();
    	 Map<String,Object>mapResult=new HashMap<>();
    	 DoctorsEntity user=repo.findDoctorByUserName(login.getUsername());
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
    
    
    public String createCsvFile(drReport info)
      {
    	 File file=new File("C:\\Users\\HP\\Desktop\\filesTest\\DrReportCSV.csv");
     	Integer drId=info.getDrId();
     	String fromD=info.getFromDate();
     	String toD=info.getToDate();
     	List<AppointmentEntity> AppointmentInfo=AppService.reportInfoOfDr(drId,fromD ,toD);
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
		 
     	try (PrintWriter writer = new PrintWriter(file)) {
			writer.println("PatientId,DrId,appointmentD,appointmentT");
			
			ReportList.forEach(x -> {
			
				writer.println(x.getPatientId() + "," + x.getDoctorId()+ "," + x.getAppointmentDate() + "," + x.getAppointmentTime());

			});
			writer.flush();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return file.getAbsolutePath();
	
	  
    
      }
    
    
    
	}
