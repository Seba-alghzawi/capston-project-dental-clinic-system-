package com.example.finalProject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.finalProject.Entity.PatientEntity;

@Repository
public interface PatientRepository extends JpaRepository< PatientEntity,Integer> {
	
	@Query(value = "SELECT e FROM PatientEntity e where e.username=:username " )
	PatientEntity findPatientByUserName(String username);
	
	@Query(value = "SELECT e FROM PatientEntity e where e.password=:password " )
	PatientEntity findPatientByPassword(String password);

}
