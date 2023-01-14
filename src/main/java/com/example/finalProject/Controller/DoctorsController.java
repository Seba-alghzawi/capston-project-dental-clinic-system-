package com.example.finalProject.Controller;

import java.awt.PageAttributes.MediaType;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.http.HttpHeaders;
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
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.finalProject.POGO.PatientInfoBookedTime;
import com.example.finalProject.POGO.Result;
import com.example.finalProject.POGO.TokenUtility;
import com.example.finalProject.POGO.drReport;
import com.example.finalProject.POGO.Login;
import com.example.finalProject.Repository.DoctorsRepository;
import com.example.finalProject.Repository.PatientRepository;
import com.example.finalProject.Service.AppointmentService;
import com.example.finalProject.Service.PatientsService;
import com.example.finalProject.Service.doctorsService;


@RestController
@RequestMapping("/Doctor")
public class DoctorsController {

	@Autowired
	private doctorsService service;
	
	@Autowired
	private PatientRepository PatientRepo;
	
	@Autowired
	private DoctorsRepository DoctorsRepo;
	
	@Autowired
	private AppointmentService AppService;
	
	@Autowired
	private PatientsService patientService;
	
	
	 @PostMapping ("/addDoctor")
		public Result addDoctor (HttpServletRequest request, HttpServletResponse response,@Valid @RequestBody DoctorsEntity dr) {
		 Result result =new Result();
		 Map<String, Object> mapResult = new HashMap<>();
		 Result resultv= service.addDoctor(dr);
			if (resultv.getStatusCode()=="0") {
				result.setStatusCode("0");
		 		result.setStatusDescription("Succ");
		 		mapResult.put("added succ", "the dr added succ");
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
	  @PostMapping("/Login")
	 public Result Login(@RequestBody @Valid Login login) {
		 return service.login(login) ;
	 }

	 
	 
	 @PostMapping ("/updateDoctor")
	 public Result updatePatient (HttpServletRequest request, HttpServletResponse response,@Valid @RequestBody DoctorsEntity dr) {
		 Result TokenResult= TokenUtility.checkToken(request.getHeader("token"));
		 if(TokenResult.getStatusCode()=="0") {
			 Result result =new Result();
			 Map<String, Object> mapResult = new HashMap<>();
			 Result resultv= service.updateDoctor(dr);
			 if (resultv.getStatusCode()=="0") {
					result.setStatusCode("0");
			 		result.setStatusDescription("Succ");
			 		mapResult.put("update succ", "the dr updated succ");
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
	 
		@GetMapping("/findBookedTimeToday")
		public Result BookedTimesOfDr(HttpServletRequest request, HttpServletResponse response,@RequestParam Integer DrId ) {
			Result TokenResult= TokenUtility.checkToken(request.getHeader("token"));
			 if(TokenResult.getStatusCode()=="0") {
				 Result result =new Result();
				 Map<String, Object> mapResult = new HashMap<>();
				List<AppointmentEntity> app= AppService.findAppointment(DrId) ;
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
						p.setPatientID(app.get(i).getPatientObj().getId());
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
		
		@PostMapping ("/getAvailableTimes")
		public Result getAvailableTimes (HttpServletRequest request, HttpServletResponse response,
				 @RequestParam Integer DrId) {
			Result TokenResult= TokenUtility.checkToken(request.getHeader("token"));
			 if(TokenResult.getStatusCode()=="0") {
				 Result result =new Result();
				 Map<String, Object> mapResult = new HashMap<>();
				List<LocalTime> Avatimes= AppService.availableTime(DrId) ;
				if(!DoctorsRepo.findById(DrId).isPresent())
				{
					result.setStatusCode("1");
			 		result.setStatusDescription("failed");
			 		mapResult.put("drId ", "no dr with this id");
			 		result.setResultMap(mapResult);
					return result;
				}
				if(Avatimes.size()==0)
				{
					result.setStatusCode("0");
		 			result.setStatusDescription("Succ");
		 			mapResult.put("AvaialableTimes","no available times today");
		 			result.setResultMap(mapResult);
					return result;
		
				}
				if(Avatimes.size()!=0 &&Avatimes.size()==9)
				{
					result.setStatusCode("0");
		 			result.setStatusDescription("Succ");
		 			mapResult.put("AvaialableTimes","all times are available today");
		 			result.setResultMap(mapResult);
					return result;
		
				}
				else
				{
					result.setStatusCode("0");
			 		result.setStatusDescription("Succ");
			 		mapResult.put("available time today ", Avatimes);
			 		result.setResultMap(mapResult);
					return result;
				}
			}
			else
				{
					 return TokenResult;
				 }
		
				
		}
		
		@GetMapping ("/UpdateAppointmentStatus")
		public Result UpdateAppointmentStatus (HttpServletRequest request, HttpServletResponse response,
				 @RequestParam Integer appointmentID,@RequestParam Integer status) {
			Result TokenResult= TokenUtility.checkToken(request.getHeader("token"));
			 if(TokenResult.getStatusCode()=="0") {
				 Result result =new Result();
					Map<String, Object> mapResult = new HashMap<>();
					Result resultv=AppService.updateStatusOfAppointment(appointmentID, status);
					if(resultv.getStatusCode()=="1")
					{
						result.setStatusCode("1");
			 			result.setStatusDescription("Failed");
			 			mapResult.put("failed while updating", resultv.getResultMap());
			 			result.setResultMap(mapResult);
						return result;
					}
					else
					{
						result.setStatusCode("0");
				 		result.setStatusDescription("Succ");
				 		mapResult.put("appointment apdated succ ",resultv.getResultMap());
				 		result.setResultMap(mapResult);
						return result;
					}
			}
			else
				{
					 return TokenResult;
				 }
	
		}
		
		@GetMapping ("/patientVisitedCount")
		public Result patientVisitedCount (HttpServletRequest request, HttpServletResponse response,
				 @RequestParam Integer drID,@RequestParam Integer PatientId) {
			Result TokenResult= TokenUtility.checkToken(request.getHeader("token"));
			 if(TokenResult.getStatusCode()=="0") {
					Result result =new Result();
					Map<String, Object> mapResult = new HashMap<>();
					Result resultv=AppService.patientVisitedCount(drID, PatientId);
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
				 		mapResult.put("patient Visited Count ",resultv.getResultMap());
				 		result.setResultMap(mapResult);
						return result;
					}
			}
			else
					{
						 return TokenResult;
					 }
			
		
		}
		
		@GetMapping ("/AllPatients")
		public Result AllPatients (HttpServletRequest request, HttpServletResponse response)
		{Result TokenResult= TokenUtility.checkToken(request.getHeader("token"));
		 if(TokenResult.getStatusCode()=="0") {
			 Result result =new Result();
				result=patientService.seeAllPatients();
				return result;
		 }
		 else
	 		{
	 			 return TokenResult;
	 		 }
			
		}
		
		
		@GetMapping ("/FindPatientById")
		public Result FindPatientById (HttpServletRequest request, HttpServletResponse response,@RequestParam Integer PatientId)
		{
			Result TokenResult= TokenUtility.checkToken(request.getHeader("token"));
			 if(TokenResult.getStatusCode()=="0") {
				 Result result =new Result();
					Map<String, Object> mapResult = new HashMap<>();
					PatientEntity patient=patientService.findPatientById(PatientId);
					result.setStatusCode("0");
			 		result.setStatusDescription("Succ");
			 		mapResult.put("the patient ", patient);
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
		
		
		 @GetMapping(value = "/downloadCsvOfDrReport", produces = org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public Object downloadCsvOfDrReport(HttpServletRequest request, HttpServletResponse response
			,@Valid @RequestParam Integer drId,@RequestParam String fromD,@RequestParam String toD) {
			 drReport info=new drReport();
			 info.setDrId(drId);
			 info.setFromDate(fromD);
			 info.setToDate(toD);
			 Result TokenResult= TokenUtility.checkToken(request.getHeader("token"));
			 if(TokenResult.getStatusCode()=="0") {
				 String pathFile = service.createCsvFile(info);
					InputStream input = null;
					try {
						input = new BufferedInputStream(new FileInputStream(new File(pathFile)));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					InputStreamResource resource = new InputStreamResource(input);
					return resource;
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
