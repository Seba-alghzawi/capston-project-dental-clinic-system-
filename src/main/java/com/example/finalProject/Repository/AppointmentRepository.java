package com.example.finalProject.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.finalProject.Entity.AppointmentEntity;
import com.example.finalProject.Entity.DoctorsEntity;


@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity,Integer> {

 @Query(nativeQuery = true, value = "SELECT * FROM appointment e  where e.doctorId=?1 and (e.appointmentDate=?2) and (e.appointmentTime BETWEEN '08:00' and '17:00')")
List< AppointmentEntity >findAppointmentOfDr(Integer id,LocalDate date);
 
  @Query(nativeQuery = true, value = "SELECT * FROM appointment e  where e.doctorId=?1 and (e.appointmentDate=?2) and (e.appointmentTime=?3)")
   AppointmentEntity CheckIfThereIsAppointment(Integer DrId,LocalDate date,LocalTime time);
 
 
  @Query(nativeQuery = true, value = "SELECT * FROM appointment e  where e.doctorId=?1 and (e.appointmentDate=?2)")
  List< AppointmentEntity > findAvailableTime(Integer DrId,LocalDate date);


  @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM appointment e where e.doctorId=?1 and (e.patientId=?2) and (e.appointmentStatus=0)")
  Integer countPatientVisited(Integer DrId,Integer patientId);

  @Query(nativeQuery = true, value = "SELECT * FROM appointment e where e.appointmentDate=?1 and (e.appointmentTime=?2)")
  List<AppointmentEntity> bookedDoctors(LocalDate date,LocalTime time);

   @Query(nativeQuery = true, value = "SELECT * FROM appointment e where e.patientId=?1 and ((e.appointmentDate BETWEEN ?2 AND ?3))")
  List<AppointmentEntity> ReportInfoOfPatient(Integer patientId,LocalDate date1,LocalDate date2);

   @Query(nativeQuery = true, value = "SELECT * FROM appointment e where e.doctorId=?1 and ((e.appointmentDate BETWEEN ?2 AND ?3))")
   List<AppointmentEntity> ReportInfoOfDr(Integer DrId,LocalDate date1,LocalDate date2);

  
  // >= ?2 and e.appointmentDate <= ?3
}


