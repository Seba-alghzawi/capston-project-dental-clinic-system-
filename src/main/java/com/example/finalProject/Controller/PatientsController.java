package com.example.finalProject.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.example.finalProject.Entity.AppointmentEntity;
import com.example.finalProject.Entity.DoctorsEntity;
import com.example.finalProject.Entity.PatientEntity;
import com.example.finalProject.POGO.AppointmentCreation;
import com.example.finalProject.POGO.Login;
import com.example.finalProject.POGO.Result;
import com.example.finalProject.POGO.TokenUtility;
import com.example.finalProject.POGO.reportInfo;
import com.example.finalProject.Repository.DoctorsRepository;
import com.example.finalProject.Repository.PatientRepository;
import com.example.finalProject.Service.AppointmentService;
import com.example.finalProject.Service.PatientsService;
import com.example.finalProject.Service.doctorsService;


@RestController
@RequestMapping("/Patient")
public class PatientsController {

	
	
	@Autowired
	private PatientsService service;
	
	@Autowired
	private doctorsService drService;
	
	@Autowired
	private AppointmentService AppService;
	
	@Autowired
	private PatientRepository PatientRepo;
	
	@Autowired
	private DoctorsRepository DoctorsRepo;
	
	 @PostMapping ("/addPatient")
		public Result addPatient (HttpServletRequest request, HttpServletResponse response,@Valid @RequestBody PatientEntity patient) {
		 Result result =new Result();
		 Map<String, Object> mapResult = new HashMap<>();
			 Result resultv= service.addPatient(patient);
				if (resultv.getStatusCode()=="0") {
					result.setStatusCode("0");
			 		result.setStatusDescription("Succ");
			 		mapResult.put("added succ", "the patient add succ");
			 		result.setResultMap(mapResult);
					return result;
				} else {
					result.setStatusCode("1");
		 			result.setStatusDescription("Failed");
		 			mapResult.put("failed while add", resultv.getResultMap());
		 			result.setResultMap(mapResult);
					return result;
				}
			
		}
	 
	  @PostMapping("/Login")
	 public Result Login(@RequestBody @Valid Login login) {
		 return service.login(login) ;
	 }
	  
	 
	 @PostMapping ("/updatePatient")
	 public Result updatePatient (HttpServletRequest request, HttpServletResponse response,@Valid @RequestBody PatientEntity patient) {
		 Result TokenResult= TokenUtility.checkToken(request.getHeader("token"));
		 Result result =new Result();
		 Map<String, Object> mapResult = new HashMap<>();
		 if(TokenResult.getStatusCode()=="0") {
			 Result resultv= service.updatePatient(patient);
			 if (resultv.getStatusCode()=="0") {
					result.setStatusCode("0");
			 		result.setStatusDescription("Succ");
			 		mapResult.put("the patient update succ",  resultv.getResultMap());
			 		result.setResultMap(mapResult);
					return result;
				} else {
					result.setStatusCode("1");
		 			result.setStatusDescription("Failed");
		 			mapResult.put("failed while update", resultv.getResultMap());
		 			result.setResultMap(mapResult);
					return result;
				}
		 }
		 else
		 {
			 return TokenResult;
		 }
		 
	 }
	 
	 
	 @GetMapping("/AllDoctors")
	 public Result AllDoctors (HttpServletRequest request, HttpServletResponse response) {
		 Result TokenResult= TokenUtility.checkToken(request.getHeader("token"));
		 if(TokenResult.getStatusCode()=="0") {
			 	Result result =new Result();
				Map<String, Object> mapResult = new HashMap<>();
				List<DoctorsEntity> doctorsList=drService.SeeAllDoctors();
				List<String>doctors=new ArrayList<>();
				for(int i=0;i<doctorsList.size();i++)
				{
					doctors.add(doctorsList.get(i).getName());
				}
				result.setStatusCode("0");
		 		result.setStatusDescription("Succ");
		 		mapResult.put("the doctors ", doctors);
		 		result.setResultMap(mapResult);
				return result;
		}
		else
			{
				 return TokenResult;
			 }
		 
		
		 
	 }
	 
	 
	@DeleteMapping ("/CancelAppointment")
	public Result CancelAppointment (HttpServletRequest request, HttpServletResponse response,@RequestParam Integer AppId)
	{
		 Result TokenResult= TokenUtility.checkToken(request.getHeader("token"));
		 if(TokenResult.getStatusCode()=="0") {
			 Result result =new Result();
				Map<String, Object> mapResult = new HashMap<>();
				Result resultv=AppService.DeleteAppointment(AppId);
				if(resultv.getStatusCode()=="1")
				{
					result.setStatusCode("1");
		 			result.setStatusDescription("Failed");
		 			mapResult.put("error", resultv.getResultMap());
		 			result.setResultMap(mapResult);
					return result;
				}
				else
				{
					result.setStatusCode("0");
			 		result.setStatusDescription("Succ");
			 		mapResult.put("deleted ", resultv.getResultMap());
			 		result.setResultMap(mapResult);
					return result;
				}
		}
		else
			{
				 return TokenResult;
			 }
		
		
	}
	 
