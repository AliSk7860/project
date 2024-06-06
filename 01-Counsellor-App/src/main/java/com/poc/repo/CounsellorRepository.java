package com.poc.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.entity.Counsellor;

public interface CounsellorRepository extends JpaRepository<Counsellor, Integer> {

	
	public Counsellor findByEmailAndPwd(String email,String pwd);
	
	public Counsellor findByEmail(String email);
}
