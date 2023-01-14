package com.example.finalProject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.finalProject.Entity.DoctorsEntity;

@Repository
public interface DoctorsRepository extends JpaRepository<DoctorsEntity,Integer> {


	 @Query(value = "SELECT e FROM DoctorsEntity e where e.username=:username " )
	DoctorsEntity findDoctorByUserName(String username);
	
	@Query(value = "SELECT e FROM DoctorsEntity e where e.password=:password " )
	DoctorsEntity findDoctorByPassword(String password);
	
	@Query(value = "SELECT e FROM DoctorsEntity e where e.nationalID=:nationalID " )
	DoctorsEntity findDoctorByNationalID(String nationalID);
	 
}