	 @GetMapping("/AvailableDoctors")
	public Result AvailableDoctors(HttpServletRequest request, HttpServletResponse response,@RequestParam String AppDate,@RequestParam String AppTime)
		{
		 
		 Result TokenResult= TokenUtility.checkToken(request.getHeader("token"));
		 if(TokenResult.getStatusCode()=="0") {
				Result result =new Result();
				Map<String, Object> mapResult = new HashMap<>();
				Result resultv=service.getAvailableDoctors(AppDate,AppTime);
				result.setStatusCode("0");
		 		result.setStatusDescription("Succ");
		 		mapResult.put("AvailableDoctors ", resultv.getResultMap());
		 		result.setResultMap(mapResult);
				return result;	
		}
		else
			{
				 return TokenResult;
			 }
			
			
		}
	 
	 
	 
 @PostMapping ("/addAppointment")
		public Result addAppointment (HttpServletRequest request, HttpServletResponse response,
				@Valid @RequestBody AppointmentCreation appointment) {
				Result TokenResult= TokenUtility.checkToken(request.getHeader("token"));
				 if(TokenResult.getStatusCode()=="0") {
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
				 Result resultv= AppService.AddAppointment(appointmentSave);
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
			}
			else
			{
				 return TokenResult;
			 }
	
		}

 
 
	 @PostMapping ("/ReportInfo")
		public Result ReportInfo (HttpServletRequest request, HttpServletResponse response,
				@Valid @RequestBody reportInfo info) {
		 Result TokenResult= TokenUtility.checkToken(request.getHeader("token"));
		 if(TokenResult.getStatusCode()=="0") {
			
			return service.reportInfoPatient(info);
		}
		else
			{
				 return TokenResult;
			 }
	 }
		
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
			@ExceptionHandler(MethodArgumentNotValidException.class)
			public Result handleValidationExceptions(MethodArgumentNotValidException ex) {
				Result result = new Result();
				result.setStatusCode("1");
	 			result.setStatusDescription("Failed");
				Map<String, Object> errors = new HashMap<>();
				ex.getBindingResult().getAllErrors().forEach((error) -> {
					String fieldName = ((FieldError) error).getField();
					String errorMessage = error.getDefaultMessage();
					errors.put(fieldName, errorMessage);
				});
				result.setResultMap(errors);
				return result;

			}

			@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
			@ExceptionHandler(Exception.class)
			public Result handleAllExceptionMethod(Exception ex, WebRequest requset, HttpServletResponse res) {
				Result result = new Result();
				result.setStatusCode("1");
	 			result.setStatusDescription("Failed");

				Map<String, Object> errors = new HashMap<>();
				errors.put("Exception", ex.getCause());
				result.setResultMap(errors);
				return result;
			}
	   

}
